(ns brainiac.core
  (:require [clojure.contrib.command-line :as cli]
            [brainiac.plugin-loader :as plugin-loader]
            [brainiac.websocket :as websocket]
            [aleph.http :as aleph]
            [noir.server :as server]))

(server/load-views "src/brainiac/pages")
(def handler (server/gen-handler))

(defn -main [& args]
  (cli/with-command-line args
    "Brainiac: An Awesome HUD"
    [[port p "the port to listen on" "8080"]
     [file f "the config file" "config.yml"]]
    (websocket/setup-sink)
    (plugin-loader/register-plugins file)
    (aleph/start-http-server (aleph/wrap-ring-handler handler) {:port (read-string port) :websocket true})))
