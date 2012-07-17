(ns brainiac.test.plugins.dark-sky
  (:use [brainiac.plugins.dark-sky]
        [clojure.test]))

(def example-json (java.io.ByteArrayInputStream. (.getBytes "{
  \"isPrecipitating\": true,
  \"minutesUntilChange\": 25,
  \"currentSummary\": \"rain\",
  \"hourSummary\": \"rain will stop in 25 min\",
  \"daySummary\": \"likely rain until tonight\",
  \"currentTemp\": 65,
  \"timezone\": \"America/New_York (EDT, -0400)\",
  \"checkTimeout\": 750,
  \"radarStation\": \"enx\"}")))

(deftest test-url
  (testing "sets correct url"
    (let [url-request (url "secret" 37.826 -122.423)]
      (is (= "https://api.darkskyapp.com/v1/brief_forecast/secret/37.826,-122.423" url-request)))))

(deftest test-transform
  (let [result (transform example-json)]
    (testing "sets name"
      (is (= "dark-sky" (:name result))))

    (testing "extracts temperature in fahrenheit"
      (is (= 65 (-> result :data :temp))))

    (testing "extracts forecast for next hour"
      (is (= "rain will stop in 25 min" (-> result :data :hour-summary))))

    (testing "extracts when minutes unitl current summary will change"
      (is (= 25 (-> result :data :minutes-until-change))))

    (testing "extracts current conditions"
      (is (= "rain" (-> result :data :current-summary))))))
