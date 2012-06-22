(ns brainiac.test.plugins.rss-feed
  (:use [brainiac.plugins.rss-feed]
        [clojure.test]))

(def example-feed (java.io.ByteArrayInputStream. (.getBytes (slurp "test/brainiac/test/data/rss.xml"))))

(deftest test-transform
  (let [result (transform example-feed)]
    (testing "name"
      (is (= "rss-feed" (:name result))))
    (testing "title"
      (is (= "test blog" (:title result))))
    (testing "article title"
      (is (= "Lorem ipsum" (:title (first (:data result))))))
    (testing "artile content"
      (is (= "<p>Lorem i" (.substring (:content (first (:data result))) 0 10))))))
