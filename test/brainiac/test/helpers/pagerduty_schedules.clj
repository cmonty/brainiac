(ns brainiac.test.helpers.pagerduty-schedules
  (:use [brainiac.helpers.pagerduty-schedules]
        [clojure.test]
        [clojure.contrib.mock]))

(defn schedules-json [name]
  (str "{\"total\":1,\"entries\":[{\"end\":\"2012-01-23T08:55:00-06:00\",\"start\":\"2012-01-23T08:55:00-06:00\",\"user\":{\"name\":\"" name "\",\"id\":\"PAVOS91\",\"color\":\"purple\",\"email\":\"ali@getbraintree.com\"}}]}"))

(def primary-json (java.io.ByteArrayInputStream. (.getBytes (schedules-json "Ali Aghareza"))))
(def backup-json (java.io.ByteArrayInputStream. (.getBytes (schedules-json "Paul Gross"))))

(deftest test-transform
  (let [parsed-result (transform [primary-json backup-json])]
    (testing "primary"
      (is (= "Ali Aghareza" (:primary_name parsed-result)))
      (is (= "Ali_Aghareza" (:primary_name_image parsed-result))))
    (testing "backup"
      (is (= "Paul Gross" (:backup_name parsed-result)))
      (is (= "Paul_Gross" (:backup_name_image parsed-result))))))
