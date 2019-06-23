(ns glam.events.core
  (:require
   [re-frame.core :as rf]
   [glam.db.core :as glam-db]
   [glam.db.document.core :as doc-db]
   [glam.events.interceptors :refer [check-db-spec]]
   ))

(def initialize-db ::initialize-db)
(rf/reg-event-db
 initialize-db
 (fn [_ _]
   glam-db/default-db))

(def set-active-panel ::set-active-panel)
(rf/reg-event-db
 set-active-panel
 [check-db-spec]
 (fn [db [_ active-panel id]]
   (js/console.log (str "db valid?: " (glam-db/valid-db? db)))
   (let [db (assoc db glam-db/active-panel active-panel)]
     (if (some? id)
       (assoc-in db [doc-db/active-document doc-db/id] id)
       db))))
