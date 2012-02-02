(ns brainiac.plugins.cta-bus-tracker
  (:require [brainiac.plugin :as brainiac]
            [brainiac.pages.templates :as templates]
            [brainiac.xml-utils :as xml]
            [clojure.contrib.zip-filter.xml :as zf]))

(defn parse-prediction [node]
  (let [prediction (zf/xml1-> node :pt zf/text)
        direction (zf/xml1-> node :fd zf/text)]
    (str direction " - " prediction)))

(defn transform [stream]
  (let [xml-zipper (xml/parse-xml stream)
        stop (zf/xml1-> xml-zipper :nm zf/text)
        route (zf/xml1-> xml-zipper :sri :rt zf/text)
        direction (zf/xml1-> xml-zipper :sri :dd zf/text)]
    (assoc {}
      :name "cta-bus-tracker"
      :type "list"
      :title (format "CTA #%s Bus (%s) - %s" route direction stop)
      :data (zf/xml-> xml-zipper :pre parse-prediction))))

(defn url [route-number stop-id]
  (format "http://ctabustracker.com/bustime/map/getStopPredictions.jsp?route=%s&stop=%s" route-number stop-id))

(defn html [] (templates/unordered-list))

(defn configure [{:keys [route-number stop-id program-name]}]
  (brainiac/schedule
    20000
    (brainiac/simple-http-plugin
      {:method :get :url (url route-number stop-id)}
      transform program-name)))

