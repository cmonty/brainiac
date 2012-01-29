(ns brainiac.test.loader
  (:require [brainiac.plugins.nagios-problems])
  (:use [clojure.test]
        [brainiac.loader]))

(deftest test-read-config-file
  (testing "reading a config file"
    (let [config (read-config-file "test/brainiac/test/config.yml")]
      (is (= (-> config :test :nagios-problems :host) "http://foo.bar.com"))))

    (testing "loading plugins"
      (load-programs "test/brainiac/test/config.yml")
      (is (contains? @loaded "brainiac.plugins.nagios-problems"))))
