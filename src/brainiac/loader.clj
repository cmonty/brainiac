(ns brainiac.loader
  (:require [clj-yaml.core :as yaml]))

(def loaded (atom {}))

(defn read-config-file [file]
  (yaml/parse-string (slurp file)))

(defn register [plugin program-name]
  (let [namespace (str "brainiac.plugins." (name (key plugin)))
        options (merge (val plugin) {:program-name program-name})]
    (require (symbol namespace))
    (eval (list (symbol namespace "configure") options))
    (swap! loaded assoc (str namespace) true)))

(defn create [program]
  (let [program-name (name (key program))
        plugins (val program)]
    (doseq [plugin plugins] (register plugin program-name))))

(defn load-programs [file]
  (let [configuration (read-config-file file)]
    (doseq [program configuration] (create program))))
