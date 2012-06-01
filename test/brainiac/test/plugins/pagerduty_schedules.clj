(ns brainiac.test.plugins.pagerduty-schedules
  (:use [brainiac.plugins.pagerduty-schedules]
        [clojure.test]
        [clojure.contrib.mock]))

(def schedules-json1 (java.io.ByteArrayInputStream. (.getBytes (slurp "test/brainiac/test/data/pagerduty_schedules.json"))))
(def schedules-json2 (java.io.ByteArrayInputStream. (.getBytes (slurp "test/brainiac/test/data/pagerduty_schedules.json"))))

(deftest test-transform
  (let [parsed-result (transform [schedules-json1 schedules-json2])]
    (testing "sets name"
      (is (= "pagerduty-schedules" (:name parsed-result))))
    (testing "sets data"
      (is (= "Ali Aghareza" (:primary_name parsed-result))))
    (testing "sets type"
      (is (= "schedule" (:type parsed-result))))))
