(ns glam.db.common
  (:require [cljs.spec.alpha :as s]))

(defmacro defdbkey
  ([name]
   "A combination of def and cljs.spec.alpha/def: defines a new var from a given
    keyword."
   (let [kwd (keyword #_(str *ns*) (str name))
         ns-kwd (keyword (str *ns*) (str name))]
     `(do
        (def ~name ~kwd)
        (s/def ~ns-kwd #(= % ~kwd)))))

  ([name pred]
   "This does two things:
    1. defines a var that holds an un-namespaced keyword that is used in the database
    2. uses `pred` to create a spec for the associated value in the database"
   (let [kwd (keyword #_(str *ns*) (str name))
         ns-kwd (keyword (str *ns*) (str name))]
     `(let [~'path (conj ~base-path ~kwd)]
        (def ~name ~kwd)
        (s/def ~ns-kwd ~pred))))

  ([name base-path pred]
   "This does the two things above, and also:
    3. creates a method `get-<name>` that is used to retrieve the key given the
       root re-frame database
    4. creates a method `set-<name>` that is used to assoc a new value with the key
       given the root re-frame database"
   (let [kwd (keyword #_(str *ns*) (str name))
         ns-kwd (keyword (str *ns*) (str name))
         get-fname (symbol (str "get-" name))
         set-fname (symbol (str "set-" name))]
     `(let [path# (conj ~base-path ~kwd)]
        (def ~name ~kwd)
        (s/def ~ns-kwd ~pred)
        (defn ~get-fname
          ~'[db]
          (get-in ~'db path#))
        (defn ~set-fname
          ~'[db v]
          (assoc-in ~'db path# ~'v))))))
