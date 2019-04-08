(ns glam.db.core
  (:require [cljs.spec.alpha :as s]
            [re-frame.core :as rf])
  (:require-macros [cljs.spec.alpha :as s]
                   [glam.db.common :refer [defdbkey]]))

;; db
(defdbkey home-panel)
(defdbkey about-panel)
(defdbkey active-panel #{home-panel about-panel})

(defdbkey db-root (s/keys :req [active-panel]))

(defn valid-db?
  [db]
  (s/valid? db-root db))

(def default-db
  {active-panel home-panel})

;; subs
(rf/reg-sub
 ::active-panel
 (fn [db _]
   (active-panel db)))
