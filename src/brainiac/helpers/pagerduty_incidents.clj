(ns brainiac.helpers.pagerduty-incidents
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]))

(defn url [organization service_ids]
  (let [status "triggered,acknowledged"
        base-url (format "https://%s.pagerduty.com/api/v1/incidents" organization)]
    (format "%s?status=%s&service=%s&sort_by=created_on:desc" base-url status service_ids)))

(defn transform [stream]
  (let [json (read-json (reader stream))
        incidents (:incidents json)]
    {:incidents-count (count incidents)
     :incidents incidents}))

(defn request [organization username password service-ids]
  {:method :get :url (url organization service-ids) :basic-auth [username password]})
