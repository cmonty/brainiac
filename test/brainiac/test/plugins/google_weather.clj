(ns brainiac.test.plugins.google-weather
  (:use [brainiac.plugins.google-weather]
        [clojure.test]))

(def weather-xml (java.io.ByteArrayInputStream. (.getBytes "<?xml version=\"1.0\"?><xml_api_reply version=\"1\"><weather module_id=\"0\" tab_id=\"0\" mobile_row=\"0\" mobile_zipped=\"1\" row=\"0\" section=\"0\" ><forecast_information><city data=\"Chicago, IL\"/><postal_code data=\"Chicago\"/><latitude_e6 data=\"\"/><longitude_e6 data=\"\"/><forecast_date data=\"2012-01-13\"/><current_date_time data=\"2012-01-13 21:51:00 +0000\"/><unit_system data=\"US\"/></forecast_information><current_conditions><condition data=\"Light snow\"/><temp_f data=\"21\"/><temp_c data=\"-6\"/><humidity data=\"Humidity: 71%\"/><icon data=\"/ig/images/weather/flurries.gif\"/><wind_condition data=\"Wind: W at 12 mph\"/></current_conditions><forecast_conditions><day_of_week data=\"Fri\"/><low data=\"9\"/><high data=\"22\"/><icon data=\"/ig/images/weather/chance_of_snow.gif\"/><condition data=\"Chance of Snow\"/></forecast_conditions><forecast_conditions><day_of_week data=\"Sat\"/><low data=\"18\"/><high data=\"22\"/><icon data=\"/ig/images/weather/chance_of_snow.gif\"/><condition data=\"Chance of Snow\"/></forecast_conditions><forecast_conditions><day_of_week data=\"Sun\"/><low data=\"29\"/><high data=\"31\"/><icon data=\"/ig/images/weather/mostly_sunny.gif\"/><condition data=\"Mostly Sunny\"/></forecast_conditions><forecast_conditions><day_of_week data=\"Mon\"/><low data=\"30\"/><high data=\"41\"/><icon data=\"/ig/images/weather/chance_of_rain.gif\"/><condition data=\"Chance of Rain\"/></forecast_conditions></weather></xml_api_reply>")))

(deftest test-transform
  (let [result (transform weather-xml)]
    (testing "sets name"
      (is (= "google-weather" (:name result)))

    (testing "extracts temperature in fahrenheit"
      (is (= "21" (-> result :data :temp)))

    (testing "extracts current conditions"
      (is (= "Light snow" (-> result :data :current-conditions))))))))
