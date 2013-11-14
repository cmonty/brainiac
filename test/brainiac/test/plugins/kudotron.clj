(ns brainiac.test.plugins.kudotron
  (:use [brainiac.plugins.kudotron]
        [clojure.test]))

(def example-json (java.io.ByteArrayInputStream. (.getBytes "[{\"id\":2,\"from\":\"Pilat\",\"to\":\"Harry Hoskins\",\"body\":\"Meta-kudos on the kudotron\",\"created_at\":\"2013-11-08T18:20:29.960Z\",\"updated_at\":\"2013-11-08T18:20:29.960Z\"}]")))


(deftest test-transform
  (let [result (first ((first (transform example-json)) 1))]
    (testing "sets sender"
      (is (= "Pilat" (:from result))))

    (testing "sets recipient"
      (is (= "Harry Hoskins" (-> result :to))))

    (testing "sets body"
      (is (= "Meta-kudos on the kudotron" (-> result :body))))

    (testing "sets timestamp"
      (is (= "2013-11-08T18:20:29.960Z" (-> result :timestamp))))))
