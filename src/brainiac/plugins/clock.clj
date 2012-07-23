(ns brainiac.plugins.clock
  (:require [brainiac.plugin :as brainiac]))

(defn now []
  (System/currentTimeMillis))

(defn timezone-from-name [zone-name]
  (java.util.TimeZone/getTimeZone zone-name))

(defn utc-offset [timezone timestamp]
  (let [tz (timezone-from-name timezone)]
    (.getOffset tz timestamp)))

(defn offset-entry [relative-to {:keys [timezone name]}]
  { :name name :offset (utc-offset timezone relative-to)})

(defn build-message [timezones]
  (let [now-utc (now)
        tz-offsets (map (partial offset-entry now-utc) timezones)]
    { :name "clock" :time_utc now-utc :timezones tz-offsets }))

(defn update-clock [program-name timezones]
  (brainiac/send-message (build-message timezones) program-name))

(defn configure [{:keys [program-name timezones]}]
  (fn [] (update-clock program-name timezones)))
