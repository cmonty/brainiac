(ns brainiac.loader
  (:require [clj-yaml.core :as yaml]
            [brainiac.plugin :as brainiac]))

(def loaded (atom []))

(defn read-config-file [file]
  (yaml/parse-string (slurp file)))

(defn register [plugin program-name]
  (let [namespace (brainiac/fullname (key plugin))
        options (merge (val plugin) {:program-name program-name})]
    (require (symbol namespace))
    (eval (list (symbol namespace "configure") options))
    (swap! loaded conj (str namespace))))

(defn create [program]
  (let [program-name (name (key program))
        plugins (sort-by #(:order (last %))(val program))]
    (doseq [plugin plugins] (register plugin program-name))))

(defn load-programs [file]
  (let [configuration (read-config-file file)]
    (reset! loaded [])
    (doseq [program configuration] (create program))))
