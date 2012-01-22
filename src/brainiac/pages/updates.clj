(ns brainiac.pages.updates
  (:require [brainiac.websocket :as websocket]
            [noir.core :as noir]
            [aleph.http :as aleph]))

(defn update-handler [ch handshake]
  (websocket/subscribe-to-updates ch))

(noir/custom-handler "/async" [] (aleph/wrap-aleph-handler update-handler))
