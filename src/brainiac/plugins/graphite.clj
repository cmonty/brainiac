(ns brainiac.plugins.graphite
  (:require [brainiac.plugin :as brainiac]
            [clojure.contrib.json :as json :only (read-json)]
            [clojure.java.io :as io :only (reader)]
            [clojure.contrib.string :as string]))

(def title (atom ""))

(defn- remove-nil-tuples [datapoints]
  (remove #(nil? (first %)) datapoints))

(defn- split-values [payload]
  (apply map vector (remove-nil-tuples (:datapoints (first payload)))))

(defn format-data [payload]
  {:valuesx (second (split-values payload)) :valuesy (first (split-values payload))})

(defn transform [stream]
  (let [json (json/read-json (io/reader stream))]
    {:name "graphite"
    :title @title
    :data (format-data json)}))

(defn url [graphite-url target]
  (format "%s/render?format=json&from=-24hours&target=summarize(%s,\"1h\")" graphite-url target))

(defn configure [{:keys [target custom-title graphite-url username password program-name]}]
  (swap! title str custom-title)
  (brainiac/simple-http-plugin
    {:method :get :url (url graphite-url target) :basic-auth [username password]}
    transform program-name))
