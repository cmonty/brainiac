(ns brainiac.plugins.jukebox
  (:import [java.text SimpleDateFormat]
           [java.util TimeZone])
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]))

(defn fix-default-artwork [artwork-url jukebox-url]
  (if (re-matches #"^/.*" artwork-url)
    (str jukebox-url artwork-url)
    artwork-url))

(defn transform [jukebox-url stream]
  (let [json (read-json (reader stream))
        artist (:artist json)
        album (:album json)
        title (:title json)
        requester (:requester json)
        artwork (-> json :artwork :extra-large (fix-default-artwork jukebox-url))]
  { :name "jukebox"
    :artist artist
    :album album
    :title title
    :requester requester
    :artwork artwork }))

(defn current-track-url [base-url]
  (format "%s/playlist/current-track" base-url))

(defn configure [{:keys [url program-name]}]
  (brainiac/simple-http-plugin
    {:url (current-track-url url) :headers {"Accept" "application/json"}}
    (partial transform url) program-name))
