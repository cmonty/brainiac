(ns brainiac.xml-utils
  (:import [java.io ByteArrayInputStream])
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]))

(defn parse-xml [stream]
  (zip/xml-zip (xml/parse stream)))
