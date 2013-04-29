(ns brainiac.plugins.hp-access-point
  (:require [brainiac.plugin :as brainiac]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.contrib.zip-filter.xml :as zf]))

(defn make-body [network]
  (str
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
  "<SOAP-ENV:Envelope\n"
  "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
  "xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
  "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
  "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
  "xmlns:PROCURVE-MOBILITY-MSM=\"http://www.procurve_mobility_msm.com/SOAP/API/1.7/\"\n"
  "xmlns:COLUBRIS=\"http://www.procurve_mobility_msm.com/SOAP/API/1.7/\">\n"
  "<SOAP-ENV:Body>\n"
  "<PROCURVE-MOBILITY-MSM:GetVirtualSCPSK>\n"
  "<PROCURVE-MOBILITY-MSM:vscName>" network "</PROCURVE-MOBILITY-MSM:vscName>\n"
  "</PROCURVE-MOBILITY-MSM:GetVirtualSCPSK>\n"
  "</SOAP-ENV:Body>\n"
  "</SOAP-ENV:Envelope>"))

(defn transform [network stream]
  (let [xml (zip/xml-zip (xml/parse stream))
        node (zf/xml1-> xml :SOAP-ENV:Body :PROCURVE-MOBILITY-MSM:GetVirtualSCPSKResponse :PROCURVE-MOBILITY-MSM:psk)
        passphrase (zf/text node)]
    { :name "hp-access-point"
      :network network
      :passphrase passphrase }))

(defn configure [{:keys [url network program-name]}]
  (let [body (make-body network)]
  (brainiac/simple-http-plugin
    {:method "POST"
     :url url
     :body body
     :headers {"Content-Type" "text/xml" "Accept" "*/*"}}
    (partial transform network) program-name)))

