(ns brainiac.plugin-loader
  (:require [clj-yaml.core :as yaml]))

(defn read-config-file [file]
  (yaml/parse-string (slurp file)))

(defn register [plugin]
  (let [namespace (str "brainiac.plugins." (name (key plugin)))
        options (val plugin)]
    (require (symbol namespace))
    (eval (list (symbol namespace "configure") options))))

(defn register-plugins [file]
  (let [configuration (read-config-file file)]
    (loop [plugin (first configuration)
           remaining (rest configuration)]
      (when-not (nil? plugin)
        (register plugin)
        (recur (first remaining) (rest remaining))))))

