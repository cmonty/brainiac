(ns brainiac.pages.root
  (:use [noir.core :only (defpage)]
        [hiccup.core]
        [brainiac.pages.layout :only (main-layout)]))

(defpage "/" []
  (main-layout [:div#plugins]))
