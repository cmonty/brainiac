(ns brainiac.plugin
  (:use [clojure.contrib.http.agent :only (success? status http-agent stream)]
        [clojure.contrib.string :only (replace-str)]
        clj-logging-config.log4j)
  (:require [brainiac.websocket :as websocket]
            [aleph.formats :as formats]
            [lamina.core :as lamina]
            [clojure.contrib.str-utils :as s]
            [clojure.contrib.base64 :as base64]
            [clojure.tools.logging :as log :only (info error)]
            [overtone.at-at :as at-at]))

(set-logger! :pattern "[%d] %m - %r%n")

(def *debug* false)
(defn tap [s] (if *debug* (prn s)) s)

(defn fullname [plugin]
  (str "brainiac.plugins." (name plugin)))

(defn shortname [plugin]
  (replace-str "brainiac.plugins." "" plugin))

(defn build-basic-auth [request]
  (if (:basic-auth request)
    (assoc {}
      "Authorization"
      (str "Basic "(base64/encode-str (s/str-join ":" (:basic-auth request)))))
    {}))

(defn munge-request [request]
  (let [url-callback (:url-callback request)
        url (if (nil? url-callback) (:url request) (url-callback))
        headers (merge {} (build-basic-auth request))]
    [url :headers headers]))

(defn build-url [request]
  (let [url-callback (:url-callback request)]
    (if (nil? url-callback) (:url request) (url-callback))))

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
  (let [id (shortname (key plugin))]
    [:div {:id id} ""]))

(defn render-template [plugin]
  (let [namespace (name (key plugin))]
    (require (symbol namespace))
    (eval (list (symbol namespace "html")))))

(defn agent-handler [transformer program-name]
  (fn [agnt]
    (send-message (transform agnt transformer) program-name)))

(defn simple-http-plugin [request transformer program-name]
  (fn []
    (try
      (let [url (build-url request)
            agnt (http-agent url :headers (build-basic-auth request))]
        (log/info "fetching" url)
        (await-for 60000 agnt)
        (if (success? agnt)
          (send-message (transform agnt transformer) program-name)
          (println "URL " url "failed with" (status agnt))))
      (catch Exception e (println "caught exception " (.getMessage e))))))

(defn schedule [interval method]
  (at-at/every interval method))
