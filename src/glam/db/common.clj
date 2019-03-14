(ns glam.db.common
  (:require [cljs.spec.alpha :as s]))


(defmacro def-with-spec
  [key pred]
  `(do
     (def ~(-> key name symbol) ~key)
     (s/def ~key ~pred)))
