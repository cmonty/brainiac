(ns brainiac.plugins.pagerduty-schedules
  (:import [java.util Calendar TimeZone]
           [java.text SimpleDateFormat])
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]
						[clojure.string :only [replace, split]]))

(defn now []
  (.getTime (Calendar/getInstance)))

(defn date-formatter [date]
  (let [date-formatter (SimpleDateFormat. "yyyy-MM-dd'T'HH:mmZ")]
    (.setTimeZone date-formatter (TimeZone/getTimeZone "America/Chicago"))
    (.format date-formatter date)))

(defn url [organization schedule]
  (fn []
    (let [current-time (date-formatter (now))]
      (format "https://%s.pagerduty.com/api/v1/schedules/%s/entries?since=%s&until=%s&overflow=true" organization schedule current-time current-time))))

(defn html []
  [:script#schedule-template {:type "text/mustache"}
   "<h3>On Call Now</h3>
   <p class='on-call'>
     <img src='https://www.braintreepayments.com/assets/team/{{ primary_name_image }}.jpg'/>
     {{ primary_name }}
   </p>
   <div style='clear:both'/>
   <p class='on-call'>
     <img src='https://www.braintreepayments.com/assets/team/{{ backup_name_image }}.jpg'/>
     {{ backup_name }}
   </p>"])

; {:entries [{:end 2012-06-02T12:00:00-05:00, :start 2012-05-28T10:00:00-05:00, :user {:name Ben Mills, :id PR3XPTK, :color red, :email ben.mills@getbraintree.com}}], :total 1}
(defn- name-from-json [json]
  (-> json :entries first :user :name))

(defn- image-from-name [name]
  (clojure.string/replace name #" " "_"))

(defn transform [streams]
  (let [json-responses (map #(read-json (reader %)) streams)
        [primary-json backup-json] json-responses
        primary-name (name-from-json primary-json)
        backup-name (name-from-json backup-json)]
    {:name "pagerduty-schedules"
     :type "schedule"
     :primary_name primary-name
     :primary_name_image (image-from-name primary-name)
     :backup_name backup-name
     :backup_name_image (image-from-name backup-name)}))

(defn configure [{:keys [program-name organization username password schedule_ids]}]
  (let [urls (map #(url organization %) (clojure.string/split schedule_ids #","))
        requests (map #(hash-map :method :get :url-callback % :basic-auth [username password]) urls)]
  (brainiac/multiple-url-http-plugin requests transform program-name)))
