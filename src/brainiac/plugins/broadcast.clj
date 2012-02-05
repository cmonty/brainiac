(ns brainiac.plugins.broadcast
  (:require [brainiac.plugin :as brainiac]
            [brainiac.pages.templates :as templates]
            [brainiac.html-utils :as html]))

(def clear (atom 30000))

(defn html [] (templates/simple))

(defn message [data]
  (assoc {}
     :name "broadcast"
     :type "simple"
     :title "Broadcasted"
     :data data))

(defn configure [{:keys [clear-interval program-name]}]
  (swap! clear * clear-interval 1000))

