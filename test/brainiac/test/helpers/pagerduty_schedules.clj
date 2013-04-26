(ns brainiac.test.helpers.pagerduty-schedules
  (:use [brainiac.helpers.pagerduty-schedules]
        [clojure.test]
        [clojure.contrib.mock]))

(defn schedules-json [name email]
  (str "{\"total\":1,\"entries\":[{\"end\":\"2012-01-23T08:55:00-06:00\",\"start\":\"2012-01-23T08:55:00-06:00\",\"user\":{\"name\":\"" name "\",\"id\":\"PAVOS91\",\"color\":\"purple\",\"email\":\"" email "\"}}]}"))

(def primary-json (java.io.ByteArrayInputStream. (.getBytes (schedules-json "Ali Aghareza" "ali@getbraintree.com"))))
(def backup-json (java.io.ByteArrayInputStream. (.getBytes (schedules-json "Paul Gross" "pgross@getbraintree.com"))))

(deftest test-transform
  (let [parsed-result (transform [primary-json backup-json])]
    (testing "primary"
      (is (= "Ali Aghareza" (:primary_name parsed-result)))
      (is (= "ali@getbraintree.com", (:primary_email parsed-result)))
      (is (= "https://secure.gravatar.com/avatar/cb47210be3a8be90e2af65156e57b324", (:primary_gravatar parsed-result)))
    (testing "backup"
      (is (= "Paul Gross" (:backup_name parsed-result)))
      (is (= "pgross@getbraintree.com", (:backup_email parsed-result)))
      (is (= "https://secure.gravatar.com/avatar/6d804cfe97a2d8f9c669a1da65993c5c", (:backup_gravatar parsed-result)))))))
