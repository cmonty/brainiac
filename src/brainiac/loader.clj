(ns brainiac.loader
  (:require [clj-yaml.core :as yaml]
            [brainiac.plugin :as brainiac]))

(def loaded (atom {}))

(defn read-config-file [file]
  (yaml/parse-string (slurp file)))

(defn load-from-options [namespace program-name options]
  (let [merged-options (merge options {:program-name program-name})]
    (eval (list (symbol namespace "configure") merged-options))))

(defn register [plugin program-name]
  (let [namespace (brainiac/fullname (key plugin))
        options-list (flatten (list (val plugin)))]
    (require (symbol namespace))
    (doseq [options options-list] (load-from-options namespace program-name options))
    (swap! loaded assoc (str namespace) true)))

(defn create [program]
  (let [program-name (name (key program))
        plugins (val program)]
    (doseq [plugin plugins] (register plugin program-name))))

(defn load-programs [file]
  (let [configuration (read-config-file file)]
    (doseq [program configuration] (create program))))
