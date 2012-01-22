(ns brainiac.test.plugins.twitter-search
  (:use [brainiac.plugins.twitter-search]
        [clojure.test]))

(def twitter-json (java.io.ByteArrayInputStream. (.getBytes (slurp "test/brainiac/test/data/twitter.json"))))

(deftest test-transform
  (let [result (transform twitter-json)]
    (testing "sets name"
      (is (= "twitter-search" (:name result))))
    (testing "sets type"
      (is (= "ticker" (:type result))))
    (testing "sets data"
      (is (= "T A Y. R O Z A Y\u2640\u2640: I'm such a maniac let me get some brainiac" (first (:data result)))))))
