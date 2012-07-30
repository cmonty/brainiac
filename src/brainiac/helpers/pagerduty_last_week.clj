(ns brainiac.helpers.pagerduty-last-week
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

(defn- days-ago [number]
  (.getTime (doto (Calendar/getInstance)
              (.setTime (clock/today))
              (.add Calendar/DAY_OF_MONTH number))))

(defn- date-part [date]
  (let [calendar (doto (Calendar/getInstance) (.setTime date))]
    [(.get calendar Calendar/YEAR) (.get calendar Calendar/MONTH) (.get calendar Calendar/DAY_OF_MONTH)]))

(defn- hour-of-day-part [date]
  (.get (doto (Calendar/getInstance) (.setTime date) (.setTimeZone (TimeZone/getTimeZone "America/Chicago"))) Calendar/HOUR_OF_DAY))

(defn- outside-business-hours? [date]
  (not (contains? business-hours (hour-of-day-part date))))

(defn- outside-sleeping-hours? [date]
  (not (contains? sleeping-hours (hour-of-day-part date))))

(defn url [organization service-ids]
  (fn []
    (let [date (.format yyyy-mm-dd-format (days-ago -7))]
      (format "https://%s.pagerduty.com/api/v1/incidents?service=%s&since=%s&fields=created_on&sort_by=created_on:asc" organization service-ids date))))

(defn- parse-incidents [json-incidents]
  (partition-by date-part (map (fn [incident] (.parse utc-format (:created_on incident))) json-incidents)))

(defn classify-calls [call-hours]
  (if (not-any? #(contains? sleeping-hours %) call-hours)
    (if (some #(not (contains? business-hours %)) call-hours)
      "outside-business-hours"
      "none")
    "wake-up"))

(defn- default-data []
  (map days-ago (vec (range -6 1))))

(defn- same-day? [date grouped_dates]
  (= (.format mmm-d-format date) (.format mmm-d-format (first grouped_dates))))

(defn- incidents-for [date incidents]
  (flatten (filter (partial same-day? date) incidents)))

(defn- fill-data [partitioned-incidents default]
  (let [incidents (incidents-for default partitioned-incidents)]
    (if (empty? incidents)
      {:count 0
       :impact "none"
       :date (.format mmm-d-format default)}
      {:count (count incidents)
       :impact (classify-calls (map hour-of-day-part incidents))
       :date (.format mmm-d-format (first incidents))})))

(defn- build-data [partitioned-incidents]
  (let [init-week (default-data)]
    (map (partial fill-data partitioned-incidents) init-week)))

(defn transform [stream]
  (let [json (read-json (reader stream))
        parsed-incidents (parse-incidents (:incidents json))]
    {:data (build-data parsed-incidents)}))

(defn request [organization username password service-ids]
  {:method :get :url-callback (url organization service-ids) :basic-auth [username password]})
