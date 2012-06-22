(ns brainiac.test.plugins.cta-train-tracker
  (:use [brainiac.plugins.cta-train-tracker]
        [clojure.test]
        [clojure.contrib.mock]))

(def sample-xml (java.io.ByteArrayInputStream. (.getBytes "<?xml version=\"1.0\" encoding=\"utf-8\"?><ctatt><tmst>20120529 11:56:26</tmst><errCd>0</errCd><errNm /><eta><staId>40380</staId><stpId>30074</stpId><staNm>Clark/Lake</staNm><stpDe>Service at Inner Loop platform</stpDe><rn>706</rn><rt>Org</rt><destSt>30182</destSt><destNm>Midway</destNm><trDr>5</trDr><prdt>20120529 11:56:18</prdt><arrT>20120529 11:59:18</arrT><isApp>0</isApp><isSch>0</isSch><isDly>0</isDly><isFlt>0</isFlt><flags /></eta><eta><staId>40380</staId><stpId>30374</stpId><staNm>Clark/Lake</staNm><stpDe>Subway service toward Forest Park</stpDe><rn>115</rn><rt>Blue</rt><destSt>0</destSt><destNm>Forest Park</destNm><trDr>5</trDr><prdt>20120529 11:56:11</prdt><arrT>20120529 12:00:11</arrT><isApp>0</isApp><isSch>0</isSch><isDly>0</isDly><isFlt>0</isFlt><flags /></eta><eta><staId>40380</staId><stpId>30074</stpId><staNm>Clark/Lake</staNm><stpDe>Service at Inner Loop platform</stpDe><rn>304</rn><rt>Pink</rt><destSt>0</destSt><destNm>54th/Cermak</destNm><trDr>5</trDr><prdt>20120529 11:56:08</prdt><arrT>20120529 12:00:08</arrT><isApp>0</isApp><isSch>0</isSch><isDly>0</isDly><isFlt>0</isFlt><flags /></eta><eta><staId>40380</staId><stpId>30074</stpId><staNm>Clark/Lake</staNm><stpDe>Service at Inner Loop platform</stpDe><rn>007</rn><rt>G</rt><destSt>30139</destSt><destNm>Cottage Grove</destNm><trDr>5</trDr><prdt>20120529 11:55:48</prdt><arrT>20120529 12:00:48</arrT><isApp>0</isApp><isSch>0</isSch><isDly>0</isDly><isFlt>0</isFlt><flags /></eta></ctatt>")))

(deftest test-transform
  (expect [now (returns 1338310719000)]
    (let [parse-result (transform sample-xml)]
        (testing "sets name"
          (is (="cta-train-tracker" (:name parse-result))))

        (testing "sets station"
          (is (= "Clark/Lake" (:station parse-result))))

        (testing "sets data"
          (is (= [{:destination "Midway" :line "Orange" :arrival-time "DUE" :due-in-millis 39000}
                  {:destination "54th/Cermak" :line "Pink" :arrival-time "1 MIN" :due-in-millis 89000}
                  {:destination "Forest Park" :line "Blue" :arrival-time "1 MIN" :due-in-millis 92000}
                  {:destination "Cottage Grove" :line "Green" :arrival-time "2 MIN" :due-in-millis 129000}
                  ] (:data parse-result)))))))
