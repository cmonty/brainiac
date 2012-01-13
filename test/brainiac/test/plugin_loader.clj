(ns brainiac.test.plugin-loader
  (:require [brainiac.plugins.nagios-problems])
  (:use [clojure.test]
        [brainiac.plugin-loader]))

(deftest test-read-config-file
  (testing "reading a config file"
    (let [config (read-config-file "test/brainiac/test/config.yml")]
      (is (= (-> config :brainiac.plugins.nagios-problems :host) "http://foo.bar.com"))))

    (testing "registering plugins"
      (let [registered (atom false)]
        (binding [brainiac.plugins.nagios-problems/configure (fn [_] (reset! registered true))]
          (register-plugins "test/brainiac/test/config.yml")
          (is @registered)))))
