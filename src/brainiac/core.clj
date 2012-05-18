(ns brainiac.core
  (:gen-class)
  (:require [clojure.contrib.command-line :as cli]
            [brainiac.loader :as loader]
            [aleph.http :as aleph]
            [noir.server :as server]))

(def handler (server/gen-handler))

(defn -main [& args]
  (cli/with-command-line args
    "Brainiac: An Awesome HUD"
    [[port p "the port to listen on" "8080"]
     [file f "the config file" "config.yml"]]
    (loader/load-programs file)
    (server/load-views "src/brainiac/pages")
    (aleph/start-http-server (aleph/wrap-ring-handler handler) {:port (read-string port) :websocket true})))
