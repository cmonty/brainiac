(ns brainiac.plugins.github-commits
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]
            [brainiac.pages.templates :as templates]))

(defn format-commit [commit]
  (format "%s - %s" (:login (:author commit)) (:message (:commit commit))))

(defn transform [username repository stream]
  (let [json (read-json (reader stream))]
  (assoc {}
    :name "github-commits"
    :type "github-commits"
    :title (format "Recent commits to %s/%s" username repository)
    :data (map format-commit (take 5 json)))))

(defn url [username repository]
  (format "https://api.github.com/repos/%s/%s/commits" username repository))

(defn html [] 
  [:script#github-commits-template {:type "text/mustache"}
    "<div id='octobox'>
      <h2>{{title}}</h2>
      <ul> {{#data}}<li>{{.}}</li>{{/data}} </ul>
      </div>"])

(defn configure [{:keys [username repository program-name]}]
  (brainiac/simple-http-plugin
    {:url (url username repository)}
    (fn [stream] (transform username repository stream)) program-name))
