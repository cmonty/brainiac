(ns brainiac.test.plugins.jenkins-builds
  (:use [brainiac.plugins.jenkins-builds]
        [clojure.test]))

(def example-xml-with-failures (java.io.ByteArrayInputStream. (.getBytes "<Projects><Project name=\"project1\" lastBuildStatus=\"Success\"/><Project name=\"project2\" lastBuildStatus=\"Failure\"/><Project name=\"project3\" lastBuildStatus=\"Failure\"/><Project name=\"project4\" lastBuildStatus=\"Unknown\"/></Projects>")))

(def example-xml-no-failures (java.io.ByteArrayInputStream. (.getBytes "<Projects><Project name=\"project1\" lastBuildStatus=\"Success\"/><Project name=\"project2\" lastBuildStatus=\"Success\"/><Project name=\"project3\" lastBuildStatus=\"Success\"/></Projects>")))

(deftest test-transform-with-failures
  (let [result (transform example-xml-with-failures)]
    (testing "sets name"
      (is (= "jenkins-builds" (:name result))))
    (testing "sets type"
      (is (= "jenkins" (:type result))))
    (testing "sets title"
      (is (= "Jenkins: 2 Failing Builds" (:title result))))
    (testing "sets class name"
      (is (= "jenkins-failure" (:html_class result))))
    (testing "sets data"
      (is (= ["project3" "project5"])))))

(deftest test-transform-without-failures
  (let [result (transform example-xml-no-failures)]
    (testing "sets name"
      (is (= "jenkins-builds" (:name result))))
    (testing "sets type"
      (is (= "jenkins" (:type result))))
    (testing "sets title"
      (is (= "Jenkins: All builds passing" (:title result))))
    (testing "sets class name"
      (is (= "jenkins-success" (:html_class result))))
    (testing "sets data"
      (is (= [])))))
