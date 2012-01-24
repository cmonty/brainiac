(ns brainiac.test.plugins.pagerduty-schedules
  (:use [brainiac.plugins.pagerduty-schedules]
        [clojure.test]
        [clojure.contrib.mock]))

(def schedules-json (java.io.ByteArrayInputStream. (.getBytes (slurp "test/brainiac/test/data/pagerduty_schedules.json"))))

(deftest test-transform
  (let [parsed-result (transform schedules-json)]
    (testing "sets name"
      (is (= "pagerduty-schedule" (:name parsed-result))))
    (testing "sets data"
      (is (= "Ali Aghareza" (:name (:user (first (:data parsed-result)))))))
    (testing "sets type"
      (is (= "schedule" (:type parsed-result))))))
