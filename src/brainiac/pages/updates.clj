(ns brainiac.pages.updates
  (:use [brainiac.websocket :only (websocket-channel)]
        [lamina.core :only (receive siphon map*)])
  (:require [noir.core :as noir]
            [aleph.http :as aleph]))

(defn chat-handler [ch handshake]
  (receive ch
    (fn [name]
      (siphon websocket-channel ch))))

(noir/custom-handler "/async" [] (aleph/wrap-aleph-handler chat-handler))
