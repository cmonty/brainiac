(ns brainiac.plugins.twitter-search
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]))

(defn format-tweet [tweet]
  (format "%s: %s" (:from_user_name tweet) (:text tweet)))

(defn transform [stream]
  (let [json (read-json (reader stream))]
  (assoc {}
    :name "twitter-search"
    :type "ticker"
    :data (map format-tweet (:results json)))))

(defn search-url [term]
  (format "http://search.twitter.com/search.json?q=%s" term))

(defn configure [{:keys [term]}]
  (brainiac/schedule
    30000
    #(brainiac/simple-http-plugin
       {:url (search-url term)}
       transform)))
