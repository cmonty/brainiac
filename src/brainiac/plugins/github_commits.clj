(ns brainiac.plugins.github-commits
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]
            [brainiac.pages.templates :as templates]))

(defn format-commit [commit]
  (format "%s - %s - %s" (.substring (:sha commit) 0 10) (:login (:author commit)) (:message (:commit commit))))

(defn transform [username repository stream]
  (let [json (read-json (reader stream))]
  (assoc {}
    :name "github-commits"
    :type "list"
    :title (format "Recent commits to %s/%s" username repository)
    :data (map format-commit (take 5 json)))))

(defn url [username repository]
  (format "https://api.github.com/repos/%s/%s/commits" username repository))

(defn html [] (templates/unordered-list))

(defn configure [{:keys [username repository program-name]}]
  (brainiac/schedule
    15000
    (brainiac/simple-http-plugin
      {:url (url username repository)}
      (fn [stream] (transform username repository stream)) program-name)))
