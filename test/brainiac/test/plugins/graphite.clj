(ns brainiac.test.plugins.graphite
  (:use [brainiac.plugins.graphite]
        [clojure.test]))

(def example-json (java.io.ByteArrayInputStream. (.getBytes "{
  \"target\": \"test.target\",
  \"datapoints\": \"[[1234, 1347849960]]\"}")))

(deftest test-url
  (testing "sets correct url"
    (let [url-request (url "https://graphite.com" "sumSeries(test.data)")]
      (is (= "https://graphite.com/render?format=json&from=today&target=sumSeries(test.data)" url-request)))))

(deftest test-transform
  (let [result (transform example-json)]
    (testing "sets name"
      (is (= "graphite" (:name result))))

    (testing "extracts datapoints"
      (is (= "[[1234, 1347849960]]" (-> result :data :datapoints))))))
