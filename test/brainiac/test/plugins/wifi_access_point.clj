(ns brainiac.test.plugins.wifi-access-point
  (:use [brainiac.plugins.wifi-access-point]
        [clojure.test]))

(def example-web-page (java.io.ByteArrayInputStream. (.getBytes "super secret")))

(deftest test-transform
  (let [result (transform "FBI Van" example-web-page)]
    (testing "sets name"
      (is (= "wifi-access-point" (:name result))))
    (testing "sets network"
      (is (= "FBI Van" (:network result))))
    (testing "sets passphrase"
      (is (= "super secret" (:passphrase result))))))
