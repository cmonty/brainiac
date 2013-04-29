(ns brainiac.test.plugins.hp-access-point
  (:use [brainiac.plugins.hp-access-point]
        [clojure.test]))

(def example-xml (java.io.ByteArrayInputStream. (.getBytes "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:PROCURVE-MOBILITY-MSM=\"http://www.procurve_mobility_msm.com/SOAP/API/1.7/\" xmlns:VERSION=\"http://www.procurve_mobility_msm.com/SOAP/API/1.0/\"><SOAP-ENV:Body><PROCURVE-MOBILITY-MSM:GetVirtualSCPSKResponse><PROCURVE-MOBILITY-MSM:psk>super secret</PROCURVE-MOBILITY-MSM:psk></PROCURVE-MOBILITY-MSM:GetVirtualSCPSKResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>")))

(deftest test-transfor
  (let [result (transform "FBI Van" example-xml)]
    (testing "sets name"
      (is (= "hp-access-point" (:name result))))
    (testing "sets network"
      (is (= "FBI Van")))
    (testing "sets passphrase"
      (is (= "super_secret")))))
