(ns brainiac.test.plugins.graphite
  (:use [brainiac.plugins.graphite]
        [clojure.test]))

(def example-json (java.io.ByteArrayInputStream. (.getBytes "[{
  \"target\": \"test.target\",
  \"datapoints\": [[1234, 1347849960], [5678, 1347849961], [null, 1347849961]]}]")))

(deftest test-url
  (testing "sets correct url"
    (let [url-request (url "https://graphite.com" "sumSeries(test.data)")]
      (is (= "https://graphite.com/render?format=json&from=-24hours&target=summarize(sumSeries(test.data),\"1h\")" url-request)))))

(deftest test-transform
  (let [result (transform example-json)]
    (testing "sets name"
      (is (= "graphite" (:name result))))

    (testing "extracts valuesy"
      (is (= [1234, 5678] (-> result :data :valuesy))))

    (testing "ignores tuples which have a null y value"
      (is (= [1347849960, 1347849961] (-> result :data :valuesx)))
      (is (= [1234, 5678] (-> result :data :valuesy))))

    (testing "extracts valuesx"
      (is (= [1347849960, 1347849961] (-> result :data :valuesx))))))
