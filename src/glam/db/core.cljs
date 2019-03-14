(ns glam.db.core
  (:require [cljs.spec.alpha :as s])
  (:require-macros [cljs.spec.alpha :as s]
                   [glam.db.common :refer [def-with-spec]]))

(def-with-spec ::home-panel string?)
(def-with-spec ::about-panel string?)
(def-with-spec ::active-panel #{home-panel about-panel})

(def-with-spec ::db (s/keys :req [::active-panel]))

(defn valid-db?
  [db]
  (s/valid? ::db db))

(def default-db
  {active-panel home-panel})
