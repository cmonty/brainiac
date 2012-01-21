(ns brainiac.plugins.cta-bus-tracker
  (:require [brainiac.plugin :as brainiac]
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

(defn tracker-url [route-number stop-id]
  (format "http://ctabustracker.com/bustime/map/getStopPredictions.jsp?route=%s&stop=%s" route-number stop-id))

(defn configure [{:keys [route-number stop-id]}]
  (brainiac/schedule
    20000
    (brainiac/simple-http-plugin
      {:method :get :url (tracker-url route-number stop-id)}
      transform)))

