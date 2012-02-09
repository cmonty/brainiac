(ns brainiac.test.plugins.elovation-game
  (:use [brainiac.plugins.elovation-game]
        [clojure.test]))

(def game-json (java.io.ByteArrayInputStream. (.getBytes "{\"name\":\"Ping Pong\",\"ratings\":[{\"player\":\"Drew Olson\",\"value\":1081},{\"player\":\"Amit Jhawar\",\"value\":1072},{\"player\":\"Patrick Schless\",\"value\":1055},{\"player\":\"Cory Monty\",\"value\":1033},{\"player\":\"Li-Hsuan Lung\",\"value\":1031}],\"results\":[{\"winner\":\"Amit Jhawar\",\"loser\":\"Michael Varney\",\"created_at\":\"2012-02-09 00:34:35 UTC\"},{\"winner\":\"Li-Hsuan Lung\",\"loser\":\"Brian Cosgrove\",\"created_at\":\"2012-02-08 23:53:57 UTC\"},{\"winner\":\"Cory Monty\",\"loser\":\"Brian Cosgrove\",\"created_at\":\"2012-02-08 23:53:49 UTC\"},{\"winner\":\"Patrick Schless\",\"loser\":\"Brian Cosgrove\",\"created_at\":\"2012-02-08 21:58:15 UTC\"},{\"winner\":\"Faiva Walker\",\"loser\":\"Tom Preuss\",\"created_at\":\"2012-02-08 20:10:04 UTC\"}]}")))

(deftest test-transform
  (let [result (transform game-json)]
    (testing "sets title"
      (is (= "Ping Pong" (:title result))))
    (testing "sets name"
      (is (= "elovation-game" (:name result))))
    (testing "sets type"
      (is (= "elovation-game" (:type result))))
    (testing "sets rank-data"
      (is (= ["1081 - Drew Olson" "1072 - Amit Jhawar" "1055 - Patrick Schless" "1033 - Cory Monty" "1031 - Li-Hsuan Lung"] (:rank-data result))))
    (testing "sets result-data"
      (is (= ["Amit Jhawar beat Michael Varney at 6:34PM on 2/8" "Li-Hsuan Lung beat Brian Cosgrove at 5:53PM on 2/8"] (take 2 (:result-data result)))))))
