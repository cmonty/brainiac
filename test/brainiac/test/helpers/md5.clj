(ns brainiac.test.helpers.md5
  (:require [brainiac.helpers.md5 :as md5])
  (:use [clojure.test]
        [brainiac.helpers.md5]))

(deftest md5
  (testing "hashes email using md5 algorithm"
    (is (= "55502f40dc8b7c769880b10874abc9d0" (md5/str->hash "test@example.com")))))
