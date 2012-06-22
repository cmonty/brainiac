(ns brainiac.websocket
  (:use [clojure.contrib.json :only (read-json)])
  (:require [lamina.core :as lamina]))

(def recent-updates (atom {}))
(def broadcast-channel (lamina/channel))

(defn store-recent-update! [json-message]
  (let [message (read-json json-message)]
    (swap! recent-updates assoc (:name message) json-message))
  json-message)

(defn broadcast-json [json-message program-name]
  (store-recent-update! json-message)
  (lamina/enqueue broadcast-channel json-message))

(defn subscribe-to-updates [ch]
  (lamina/receive ch
    (fn [program]
      (lamina/siphon (lamina/fork broadcast-channel) ch)
      (lamina/ground broadcast-channel)
      (doseq [m (vals @recent-updates)] (broadcast-json m program)))))
