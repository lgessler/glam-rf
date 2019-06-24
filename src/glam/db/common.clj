(ns glam.db.common
  (:require [cljs.spec.alpha :as s]))

(defmacro defdbkey
  "A combination of def and cljs.spec.alpha/def: defines a new var from a given
  symbol, name, with the value ::name, and also creates a spec def using pred.
  If pred is not provided, assumes the spec should check for the keyword."
  ([name]
   (let [kwd (keyword #_(str *ns*) (str name))
         ns-kwd (keyword (str *ns*) (str name))]
     `(do
        (def ~name ~kwd)
        (s/def ~ns-kwd #(= % ~kwd)))))
  ([name pred]
   (let [kwd (keyword #_(str *ns*) (str name))
         ns-kwd (keyword (str *ns*) (str name))]
     `(do
        (def ~name ~kwd)
        (s/def ~ns-kwd ~pred)))))
