(ns brainiac.test.plugins.muni-tracker
  (:use [brainiac.plugins.muni-tracker]
        [clojure.test]
        [clojure.contrib.mock]))

(def sample-stop (java.io.ByteArrayInputStream. (.getBytes (slurp "test/brainiac/test/data/muni_stop.xml"))))

(deftest test-transform
  (let [result (transform sample-stop)]
    (testing "sets routes with direction"
      (is (= ["30-Stockton"
              "45-Union Stockton"] (map :name (:routes result)))))

    (testing "sets routes with direction"
      (is (= ["Outbound to The Marina District"
              "Outbound to The Presidio"] (map :direction (:routes result)))))
    
    (testing "sets stop name"
      (is (= "Townsend and 4th" (:stop result))))

    (testing "sets departure times for a route"
      (is (= '("5" "8" "11") (:departures (first (:routes result))))))))
