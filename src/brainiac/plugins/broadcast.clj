(ns brainiac.plugins.broadcast
  (:require [brainiac.plugin :as brainiac]
            [brainiac.html-utils :as html]))

(def clear (atom 30000))

(defn message [data]
  (assoc {}
     :name "broadcast"
     :title "Broadcasted"
     :data data))

(defn configure [{:keys [clear-interval program-name]}]
  (swap! clear * clear-interval 1000))

