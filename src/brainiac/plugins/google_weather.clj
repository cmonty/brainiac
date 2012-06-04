(ns brainiac.plugins.google-weather
  (:require [brainiac.plugin :as brainiac]
            [brainiac.xml-utils :as xml]
            [clojure.contrib.string :as string]
            [clojure.contrib.zip-filter.xml :as zf]))

(defn parse-conditions [node]
    {
      :temp (zf/xml1-> node :temp_f (zf/attr :data))
      :current-conditions (string/ltrim (zf/xml1-> node :condition (zf/attr :data)))
     })

(defn transform [stream]
  (let [xml-zipper (xml/parse-xml stream)
        current (zf/xml1-> xml-zipper :weather :current_conditions parse-conditions)]
    (assoc {}
      :name "google-weather"
      :type "weather"
      :title "Right now, outside..."
      :data current)))

(defn html []
  [:script#weather-template {:type "text/mustache"}
   "<h3>{{title}}</h3> <h2 class='temperature'>{{data.temp}}&deg;</h2>
		<h4 class='conditions'>{{data.current-conditions}}</h4>"])

(defn url [city]
  (format "http://www.google.com/ig/api?weather=%s" city))

(defn configure [{:keys [city program-name]}]
  (brainiac/simple-http-plugin
    {:url (url city)}
    transform program-name))
