(ns brainiac.helpers.md5
  (:require [clojure.contrib.io :only to-byte-array]
            [clojure.contrib.string :only blank?])
  (:import [java.security MessageDigest]))

(defn str->hash [string]
  (if (clojure.contrib.string/blank? string)
    (str "")
    (let [hash-bytes
            (doto (MessageDigest/getInstance "MD5")
              (.reset)
              (.update (clojure.contrib.io/to-byte-array string)))]
      (format "%x" (new java.math.BigInteger 1 (.digest hash-bytes))))))
