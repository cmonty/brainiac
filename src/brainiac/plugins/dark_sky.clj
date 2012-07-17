(ns brainiac.plugins.dark-sky
  (:require [brainiac.plugin :as brainiac]
            [clojure.contrib.json :as json :only (read-json)]
            [clojure.java.io :as io :only (reader)]
            [clojure.contrib.string :as string]))


(defn format-forecast [forecast]
  {
    :temp (:currentTemp forecast)
    :current-summary (:currentSummary forecast)
    :hour-summary (:hourSummary forecast)
    :minutes-until-change (:minutesUntilChange forecast)
  })

(defn transform [stream]
  (let [json (json/read-json (io/reader stream))]
    (assoc {}
      :name "dark-sky"
      :title "Right now, outside..."
      :data (format-forecast json))))

(defn url [api-key lat lon]
  (format "https://api.darkskyapp.com/v1/brief_forecast/%s/%s,%s" api-key lat lon))

(defn configure [{:keys [api-key lat lon program-name]}]
  (brainiac/simple-http-plugin
    {:url (url api-key lat lon)}
    transform program-name))
