(ns brainiac.plugins.pagerduty-incidents
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]))

(defn url [organization service_ids]
  (let [status "triggered,acknowledged"
        base-url (format "https://%s.pagerduty.com/api/v1/incidents" organization)]
    (format "%s?status=%s&service=%s&sort_by=created_on:desc" base-url status service_ids)))

(defn transform [stream]
  (let [json (read-json (reader stream))]
    (assoc {}
      :name "pagerduty-incidents"
      :type "alert"
      :data (:incidents json))))

(defn html []
  [:script#alert-template {:type "text/mustache"}
   "<h3>Alert</h3>{{#data}}<p>{{trigger_summary_data.subject}}</p>{{/data}}"])

(defn configure [{:keys [program-name username password organization service_ids schedule]}]
  (brainiac/simple-http-plugin
    {:method :get :url (url organization service_ids) :basic-auth [username password]}
    transform program-name))
