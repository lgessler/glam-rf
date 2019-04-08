(ns glam.events.core
  (:require
   [re-frame.core :as rf]
   [glam.db.core :as glam-db]
   ))

(def initialize-db ::initialize-db)
(rf/reg-event-db
 initialize-db
 (fn [_ _]
   glam-db/default-db))

(def set-active-panel ::set-active-panel)
(rf/reg-event-db
 set-active-panel
 (fn [db [_ active-panel]]
   (js/console.log (glam-db/valid-db? db))
   (assoc db glam-db/active-panel active-panel)))
