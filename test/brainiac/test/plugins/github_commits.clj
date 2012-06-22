(ns brainiac.test.plugins.github-commits
  (:use [brainiac.plugins.github-commits]
        [clojure.test]))

(def github-json (java.io.ByteArrayInputStream. (.getBytes (slurp "test/brainiac/test/data/github_commits.json"))))

(deftest test-transform
  (let [result (transform "cmonty" "brainiac" github-json)]
    (testing "sets title"
      (is (= "Recent commits to cmonty/brainiac" (:title result))))
    (testing "sets name"
      (is (= "github-commits" (:name result))))
    (testing "sets data"
      (is (= "mikepilat - Forgot to add test file for cta-bus-tracker plugin in previous commit" (first (:data result)))))))
