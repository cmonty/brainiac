(ns brainiac.plugin
  (:use [clojure.contrib.http.agent :only (success? status http-agent stream)]
        [clojure.contrib.string :only (replace-str)]
        [clojure.tools.logging :only (info warn)])
  (:require [brainiac.websocket :as websocket]
            [aleph.formats :as formats]
            [lamina.core :as lamina]
            [clojure.contrib.str-utils :as s]
            [clojure.contrib.base64 :as base64]
            [clojure.contrib.json :as json]
            [overtone.at-at :as at-at]))

(def *debug* false)
(def *templates-path* (. (java.io.File. "." "src/brainiac/templates") getCanonicalPath))
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

(defn build-url [request]
  (let [url-callback (:url-callback request)]
    (if (nil? url-callback) (:url request) (url-callback))))

(defn transform [agnt transformer]
  (-> (stream agnt)
    tap
    transformer
    tap))

(defn send-message [message program]
  (let [json-message (json/json-str message)]
    (websocket/broadcast-json json-message program)))

(defn receive-message [program plugin content]
  (let [namespace (fullname plugin)]
    (send-message (eval (list (symbol namespace "message") {:message content})) program)))

(defn render [plugin side]
  (let [id (shortname plugin)]
    [:div {:id id :class side} ""]))

(defn render-template [plugin]
  (let [plugin-name (shortname plugin)
        filename (str *templates-path* "/" plugin-name ".html")]
    [:script {:type "text/mustache", :id (str plugin-name "-template")}
      (slurp filename)]))

(defn agent-handler [transformer program-name]
  (fn [agnt]
    (send-message (transform agnt transformer) program-name)))

(defn- http-agent-for-request [request]
  (let [url (build-url request)
        headers (merge (:headers request) (build-basic-auth request))]
    (info "fetching" url)
    (http-agent url :headers headers)))

(defn simple-http-plugin [request transformer program-name]
  (fn []
    (try
      (let [url (build-url request)
            agnt (http-agent-for-request request)]
        (await-for 60000 agnt)
        (if (success? agnt)
          (send-message (transform agnt transformer) program-name)
          (warn "URL " url "failed with" (status agnt))))
      (catch Exception e (warn "caught exception " (.getMessage e))))))

(defn multiple-url-http-plugin [requests transformer program-name]
  (fn []
    (try
      (let [agents (map http-agent-for-request requests)]
        (doseq [agnt agents]
          (await-for 60000 agnt))
        (if (every? success? agents)
          (send-message (transformer (map stream agents)) program-name)
          (warn "Some URLs failed")))
      (catch Exception e (warn "caught exception " (.getMessage e))))))

(defn schedule [interval method]
  (at-at/every interval method))
