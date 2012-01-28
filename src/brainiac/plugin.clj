(ns brainiac.plugin
  (:use [clojure.contrib.http.agent :only (error? http-agent stream)])
  (:require [brainiac.websocket :as websocket]
            [aleph.formats :as formats]
            [lamina.core :as lamina]
            [clojure.contrib.str-utils :as s]
            [clojure.contrib.base64 :as base64]
            [overtone.at-at :as at-at]))

(def *debug* false)
(defn tap [s] (if *debug* (prn s)) s)

(defn build-basic-auth [request]
  (if (:basic-auth request)
    (assoc {}
      "Authorization"
      (str "Basic "(base64/encode-str (s/str-join ":" (:basic-auth request)))))
    {}))

(defn munge-request [request handler]
  (let [url (:url request)
        headers (merge {} (build-basic-auth request))]
    (http-agent url :headers headers :handler handler)))

(defn jsonify [agnt transformer]
  (-> (stream agnt)
    tap
    transformer
    tap
    formats/encode-json->string))

(defn agent-handler [transformer program-name]
  (fn [agnt]
    (websocket/broadcast-json (jsonify agnt transformer) program-name)))

(defn simple-http-plugin [request transformer program-name]
  (fn [] (munge-request request (agent-handler transformer program-name))))

(defn schedule [interval method]
  (at-at/every interval method))
