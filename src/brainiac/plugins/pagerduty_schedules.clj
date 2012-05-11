(ns brainiac.plugins.pagerduty-schedules
  (:import [java.util Calendar TimeZone]
           [java.text SimpleDateFormat])
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]
						[clojure.string :only replace]))

(defn now []
  (.getTime (Calendar/getInstance (TimeZone/getTimeZone "America/Chicago"))))

(defn date-formatter [date]
  (let [date-formatter (SimpleDateFormat. "yyyy-MM-dd'T'HH:mmZ")]
    (.format date-formatter date)))

(defn url [organization schedule]
  (fn []
    (let [current-time (date-formatter (now))]
      (format "https://%s.pagerduty.com/api/v1/schedules/%s/entries?since=%s&until=%s&overflow=true" organization schedule current-time current-time))))

(defn html []
  [:script#schedule-template {:type "text/mustache"}
   "<h3>On Call Now</h3><p class='on-call'><img src='https://www.braintreepayments.com/assets/team/{{ person_name }}.jpg'/>{{#data}}{{ user.name }}{{/data}}</p>"])

(defn transform [stream]
  (let [json (read-json (reader stream))
				person_name (clojure.string/replace (:name (:user (first (:entries json)))) #" ""_")]
    (assoc {}
      :name "pagerduty-schedules"
      :type "schedule"
			:person_name person_name
      :data (:entries json))))

(defn configure [{:keys [program-name organization username password schedule_ids]}]
  (brainiac/schedule
    5000
    (brainiac/simple-http-plugin
      {:method :get :url-callback (url organization schedule_ids) :basic-auth [username password]}
      transform program-name)))
