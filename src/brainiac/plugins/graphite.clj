(ns brainiac.plugins.graphite
  (:require [brainiac.plugin :as brainiac]
            [clojure.contrib.json :as json :only (read-json)]
            [clojure.java.io :as io :only (reader)]
            [clojure.contrib.string :as string]))

(defn- split-values [payload]
  (apply map vector (:datapoints (first payload))))

(defn format-data [payload]
  {:valuesx (second (split-values payload)) :valuesy (first (split-values payload))})

(defn transform [stream]
  (let [json (json/read-json (io/reader stream))]
    {:name "graphite"
    :title "Graphite Graph"
    :data (format-data json)}))

(defn url [graphite-url target]
  (format "%s/render?format=json&from=-1hour&target=%s" graphite-url target))

(defn configure [{:keys [target graphite-url username password program-name]}]
  (brainiac/simple-http-plugin
    {:method :get :url (url graphite-url target) :basic-auth [username password]}
    transform program-name))
