(ns brainiac.html-utils
  (:import [org.htmlcleaner HtmlCleaner]
           [org.apache.commons.lang StringEscapeUtils]))

(defn parse-html [stream]
  (let [html (HtmlCleaner.)]
    (doto (.getProperties html)
      (.setOmitComments true)
      (.setPruneTags "script,style"))
    (.clean html stream)))

(defn text-at [html xpath]
  (let [node (first (seq (.evaluateXPath html xpath)))]
    (when-not (nil? node)
      (-> node
        (.getText)
        str
        (StringEscapeUtils/unescapeHtml)))))

(defn nodes-at [html xpath]
  (seq (.evaluateXPath html xpath)))

(defn children [html]
  (.getChildren html))
