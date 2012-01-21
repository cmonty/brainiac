(ns brainiac.test.plugins.cta-bus-tracker
  (:use [brainiac.plugins.cta-bus-tracker]
        [clojure.test]
        [clojure.contrib.mock]))

(def sample-xml (java.io.ByteArrayInputStream. (.getBytes "<?xml version=\"1.0\"?><stop><id>204</id><nm>Halsted &amp; Jackson</nm><sri><rt>8</rt><d>North Bound</d><dd>North Bound</dd><!--@3--></sri><sbs></sbs><cr>8</cr><pre><pt>9 MIN</pt><fd>Waveland/Broadway</fd><v>1211</v><rn>8</rn></pre><pre><pt>15 MIN</pt><fd>Waveland/Broadway</fd><v>1369</v><rn>8</rn></pre><pre><pt>28 MIN</pt><fd>Waveland/Broadway</fd><v>1656</v><rn>8</rn></pre></stop>")))

(deftest test-transform
  (let [parse-result (transform sample-xml)]
    (testing "sets type"
      (is (= "list" (:type parse-result))))

      (testing "sets name"
        (is (="cta-bus-tracker" (:name parse-result))))

      (testing "sets title"
        (is (= "CTA #8 Bus (North Bound) - Halsted & Jackson" (:title parse-result))))

      (testing "sets data"
        (is (= ["Waveland/Broadway - 9 MIN" "Waveland/Broadway - 15 MIN" "Waveland/Broadway - 28 MIN"] (:data parse-result))))))
