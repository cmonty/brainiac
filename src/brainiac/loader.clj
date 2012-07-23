(ns brainiac.loader
  (:require [clj-yaml.core :as yaml]
            [brainiac.plugin :as brainiac]))

(def loaded (atom []))

(defn read-config-file [file]
  (yaml/parse-string (slurp file)))

(defn timeout-for-plugin [options]
  (* 1000 (get options :interval 60)))

(defn register [plugin program-name]
  (let [plugin-ns (brainiac/fullname (key plugin))
        options (merge (val plugin) {:program-name program-name})]
    (require (symbol plugin-ns))
    (brainiac/schedule
      (timeout-for-plugin options)
      (apply (ns-resolve (the-ns (symbol plugin-ns)) (symbol "configure")) [options]))
    (swap! loaded conj (str plugin-ns))))

(defn create [program]
  (let [program-name (name (key program))
        plugins (sort-by #(:order (last %))(val program))]
    (doseq [plugin plugins] (register plugin program-name))))

(defn load-programs [file]
  (let [configuration (read-config-file file)]
    (reset! loaded [])
    (doseq [program configuration] (create program))))
