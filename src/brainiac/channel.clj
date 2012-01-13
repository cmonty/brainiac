(ns brainiac.websocket
  (:require [lamina.core :as lamina]))

(def websocket-channel (lamina/channel))

(defn setup-sink []
  (lamina/receive-all websocket-channel (fn [_])))

(defn broadcast [message]
  (lamina/enqueue websocket-channel message))
