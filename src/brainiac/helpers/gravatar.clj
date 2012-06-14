(ns brainiac.helpers.gravatar
  (:require [brainiac.helpers.md5 :as md5]))

(def secure-url "https://secure.gravatar.com/avatar/")

(defn email->gravatar [email]
  (str secure-url (md5/str->hash email)))
