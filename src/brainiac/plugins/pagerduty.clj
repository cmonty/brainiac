(ns brainiac.plugins.pagerduty
  (:import [java.util Calendar TimeZone]
           [java.text SimpleDateFormat])
  (:require [brainiac.plugin :as brainiac]
            [brainiac.helpers.pagerduty-incidents :as pagerduty-incidents]
            [brainiac.helpers.pagerduty-last-week :as pagerduty-last-week]
            [brainiac.helpers.pagerduty-schedules :as pagerduty-schedules]
						[clojure.string :as string]
            [clojure.contrib.json :as json]
            [clojure.java.io :as io]))

(defn transform [streams]
  (let [[last-week-stream incidents-stream & schedules-streams] streams]
    (merge (pagerduty-last-week/transform last-week-stream)
           (pagerduty-incidents/transform incidents-stream)
           (pagerduty-schedules/transform schedules-streams)
           {:name "pagerduty"})))

(defn configure [{:keys [program-name organization username password schedule-ids service-ids]}]
  (let [last-week-request (pagerduty-last-week/request organization username password service-ids)
        incidents-request (pagerduty-incidents/request organization username password service-ids)
        schedule-requests (pagerduty-schedules/requests organization username password schedule-ids)
        all-requests (flatten [last-week-request incidents-request schedule-requests])]
    (brainiac/multiple-url-http-plugin all-requests transform program-name)))
