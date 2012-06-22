(ns brainiac.test.plugins.twitter-search
  (:use [brainiac.plugins.twitter-search]
        [clojure.test]))

(def twitter-json (java.io.ByteArrayInputStream. (.getBytes (slurp "test/brainiac/test/data/twitter.json"))))

(deftest test-transform
  (let [result (transform twitter-json)]
    (testing "sets name"
      (is (= "twitter-search" (:name result))))
    (testing "sets data"
      (is (= (first (:data result))
           {
              :name "T A Y. R O Z A Y\u2640\u2640"
              :handle "RumorIS_TayGAY"
              :text "I'm such a maniac let me get some brainiac"
              :profile_image_url "https://si0.twimg.com/profile_images/1751308844/15_normal.jpg"
            }
       )))))
