(ns glam.db.core
  (:require [cljs.spec.alpha :as s])
  (:require-macros [cljs.spec.alpha :as s]
                   [glam.db.common :refer [defkeyword]]))

(defkeyword home-panel string?)
(defkeyword about-panel string?)
(defkeyword active-panel #{home-panel about-panel})

(defkeyword db-root (s/keys :req [active-panel]))

(defn valid-db?
  [db]
  (s/valid? db-root db))

(def default-db
  {active-panel home-panel})
