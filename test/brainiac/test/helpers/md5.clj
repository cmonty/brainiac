(ns brainiac.test.helpers.md5
  (:require [brainiac.helpers.md5 :as md5])
  (:use [clojure.test]))

(deftest md5-string-hash
  (testing "returns empty string if nil"
    (is (= "" (md5/str->hash nil))))
  (testing "returns empty string if blank string"
    (is (= "" (md5/str->hash ""))))
  (testing "hashes email using md5 algorithm"
    (is (= "55502f40dc8b7c769880b10874abc9d0" (md5/str->hash "test@example.com")))))
