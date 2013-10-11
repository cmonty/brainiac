(ns brainiac.plugins.wifi-access-point
  (:require [brainiac.plugin :as brainiac]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.contrib.zip-filter.xml :as zf])
  (:use [clojure.java.io :only (reader)]))

(defn transform [network stream]
  (let [passphrase (apply str (line-seq (reader stream)))]
    { :name "wifi-access-point"
      :network network
      :passphrase passphrase }))

(defn configure [{:keys [url network program-name]}]
  (brainiac/simple-http-plugin
    {:method "GET"
     :url url}
    (partial transform network) program-name))

