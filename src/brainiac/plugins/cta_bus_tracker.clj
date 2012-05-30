(ns brainiac.plugins.cta-bus-tracker
  (:require [brainiac.plugin :as brainiac]
            [brainiac.pages.templates :as templates]
            [brainiac.xml-utils :as xml]
            [clojure.contrib.zip-filter.xml :as zf]))

(defn parse-prediction [node]
  (let [prediction (zf/xml1-> node :pt zf/text)
        destination (zf/xml1-> node :fd zf/text)]
    (assoc {}
           :destination destination
           :arrival-time prediction)))

(defn transform [stream]
  (let [xml-zipper (xml/parse-xml stream)
        stop (zf/xml1-> xml-zipper :nm zf/text)
        route (zf/xml1-> xml-zipper :sri :rt zf/text)
        direction (zf/xml1-> xml-zipper :sri :dd zf/text)]
    (assoc {}
      :name "cta-bus-tracker"
      :type "cta-bus-tracker"
      :route route
      :direction direction
      :stop stop
      :data (take 7 (zf/xml-> xml-zipper :pre parse-prediction)))))

(defn url [route-number stop-id]
  (format "http://ctabustracker.com/bustime/map/getStopPredictions.jsp?route=%s&stop=%s" route-number stop-id))

(defn html []
  [:script#cta-bus-tracker-template {:type "text/mustache"}
   "<h3>CTA #{{route}} Bus at {{stop}} ({{direction}})</h3><ul>{{#data}}<li>{{destination}}<span class=\"time\">{{arrival-time}}</span></li>{{/data}}</ul>"])


(defn configure [{:keys [route-number stop-id program-name]}]
  (brainiac/schedule
    20000
    (brainiac/simple-http-plugin
      {:method :get :url (url route-number stop-id)}
      transform program-name)))

