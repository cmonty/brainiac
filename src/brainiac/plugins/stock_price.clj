(ns brainiac.plugins.stock-price
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]
            [clojure.java.io :only (reader)]))

(defn parse-float [str]
  (try
    (Float/parseFloat str)
    (catch NumberFormatException e 0)))

(defn transform [stream]
  (let [json (read-json (reader stream))
        q (get-in json [:query :results :quote])
        price (parse-float (:LastTradePriceOnly q))
        change (parse-float (:Change q)) ]
    {:name "stock-price"
     :title (:Name q)
     :price (format "%.2f" price)
     :change (format "%.2f" change)
     :percent (format "%.2f%%" (* (/ change (- price change)) 100))
     :trend (if (neg? change) "down" "up")}))

(defn url [ticker]
  (format "https://query.yahooapis.com/v1/public/yql?q=select%%20*%%20from%%20yahoo.finance.quote%%20where%%20symbol%%3D%%22%s%%22&format=json&env=store%%3A%%2F%%2Fdatatables.org%%2Falltableswithkeys&callback=" ticker))

(defn configure [{:keys [ticker program-name]}]
  (brainiac/simple-http-plugin
    {:url (url ticker)}
    transform program-name))
