(ns brainiac.plugins.pagerduty-last-week
  (:import [java.util Calendar TimeZone]
           [java.text SimpleDateFormat])
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.contrib.string :only (split)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]
            [brainiac.clock :as clock]))

(def mmm-d-format (SimpleDateFormat. "MMM d"))
(def yyyy-mm-dd-format (SimpleDateFormat. "yyyy-MM-dd"))
(def utc-format (doto (SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss'Z'") (.setTimeZone (TimeZone/getTimeZone "UTC"))))
(def business-hours (set (range 9 18)))
(def sleeping-hours (set (range 0 7)))

(defn- seven-days-ago []
  (.getTime (doto (Calendar/getInstance)
              (.setTime (clock/today))
              (.add Calendar/DAY_OF_MONTH -7))))

(defn- date-part [date]
  (let [calendar (doto (Calendar/getInstance) (.setTime date))]
    [(.get calendar Calendar/YEAR) (.get calendar Calendar/MONTH) (.get calendar Calendar/DAY_OF_MONTH)]))

(defn- hour-of-day-part [date]
  (.get (doto (Calendar/getInstance) (.setTime date)) Calendar/HOUR_OF_DAY))

(defn- outside-business-hours? [date]
  (not (contains? business-hours (hour-of-day-part date))))

(defn- outside-sleeping-hours? [date]
  (not (contains? sleeping-hours (hour-of-day-part date))))

(defn url [organization service-ids]
  (let [date (.format yyyy-mm-dd-format (seven-days-ago))]
    (format "https://%s.pagerduty.com/api/v1/incidents?service=%s&since=%s&fields=created_on&sort_by=created_on:asc" organization service-ids date)))

(defn- parse-incidents [json-incidents]
  (partition-by date-part (map (fn [incident] (.parse utc-format (:created_on incident))) json-incidents)))

(defn classify-calls [call-hours]
  (if (not-any? #(contains? sleeping-hours %) call-hours)
    (if (some #(not (contains? business-hours %)) call-hours)
      "outside-business-hours"
      "none")
    "wake-up"))

(defn- build-data [partitioned-incidents]
  {:count (count partitioned-incidents)
   :impact (classify-calls (map hour-of-day-part partitioned-incidents))
   :date (.format mmm-d-format (first partitioned-incidents))})

(defn transform [stream]
  (let [json (read-json (reader stream))
        parsed-incidents (parse-incidents (:incidents json))]
    (assoc {}
      :name "pagerduty-last-week"
      :type "week-calendar"
      :data (doall (map build-data parsed-incidents)))))

(defn html []
  [:script#week-calendar-template {:type "text/mustache" :data-class "band"}
   "<table class=\"calendar\">
     <tbody>
       <tr>
         {{#data}}
           <td class=\"{{impact}}\"><span class=\"date\">{{date}}</span><span class=\"count\">{{count}}</span></td>
         {{/data}}
       </tr>
     </tbody>
    </table>"])

(defn configure [{:keys [program-name username password organization service-ids]}]
  (brainiac/schedule
    20000
    (brainiac/simple-http-plugin
      {:method :get :url (url organization service-ids) :basic-auth [username password]}
      transform program-name)))
