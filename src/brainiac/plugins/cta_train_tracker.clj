(ns brainiac.plugins.cta-train-tracker
  (:import [java.text SimpleDateFormat]
           [java.util TimeZone])
  (:require [brainiac.plugin :as brainiac]
            [brainiac.xml-utils :as xml]
            [clojure.contrib.zip-filter.xml :as zf]))

(def time-format
  (let [date-format (SimpleDateFormat. "yyyyMMdd HH:mm:ss")]
    (.setTimeZone date-format (TimeZone/getTimeZone "America/Chicago"))
    date-format))

(defn now [] (System/currentTimeMillis))

(defn due-in-minutes [due-in-millis]
  (if (< due-in-millis (* 60 1000))
    "DUE"
    (str (int (/ due-in-millis (* 60 1000))) " MIN")))

(def destination-translations
  {"Org" "Orange"
   "G" "Green"
   "Blue" "Blue"
   "Brn" "Brown"
   "Pink" "Pink"
   "P" "Purple"
   "Red" "Red"})

(defn parse-eta [node]
  (let [arrival-time (.parse time-format (zf/xml1-> node :arrT zf/text))
        due-in-millis (- (.getTime arrival-time) (now))
        line (zf/xml1-> node :rt zf/text)
        destination (zf/xml1-> node :destNm zf/text)]
    (assoc {}
           :destination destination
           :line (get destination-translations line)
           :due-in-millis due-in-millis
           :arrival-time (due-in-minutes due-in-millis))))

(defn transform [stream]
  (let [xml-zipper (xml/parse-xml stream)
        stop (zf/xml1-> xml-zipper :eta :staNm zf/text)
        route (zf/xml1-> xml-zipper :eta :rt zf/text)]
    (assoc {}
      :name "cta-train-tracker"
      :station stop
      :data (vec (take 7 (sort-by :due-in-millis (zf/xml-> xml-zipper :eta parse-eta)))))))

(defn url [map-id api-key]
  (format "http://lapi.transitchicago.com/api/1.0/ttarrivals.aspx?mapid=%s&key=%s" map-id api-key))

(defn configure [{:keys [program-name map-id api-key]}]
  (brainiac/simple-http-plugin
    {:method :get :url (url map-id api-key)}
    transform program-name))

