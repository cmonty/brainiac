(ns brainiac.test.helpers.md5
  (:require [brainiac.helpers.md5 :as md5])
  (:use [clojure.test]))

(deftest md5-string-hash
  (testing "returns empty string if nil"
    (is (= "" (md5/str->hash nil))))
  (testing "returns empty string if blank string"
    (is (= "" (md5/str->hash ""))))
  (testing "pads short hashes with zero"
    (is (= "0cc175b9c0f1b6a831c399e269772661" (md5/str->hash "a"))))
  (testing "hashes email using md5 algorithm"
    (is (= "55502f40dc8b7c769880b10874abc9d0" (md5/str->hash "test@example.com")))))
