(ns glam.db.core
  (:require [cljs.spec.alpha :as s]
            [re-frame.core :as rf]
            [glam.db.common :refer [reg-simple-sub]]
            [glam.db.document.core :refer [document]])
  (:require-macros [cljs.spec.alpha :as s]
                   [glam.db.common :refer [defdbkey]]))

(defdbkey projects [] #(or (nil? %) (seq? %)))
(defdbkey active-project [] #(or (str %) (nil? %)))
(defdbkey home-panel)
(defdbkey document-panel)
(defdbkey active-panel [] #{home-panel document-panel})

(defdbkey db-root [] (s/keys :req [active-project
                                   active-panel
                                   document]))

(defn valid-db?
  [db]
  (s/valid? ::db-root db))

(def default-db
  {projects nil
   active-project nil
   active-panel home-panel
   document {}})

;; subs
(reg-simple-sub active-panel get-active-panel)
(reg-simple-sub active-project get-active-project)
(reg-simple-sub projects get-projects)
