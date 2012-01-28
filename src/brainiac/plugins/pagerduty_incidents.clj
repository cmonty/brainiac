(ns brainiac.plugins.pagerduty-incidents
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]))

(defn incidents-url [service_ids]
  (let [status "triggered,acknowledged"
        base-url "https://braintree.pagerduty.com/api/v1/incidents"]
    (format "%s?status=%s&service=%s&sort_by=created_on:desc" base-url status service_ids)))

(defn transform [stream]
  (let [json (read-json (reader stream))]
    (assoc {}
      :name "pagerduty-incident"
      :type "alert"
      :data (:incidents json))))

(defn configure [{:keys [program-name username password service_ids schedule]}]
  (brainiac/schedule
    2000
    (brainiac/simple-http-plugin
      {:method :get :url (incidents-url service_ids) :basic-auth [username password]}
      transform program-name)))
