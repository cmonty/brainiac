(ns brainiac.test.plugins.pagerduty-incidents
  (:use [brainiac.plugins.pagerduty-incidents]
        [clojure.test]
        [clojure.contrib.mock]))

(def incidents-json (java.io.ByteArrayInputStream. (.getBytes (slurp "test/brainiac/test/data/pagerduty_incidents.json"))))

(deftest test-transform
  (let [parsed-result (transform incidents-json)]
    (testing "sets name"
      (is (= "pagerduty-incident" (:name parsed-result))))
    (testing "sets data"
      (is (= "Production Exception" (:name (:service (first (:data parsed-result)))))))
    (testing "sets type"
      (is (= "alert" (:type parsed-result))))))
