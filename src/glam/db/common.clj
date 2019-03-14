(ns glam.db.common
  (:require [cljs.spec.alpha :as s]))

(defmacro def-with-spec
  [name pred]
  (let [kwd (keyword (str *ns*) (str name))]
    `(do
       (def ~name ~kwd)
       (s/def ~kwd ~pred))))
