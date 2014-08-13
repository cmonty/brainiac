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
              :name "hav u seen ?"
              :handle "Rutzen__"
              :time "2 years ago"
              :text "o Brainiac \u00e9 meio babac\u00e3o"
              :profile_image_url "https://si0.twimg.com/profile_images/378800000060004493/527b88a6db7d7d7e7975953a99666984_normal.png"
            }
       )))))
