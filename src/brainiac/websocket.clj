(ns brainiac.websocket
  (:use [clojure.contrib.json :only (read-json)])
  (:require [lamina.core :as lamina]))

(def websocket-channel (lamina/channel))
(def recent-updates (atom {}))

(defn store-recent-update! [json-message]
  (let [message (read-json json-message)]
    (swap! recent-updates assoc (:name message) json-message))
  json-message)

(defn setup-sink []
  (lamina/receive-all websocket-channel (fn [_])))

(defn broadcast-json [json-message]
  (store-recent-update! json-message)
  (lamina/enqueue websocket-channel json-message))

(defn subscribe-to-updates [channel]
  (lamina/receive channel
    (fn [_]
      (lamina/siphon websocket-channel channel)
      (doseq [m (vals @recent-updates)] (broadcast-json m)))))
