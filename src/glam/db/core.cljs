(ns glam.db.core
  (:require [cljs.spec.alpha :as s]
            [re-frame.core :as rf]
            [glam.db.document.core :refer [active-document]])
  (:require-macros [cljs.spec.alpha :as s]
                   [glam.db.common :refer [defdbkey]]))

;; db spec ----------------------------------------------------------------------
(defdbkey home-panel)
(defdbkey document-panel)
(defdbkey active-panel #{home-panel document-panel})

;; document key
(defdbkey db-root (s/keys :req [active-panel
                                active-document]))

(defn valid-db?
  [db]
  (s/valid? db-root db))

(def default-db
  {active-panel home-panel
   active-document {}})

;; subs
(rf/reg-sub
 active-panel
 (fn [db _]
   (active-panel db)))
