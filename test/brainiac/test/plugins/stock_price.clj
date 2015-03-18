(ns brainiac.test.plugins.stock-price
  (:use [brainiac.plugins.stock-price]
        [clojure.test]))

(defn slurp-json [suffix] (java.io.ByteArrayInputStream. (.getBytes (slurp (str "test/brainiac/test/data/stock_price_" suffix ".json")))))

(deftest test-transform-up
  (let [result (transform (slurp-json "up"))]
    (testing "sets name"
      (is (= "stock-price" (:name result))))
    (testing "sets title"
      (is (= "eBay Inc." (:title result))))
    (testing "sets price"
      (is (= "59.67" (:price result))))
    (testing "sets change"
      (is (= "0.47" (:change result))))
    (testing "sets trend"
      (is (= "up" (:trend result))))))

 (deftest test-transform-down
   (let [result (transform (slurp-json "down"))]
     (testing "sets name"
       (is (= "stock-price" (:name result))))
     (testing "sets title"
       (is (= "eBay Inc." (:title result))))
     (testing "sets price"
       (is (= "59.49" (:price result))))
     (testing "sets change"
       (is (= "-0.08" (:change result))))
     (testing "sets trend"
       (is (= "down" (:trend result))))))
