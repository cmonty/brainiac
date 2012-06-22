(ns brainiac.plugins.pagerduty
  (:import [java.util Calendar TimeZone]
           [java.text SimpleDateFormat])
  (:require [brainiac.plugin :as brainiac]
            [brainiac.helpers.pagerduty-incidents :as pagerduty-incidents]
            [brainiac.helpers.pagerduty-last-week :as pagerduty-last-week]
            [brainiac.helpers.pagerduty-schedules :as pagerduty-schedules]
						[clojure.string :as string]
            [clojure.contrib.json :as json]
            [clojure.java.io :as io]))

(defn html []
  [:script#pagerduty-template {:type "text/mustache"}
   "<img class=\"header\" src=\"http://www.pagerduty.com/images/logos/pagerduty_logo_dark.png?1337996052\" />
   <h3>Incidents: {{ incidents-count }}</h3>{{#incidents}}<p>{{trigger_summary_data.subject}}</p>{{/incidents}}
   <div class=\"people clearfix\">
     <div class='on-call'>
       <img src='https://www.braintreepayments.com/assets/team/{{ primary_name_image }}.jpg'/>
       <p class=\"name\">{{ primary_name }}</p>
     </div>
     <div class='on-call'>
       <img src='https://www.braintreepayments.com/assets/team/{{ backup_name_image }}.jpg'/>
       <p class=\"name\">{{ backup_name }}</p>
     </div>
   </div>
   <table class=\"calendar\">
     <tbody>
       <tr>
         {{#data}}
           <td class=\"{{impact}}\"><span class=\"date\">{{date}}</span><span class=\"count\">{{count}}</span></td>
         {{/data}}
       </tr>
     </tbody>
   </table>
   "])

(defn transform [streams]
  (let [[last-week-stream incidents-stream & schedules-streams] streams]
    (merge (pagerduty-last-week/transform last-week-stream)
           (pagerduty-incidents/transform incidents-stream)
           (pagerduty-schedules/transform schedules-streams)
           {:name "pagerduty" :type "pagerduty"})))

(defn configure [{:keys [program-name organization username password schedule-ids service-ids]}]
  (let [last-week-request (pagerduty-last-week/request organization username password service-ids)
        incidents-request (pagerduty-incidents/request organization username password service-ids)
        schedule-requests (pagerduty-schedules/requests organization username password schedule-ids)
        all-requests (flatten [last-week-request incidents-request schedule-requests])]
    (brainiac/multiple-url-http-plugin all-requests transform program-name)))
