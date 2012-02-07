(ns brainiac.test.plugins.elovation-game
  (:use [brainiac.plugins.elovation-game]
        [clojure.test]))

(def game-json (java.io.ByteArrayInputStream. (.getBytes "{\"name\":\"Ping Pong\",\"ratings\":[{\"player\":\"Patrick Schless\",\"value\":1064},{\"player\":\"Drew Olson\",\"value\":1047},{\"player\":\"Cory Monty\",\"value\":1023},{\"player\":\"Michael Varney\",\"value\":1023},{\"player\":\"Dave Pirotte\",\"value\":1014}],\"results\":[{\"winner\":\"Patrick Schless\",\"loser\":\"Brian Cosgrove\"},{\"winner\":\"Michael Varney\",\"loser\":\"Dan Manges\"},{\"winner\":\"Michael Varney\",\"loser\":\"Tom Preuss\"},{\"winner\":\"Ben Mills\",\"loser\":\"John Downey\"},{\"winner\":\"Amit Jhawar\",\"loser\":\"Dave Pirotte\"}]}")))

(deftest test-transform
  (let [result (transform game-json)]
    (testing "sets title"
      (is (= "Ping Pong" (:title result))))
    (testing "sets name"
      (is (= "elovation-game" (:name result))))
    (testing "sets type"
      (is (= "elovation-game" (:type result))))
    (testing "sets rank-data"
      (is (= ["1064 - Patrick Schless" "1047 - Drew Olson" "1023 - Cory Monty" "1023 - Michael Varney" "1014 - Dave Pirotte"] (:rank-data result))))
    (testing "sets result-data"
      (is (= ["Patrick Schless beat Brian Cosgrove" "Michael Varney beat Dan Manges" "Michael Varney beat Tom Preuss" "Ben Mills beat John Downey" "Amit Jhawar beat Dave Pirotte"] (:result-data result))))))
