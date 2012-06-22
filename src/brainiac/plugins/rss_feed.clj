(ns brainiac.plugins.rss-feed
  (:require [brainiac.plugin :as brainiac]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.contrib.zip-filter.xml :as zf]))

(defn parse-item [item]
  (assoc {}
    :title (zf/xml1-> item :title zf/text)
    :content (zf/xml1-> item :description zf/text)))

(defn transform [stream]
  (let [xml (zip/xml-zip (xml/parse stream))]
    (assoc {}
      :name "rss-feed"
      :title (zf/xml1-> xml :channel :title zf/text)
      :data (zf/xml-> xml :channel :item parse-item))))

(defn configure [{:keys [url program-name]}]
  (brainiac/simple-http-plugin
    {:url url}
    transform program-name))
