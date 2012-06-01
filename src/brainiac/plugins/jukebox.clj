(ns brainiac.plugins.jukebox
  (:import [java.text SimpleDateFormat]
           [java.util TimeZone])
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]
            [brainiac.pages.templates :as templates]))

(defn transform [stream]
  (let [json (read-json (reader stream))
        artist (:artist json)
        album (:album json)
        title (:title json)
        requester (:requester json)
        artwork (:artwork json)]
  (assoc {}
    :name "jukebox"
    :type "jukebox"
    :artist artist
    :album album
    :title title
    :requester requester
    :artwork artwork
         )))

(defn jukebox-url [base-url]
  (format "%s/playlist/current-track" base-url))

(defn html []
  [:script#jukebox-template {:type "text/mustache"}
   "<h3>Now Playing</h3><p>{{artist}}</p><p>{{title}}</p><p><img src=\"{{artwork}}\"/></p><p>{{album}}</p>"])

(defn configure [{:keys [url program-name]}]
  (brainiac/simple-http-plugin
    {:url (jukebox-url url) :headers {"Accept" "application/json"}}
    transform program-name))
