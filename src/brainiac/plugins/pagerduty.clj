(ns brainiac.plugins.pagerduty
  (:import [java.util Calendar TimeZone]
           [java.text SimpleDateFormat])
  (:require [brainiac.plugin :as brainiac]
            [brainiac.helpers.pagerduty-schedules :as pagerduty-schedules]
            [brainiac.helpers.pagerduty-last-week :as pagerduty-last-week]
						[clojure.string :as string]
            [clojure.contrib.json :as json]
            [clojure.java.io :as io]))

(defn html []
  [:script#pagerduty-template {:type "text/mustache"}
   "<table class=\"calendar\">
     <tbody>
       <tr>
         {{#data}}
           <td class=\"{{impact}}\"><span class=\"date\">{{date}}</span><span class=\"count\">{{count}}</span></td>
         {{/data}}
       </tr>
     </tbody>
   </table>
   <h3 style='margin-top: 20px'>On Call Now</h3>
   <p class='on-call'>
     <img src='https://www.braintreepayments.com/assets/team/{{ primary_name_image }}.jpg'/>
     {{ primary_name }}
   </p>
   <div style='clear:both'/>
   <p class='on-call'>
     <img src='https://www.braintreepayments.com/assets/team/{{ backup_name_image }}.jpg'/>
     {{ backup_name }}
   </p>"])

(defn transform [streams]
  (let [last-week-stream (first streams)
        schedules-streams (rest streams)]
    (merge (pagerduty-last-week/transform last-week-stream)
           (pagerduty-schedules/transform schedules-streams)
           {:name "pagerduty" :type "pagerduty"})))

(defn configure [{:keys [program-name organization username password schedule-ids service-ids]}]
  (let [last-week-request (pagerduty-last-week/request organization username password service-ids)
        schedule-requests (pagerduty-schedules/requests organization username password schedule-ids)]
    (brainiac/multiple-url-http-plugin (cons last-week-request schedule-requests) transform program-name)))
