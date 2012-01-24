(ns brainiac.pages.layout
  (:use [noir.core :only (defpartial)]
        hiccup.core
        hiccup.form-helpers
        hiccup.page-helpers))

(defpartial main-layout [& content]
  (html5
    [:head
     [:title "Brainiac"]
     (include-js "https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js")
     (include-js "/js/vendor/jquery.mustache.js")
     (include-js "/js/vendor/jquery.vticker.js")
     (include-js "/js/widgets.js")
     (include-js "/js/updater.js")
     (include-css "/css/main.css")]
    [:body
     content
     [:div.templates
      [:script#weather-template {:type "text/mustache"}
       "<h3>{{title}}</h3> <h2>{{data.temp}}</h2> <p><img src={{data.icon}}><br>{{data.current-conditions}}</p>"]
      [:script#ticker-template {:type "text/mustache" :data-widget "ticker"}
       "<div class=\"ticker\"><ul class=\"ticker\"> {{#data}}<li>{{.}}</li>{{/data}} </ul></ticker>"]
      [:script#nagios-problems-template {:type "text/mustache"}
       "<h3>{{title}}</h3> <ul> {{#data}}<li>{{service}} ({{host}})</li>{{/data}} </ul>"]
      [:script#content-template {:type "text/mustache"}
       "<h3>{{title}}</h3> {{#data}}<h5>{{title}}<article>{{&content}}</article>{{/data}}"]
      [:script#schedule-template {:type "text/mustache"}
       "<h3>On Call Now</h3>{{#data}}<p>{{ user.name }}</p>{{/data}}"]
      [:script#alert-template {:type "text/mustache"}
       "<h3>Alert</h3>{{#data}}<p>{{trigger_summary_data.subject}}</p>{{/data}}"]
      [:script#list-template {:type "text/mustache"}
       "<h3>{{title}}</h3> <ul> {{#data}}<li>{{.}}</li>{{/data}} </ul>"]]]))

