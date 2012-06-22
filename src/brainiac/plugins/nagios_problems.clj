(ns brainiac.plugins.nagios-problems
  (:use [clojure.stacktrace :only (print-stack-trace)])
  (:require [brainiac.plugin :as brainiac]
            [brainiac.html-utils :as html]))

(defn url [host]
  (format "%s/cgi-bin/nagios3/status.cgi?hostgroup=all&style=detail&servicestatustypes=28&hoststatustypes=15" host))

(defn map-row [row]
  (try
    (if (> (count (html/children row)) 1)
      (assoc {}
        :message (html/text-at row "td[position() = 7]")
        :host  (html/text-at row "td[position() = 1]//a")
        :service (html/text-at row "td[position() = 2]//a"))
      {})
  (catch Exception e (print-stack-trace e))))

(defn transform [stream]
  (let [html (html/parse-html stream)
        status-rows (html/nodes-at html "//body/p/table[@class='status']/tbody/tr")]
    (assoc {}
      :name "nagios-problems"
      :title "Nagios Problems"
      :data (remove empty? (map map-row (rest status-rows))))))

(defn configure [{:keys [host username password program-name]}]
  (binding [brainiac/*debug* true]
    (brainiac/simple-http-plugin
      {:method :get :url (url host) :basic-auth [username password]}
      transform program-name)))
