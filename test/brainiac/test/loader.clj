(ns brainiac.test.loader
  (:require [brainiac.plugin :as brainiac])
  (:use [clojure.test]
        [clojure.contrib.mock]
        [brainiac.loader]))

(deftest test-read-config-file
  (expect [brainiac/schedule (returns nil)]
    (testing "reading a config file"
      (let [config (read-config-file "test/brainiac/test/config.yml")]
        (is (= (-> config :test :nagios-problems :host) "http://nagios-problems.com"))))

      (testing "loading plugins"
        (load-programs "test/brainiac/test/config.yml")
        (is (= (first @loaded) "brainiac.plugins.google-weather")))

      (testing "plugins loaded in order"
        (load-programs "test/brainiac/test/config.yml")
        (is (= @loaded [
                    "brainiac.plugins.google-weather",
                    "brainiac.plugins.nagios-problems",
                    "brainiac.plugins.jenkins-builds",
                    "brainiac.plugins.dark-sky",
                    "brainiac.plugins.graphite"])))))
