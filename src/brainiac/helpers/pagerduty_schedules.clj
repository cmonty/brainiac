(ns brainiac.helpers.pagerduty-schedules
  (:import [java.util Calendar TimeZone]
           [java.text SimpleDateFormat])
  (:require [brainiac.plugin :as brainiac]
						[clojure.string :as string]
            [clojure.contrib.json :as json]
            [clojure.java.io :as io])
  (:use [brainiac.helpers.gravatar :only [email->gravatar]]))

(defn now []
  (.getTime (Calendar/getInstance)))

(defn format-date [date]
  (let [date-formatter (doto (SimpleDateFormat. "yyyy-MM-dd'T'HH:mmZ")
                         (.setTimeZone (TimeZone/getTimeZone "America/Chicago")))]
    (.format date-formatter date)))

(defn url [organization schedule]
  (fn []
    (let [current-time (format-date (now))]
      (format "https://%s.pagerduty.com/api/v1/schedules/%s/entries?since=%s&until=%s&overflow=true" organization schedule current-time current-time))))

(defn- json->name [json]
  (-> json :entries first :user :name))

(defn- json->email [json]
  (-> json :entries first :user :email))

(defn- name->image [name]
  (string/replace name #" " "_"))

(defn transform [streams]
  (let [json-responses (map (comp json/read-json io/reader) streams)
        [primary-json backup-json] json-responses
        primary-name (json->name primary-json)
        backup-name (json->name backup-json)
        primary-email (json->email primary-json)
        backup-email (json->email backup-json)]
    {:primary_name primary-name
     :primary_email primary-email
     :primary_gravatar (email->gravatar primary-email)
     :backup_name backup-name
     :backup_email backup-email
     :backup_gravatar (email->gravatar backup-email)}))

(defn- request [username password url]
  {:method "GET" 
   :url-callback url
   :basic-auth [username password]})

(defn requests [organization username password schedule-ids]
  (let [urls (map (partial url organization) (string/split schedule-ids #","))]
    (map (partial request username password) urls)))
