
(ns brainiac.plugins.kudotron
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)]
        [clojure.string :only (join split)])
  (:require [brainiac.plugin :as brainiac])
)


(defn- escape-html
 "Like clojure.core/str but escapes < > and &."
 [x]
  (-> x str (.replace "&" " and ") (.replace "<" "&lt;") (.replace ">" "&gt;")))

(defn interpret-message [msgJson] {
  :to (:to msgJson)
  :from (:from msgJson)
  :body (:body msgJson)
  :timestamp (:created_at msgJson)
})

(defn transform [stream]
  (let [json (read-json (reader stream))]
  (assoc {}
    :name "kudotron"
    :messages (map interpret-message (take 4 json)))))

(defn configure [{:keys [url]}]
  (brainiac/simple-http-plugin {:url url} transform "burp")
)
