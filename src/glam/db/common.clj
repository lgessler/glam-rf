(ns glam.db.common
  (:require [cljs.spec.alpha :as s]))

(defmacro defkeyword
  "A combination of def and cljs.spec.alpha/def: defines a new var from a given
  symbol, name, with the value ::name, and also creates a spec def using pred."
  [name pred]
  (let [kwd (keyword (str *ns*) (str name))]
    `(do
       (def ~name ~kwd)
       (s/def ~kwd ~pred))))
