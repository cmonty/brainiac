(ns brainiac.plugins.google-weather
  (:require [brainiac.plugin :as brainiac]
            [brainiac.xml-utils :as xml]
            [clojure.contrib.zip-filter.xml :as zf]))

(defn parse-conditions [node]
  (assoc {}
    :temp (zf/xml1-> node :temp_f (zf/attr :data))
    :icon (str "http://www.google.com" (zf/xml1-> node :icon (zf/attr :data)))
    :current-conditions (zf/xml1-> node :condition (zf/attr :data))))

(defn transform [stream]
  (let [xml-zipper (xml/parse-xml stream)
        current (zf/xml1-> xml-zipper :weather :current_conditions parse-conditions)]
    (assoc {}
      :name "google-weather"
      :type "weather"
      :title "Weather"
      :data current)))

(defn weather-url [city]
  (format "http://www.google.com/ig/api?weather=%s" city))

(defn configure [{:keys [city]}]
  (brainiac/schedule
    5000
    #(brainiac/simple-http-plugin
       {:url (weather-url city)}
       transform)))
