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
        plugins (last program)]
    (loop [plugin (first plugins)
           remaining (rest plugins)]
      (when-not (nil? plugin)
        (register plugin program-name)
        (recur (first remaining) (rest remaining))))))

(defn load-programs [file]
  (let [configuration (read-config-file file)]
    (loop [program (first configuration)
           remaining (rest configuration)]
      (when-not (nil? program)
        (create program)
        (recur (first remaining) (rest remaining))))))

