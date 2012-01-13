(ns brainiac.core
  (:require [brainiac.plugin-loader :as plugin-loader]
            [brainiac.websocket :as websocket]
            [aleph.http :as aleph]
            [noir.server :as server]))

(server/load-views "src/brainiac/pages")
(def handler (server/gen-handler))

(defn -main [& args]
  (websocket/setup-sink)
  (plugin-loader/register-plugins "config.yml")
  (aleph/start-http-server (aleph/wrap-ring-handler handler) {:port 8080 :websocket true}))
