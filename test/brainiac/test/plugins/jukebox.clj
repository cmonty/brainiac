(ns brainiac.test.plugins.jukebox
  (:use [brainiac.plugins.jukebox]
        [clojure.test]))

(def track-json (java.io.ByteArrayInputStream. (.getBytes "{\"isRequester\":false,\"progress\":459,\"duration\":482,\"playing\":true,\"artist\":\"Sufjan Stevens\",\"owner\":\"cory\",\"title\":\"Too Much\",\"album\":\"The Age of Adz\",\"playCount\":9,\"skipCount\":1,\"artwork\":{\"extra-large\":\"http://userserve-ak.last.fm/serve/174s/50938467.png\"},\"id\":\"a24826c1-8cd6-4d31-9005-e3b742714f4f\",\"requester\":\"(randomizer)\"}")))

(def track-with-missing-art-json (java.io.ByteArrayInputStream. (.getBytes "{\"isRequester\":false,\"progress\":459,\"duration\":482,\"playing\":true,\"artist\":\"Sufjan Stevens\",\"owner\":\"cory\",\"title\":\"Too Much\",\"album\":\"The Age of Adz\",\"playCount\":9,\"skipCount\":1,\"artwork\":{\"extra-large\":\"/img/not-found.png\"},\"id\":\"a24826c1-8cd6-4d31-9005-e3b742714f4f\",\"requester\":\"(randomizer)\"}")))

(deftest test-transform
  (let [result (transform "http://jukebox" track-json)]
    (testing "sets name"
      (is (= "jukebox" (:name result))))
    (testing "sets artist"
      (is (= "Sufjan Stevens" (:artist result))))
    (testing "sets title"
      (is (= "Too Much" (:title result))))
    (testing "sets album"
      (is (= "The Age of Adz" (:album result))))
    (testing "sets artwork"
      (is (=  "http://userserve-ak.last.fm/serve/174s/50938467.png" (:artwork result))))
    (testing "sets requester"
      (is (= "(randomizer)" (:requester result)))))
  (let [result (transform "http://jukebox" track-with-missing-art-json)]
    (testing "appends the Jukebox URL to the relative URL"
      (is (= "http://jukebox/img/not_found.png")))))
