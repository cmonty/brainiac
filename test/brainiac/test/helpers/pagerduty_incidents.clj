(ns brainiac.test.helpers.pagerduty-incidents
  (:use [brainiac.helpers.pagerduty-incidents]
        [clojure.test]
        [clojure.contrib.mock]))

(def incidents-json (java.io.ByteArrayInputStream. (.getBytes (slurp "test/brainiac/test/data/pagerduty_incidents.json"))))

(deftest test-transform
  (let [parsed-result (transform incidents-json)]
    (testing "sets incident-count"
      (is (= 1 (:incidents-count parsed-result))))
    (testing "sets incidents"
      (is (= "Exceptions" (:name (:service (first (:incidents parsed-result)))))))))
