(ns brainiac.test.helpers.gravatar
  (:require [brainiac.helpers.gravatar :as gravatar])
  (:use [clojure.test]))

(deftest test-gravatar-url
  (testing "returns gravatar url for email"
    (is (= "https://secure.gravatar.com/avatar/55502f40dc8b7c769880b10874abc9d0"
           (gravatar/email->gravatar "test@example.com")))))
