(ns brainiac.test.plugins.elovation-game
  (:use [brainiac.plugins.elovation-game]
        [clojure.test]))

(def game-json (java.io.ByteArrayInputStream. (.getBytes "{\"name\":\"Ping Pong\",\"ratings\":[{\"player\":{\"name\":\"Amit Jhawar\",\"email\":null},\"value\":1267},{\"player\":{\"name\":\"Dave Pirotte\",\"email\":\"dpirotte@gmail.com\"},\"value\":1114},{\"player\":{\"name\":\"Michael Varney\",\"email\":null},\"value\":1081},{\"player\":{\"name\":\"Drew Olson\",\"email\":\"drew@drewolson.org\"},\"value\":1060},{\"player\":{\"name\":\"David Vasser\",\"email\":\"david.vasser@getbraintree.com\"},\"value\":1054}],\"results\":[{\"winner\":\"Brian Cosgrove\",\"loser\":\"William Dix\",\"created_at\":\"2012-05-18 17:33:40 UTC\"},{\"winner\":\"Cory Monty\",\"loser\":\"John Downey\",\"created_at\":\"2012-05-17 23:19:31 UTC\"},{\"winner\":\"Brian Cosgrove\",\"loser\":\"William Dix\",\"created_at\":\"2012-05-17 22:54:03 UTC\"},{\"winner\":\"William Dix\",\"loser\":\"Brian Cosgrove\",\"created_at\":\"2012-05-17 22:51:15 UTC\"},{\"winner\":\"Brian Cosgrove\",\"loser\":\"William Dix\",\"created_at\":\"2012-05-16 22:44:33 UTC\"}]}")))

(deftest test-transform
  (let [result (transform game-json)]
    (testing "sets title"
      (is (= "Ping Pong" (:title result))))
    (testing "sets name"
      (is (= "elovation-game" (:name result))))
    (testing "sets type"
      (is (= "elovation-game" (:type result))))
    (testing "sets rank-data"
      (is (= ["1267 - Amit Jhawar" "1114 - Dave Pirotte" "1081 - Michael Varney" "1060 - Drew Olson" "1054 - David Vasser"] (:rank-data result))))
    (testing "sets result-data"
      (is (= ["Brian Cosgrove beat William Dix at 12:33PM on 5/18"] (take 1 (:result-data result)))))))
