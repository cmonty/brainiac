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

(defn fullname [plugin]
  (str "brainiac.plugins." (name plugin)))

(defn build-basic-auth [request]
  (if (:basic-auth request)
    (assoc {}
      "Authorization"
      (str "Basic "(base64/encode-str (s/str-join ":" (:basic-auth request)))))
    {}))

(defn munge-request [request handler]
  (let [url-callback (:url-callback request)
        url (if (nil? url-callback) (:url request) (url-callback))
        headers (merge {} (build-basic-auth request))]
    (http-agent url :headers headers :handler handler)))

(defn transform [agnt transformer]
  (-> (stream agnt)
    tap
    transformer
    tap))

(defn send-message [message program]
  (let [json-message (formats/encode-json->string message)]
    (websocket/broadcast-json json-message program)))

(defn receive-message [program plugin content]
  (let [namespace (fullname plugin)]
    (send-message (eval (list (symbol namespace "message") {:message content})) program)))

(defn render [plugin]
  (let [namespace (name (key plugin))]
    (require (symbol namespace))
    (eval (list (symbol namespace "html")))))

(defn agent-handler [transformer program-name]
  (fn [agnt]
    (send-message (transform agnt transformer) program-name)))

(defn simple-http-plugin [request transformer program-name]
  (fn [] (munge-request request (agent-handler transformer program-name))))

(defn schedule [interval method]
  (at-at/every interval method))
