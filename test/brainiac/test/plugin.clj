(ns brainiac.test.plugin
  (:use [brainiac.plugin]
        [clojure.test]))


(deftest test-render-template
  (binding [*templates-path* (. (java.io.File. "." "test/brainiac/test/templates") getCanonicalPath)]
    (testing "renders a plugins template"
        (is (= (render-template "brainiac.plugins.test-plugin") [:script {:type "text/mustache" :id "test-plugin-template"} "<h1>Test Plugin</h1>\n"])))))
