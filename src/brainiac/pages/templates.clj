(ns brainiac.pages.templates
  (:use [noir.core :only (defpartial)]
        hiccup.core
        hiccup.form-helpers))

(defpartial content []
  [:script#content-template {:type "text/mustache"}
   "<h3>{{title}}</h3> {{#data}}<h5>{{title}}<article>{{&content}}</article>{{/data}}"])

(defpartial simple []
  [:script#simple-template {:type "text/mustache"}
   "<h3>{{title}}</h3> <article>{{ data.message }}</article>"])

(defpartial unordered-list []
  [:script#list-template {:type "text/mustache"}
   "<h3>{{title}}</h3> <ul> {{#data}}<li>{{.}}</li>{{/data}} </ul>"])

