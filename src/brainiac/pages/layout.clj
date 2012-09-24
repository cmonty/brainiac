(ns brainiac.pages.layout
  (:require [brainiac.plugin :as plugin])
  (:use [noir.core :only (defpartial)]
        hiccup.core
        hiccup.form-helpers
        hiccup.page-helpers
        brainiac.loader))

(defn build-panel [plugins]
  [:div {:class "panel"}
    (map #(plugin/render % %2) plugins (cycle ["front" "back"]))])

(def panel-count 6)

(defpartial plugins []
  (map build-panel
    (map #(remove nil? %) (apply map vector (partition panel-count panel-count (repeat nil) @loaded)))))

(defpartial main-layout [& content]
  (html5
    [:head
     [:title "Brainiac"]
     (include-js "https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js")
     (include-js "/js/vendor/jquery.mustache.js")
     (include-js "/js/vendor/jquery.vticker.js")
     (include-js "/js/raphael-min.js")
     (include-js "/js/g.raphael-min.js")
     (include-js "/js/g.line-min.js")
     (include-js "/js/widgets.js")
     (include-js "/js/updater.js")
     (include-js "/js/clock.js")
     (include-js "/js/jenkins.js")
     (include-js "/js/jukebox.js")
     (include-js "/js/dark-sky.js")
     (include-js "/js/graphite.js")
     (include-js "/js/debug.js")
     (include-js "/js/flip.js")
     (include-css "/css/main.css")]
    [:body.nocursor
     content
     [:div.templates (map plugin/render-template @loaded)]]))

