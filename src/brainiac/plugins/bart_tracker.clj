(ns brainiac.plugins.bart-tracker
  (:require [brainiac.plugin :as brainiac]
            [brainiac.xml-utils :as xml]
            [clojure.contrib.zip-filter.xml :as zf]
            [clojure.string :as string]))

(defn advisory-url [api-key]
  (format "http://api.bart.gov/api/bsa.aspx?cmd=bsa&key=%s&date=today" api-key))

(defn schedule-url [station-id api-key]
  (format "http://api.bart.gov/api/etd.aspx?cmd=etd&orig=%s&key=%s" station-id api-key))

(defn minutes-display [minutes]
  (if (= minutes "Leaving")
    "now"
    (str minutes "m")))

(defn parse-estimate [node]
  (let [minutes (zf/xml1-> node :minutes zf/text)
        color (string/lower-case (zf/xml1-> node :color zf/text))
        length (zf/xml1-> node :length zf/text)]
    (assoc {}
           :minutes (minutes-display minutes)
           :color color
           :length length)))

(defn parse-departure [node]
  (let [destination (zf/xml1-> node :destination zf/text)]
    (assoc {}
           :destination destination
           :estimates (take 3 (zf/xml-> node :estimate parse-estimate)))))

(defn transform-station [stream]
  (let [xml-zipper (xml/parse-xml stream)
        station (zf/xml1-> xml-zipper :station :name zf/text)]
    (assoc {}
           :name "bart-tracker"
           :station station
           :data (take 7 (zf/xml-> xml-zipper :station :etd parse-departure)))))

(defn configure [{:keys [station-id api-key program-name]}]
  (brainiac/schedule
    20000
    (brainiac/simple-http-plugin
      {:method "GET" :url (schedule-url station-id api-key)}
      transform-station program-name)))

