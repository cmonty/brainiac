(ns brainiac.test.plugins.cta-train-tracker
  (:use [brainiac.plugins.cta-train-tracker]
        [clojure.test]
        [clojure.contrib.mock]))

(def sample-xml (java.io.ByteArrayInputStream. (.getBytes "<?xml version=\"1.0\" encoding=\"utf-8\"?><ctatt><tmst>20120109 21:18:18</tmst><errCd>0</errCd><errNm /><eta><staId>40350</staId><stpId>30069</stpId><staNm>UIC-Halsted</staNm><stpDe>Service toward Forest Park</stpDe><rn>214</rn><rt>Blue</rt><destSt>30077</destSt><destNm>Forest Park</destNm><trDr>5</trDr><prdt>20120109 21:17:54</prdt><arrT>20120109 21:23:54</arrT><isApp>0</isApp><isSch>0</isSch><isDly>0</isDly><isFlt>0</isFlt><flags /></eta><eta><staId>40350</staId><stpId>30068</stpId><staNm>UIC-Halsted</staNm><stpDe>Service toward O'Hare</stpDe><rn>223</rn><rt>Blue</rt><destSt>30171</destSt><destNm>O'Hare</destNm><trDr>1</trDr><prdt>20120109 21:17:58</prdt><arrT>20120109 21:27:58</arrT><isApp>0</isApp><isSch>0</isSch><isDly>0</isDly><isFlt>0</isFlt><flags /></eta><eta><staId>40350</staId><stpId>30069</stpId><staNm>UIC-Halsted</staNm><stpDe>Service toward Forest Park</stpDe><rn>215</rn><rt>Blue</rt><destSt>30077</destSt><destNm>Forest Park</destNm><trDr>5</trDr><prdt>20120109 21:17:39</prdt><arrT>20120109 21:36:39</arrT><isApp>0</isApp><isSch>0</isSch><isDly>0</isDly><isFlt>0</isFlt><flags /></eta><eta><staId>40350</staId><stpId>30068</stpId><staNm>UIC-Halsted</staNm><stpDe>Service toward O'Hare</stpDe><rn>129</rn><rt>Blue</rt><destSt>30171</destSt><destNm>O'Hare</destNm><trDr>1</trDr><prdt>20120109 21:17:55</prdt><arrT>20120109 21:36:55</arrT><isApp>0</isApp><isSch>0</isSch><isDly>0</isDly><isFlt>0</isFlt><flags /></eta></ctatt>")))

(deftest test-transform
  (expect [now (returns 1326165519000)]
    (let [parse-result (transform sample-xml)]
      (testing "sets type"
        (is (= "list" (:type parse-result))))

        (testing "sets name"
          (is (="train-tracker" (:name parse-result))))

        (testing "sets title"
          (is (= "UIC-Halsted (Blue)" (:title parse-result))))

        (testing "sets data"
          (is (= ["Forest Park 5 min" "O'Hare 9 min" "Forest Park 18 min" "O'Hare 18 min"] (:data parse-result)))))))
