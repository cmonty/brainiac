(ns brainiac.test.plugins.clock
  (:use [brainiac.plugins.clock]
        [clojure.test]
        [clojure.contrib.mock]))

(deftest test-message
  (expect [now (returns 1343081399837)]
    (let [result (build-message [{:name "Chicago" :timezone "America/Chicago"} {:name "UTC", :timezone "UTC"}])]
      (testing "sets name"
        (is (= "clock" (:name result))))
      (testing "sets time-utc"
        (is (= 1343081399837 (:time_utc result))))
      (testing "sets timezones"
        (is (= [{ :name "Chicago" :offset -18000000 } {:name "UTC" :offset 0}] (:timezones result)))))))
