(ns brainiac.websocket
  (:use [clojure.contrib.json :only (read-json)])
  (:require [lamina.core :as lamina]))

(def recent-updates (atom {}))

(defn store-recent-update! [json-message]
  (let [message (read-json json-message)]
    (swap! recent-updates assoc (:name message) json-message))
  json-message)

(defn websocket-for-program [program-name]
  (lamina/named-channel program-name))

(defn broadcast-json [json-message program-name]
  (store-recent-update! json-message)
  (lamina/enqueue (websocket-for-program program-name) json-message))

(defn subscribe-to-updates [channel]
  (lamina/receive channel
    (fn [program]
      (lamina/siphon (websocket-for-program program) channel)
      (doseq [m (vals @recent-updates)] (broadcast-json m program)))))
