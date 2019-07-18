(ns glam.events.core
  (:require
   [re-frame.core :as rf]
   [glam.db.core :as glam-db]
   [glam.db.document.core :as doc-db]
   [glam.events.common :refer [reg-simple-event-db]]
   [glam.events.interceptors :refer [check-db-spec]]
   [glam.events.pouchdb :as pdb]
   ))

(def set-active-panel ::set-active-panel)
(rf/reg-event-db
 set-active-panel
 [check-db-spec]
 (fn [db [_ active-panel id]]
   (js/console.log (str "db valid?: " (glam-db/valid-db? db)))
   (js/console.log (prn-str db))
   (-> db
       (glam-db/set-active-panel active-panel)
       (doc-db/set-id id))))

(def set-projects ::set-projects)
(rf/reg-event-db
 set-projects
 (fn [db [_ projects]]
   (assoc db glam-db/projects projects)))

(def set-projects-failure ::set-projects-failure)
(rf/reg-event-db
 set-projects-failure
 (fn [db [_ err]]
   (when goog.DEBUG
     (js/Error. "Failed to load projects: " err))
   db))

(def set-active-project ::set-active-project)
(rf/reg-event-fx
 set-active-project
 [check-db-spec]
 (fn [{:keys [db]} [_ project-name]]
   {:db (-> db
            (glam-db/set-active-project project-name))
    ;; TODO: pouchdb effect goes here to load docs
    }))

(def initialize-db ::initialize-db)
(rf/reg-event-fx
 initialize-db
 (fn [cofx _]
   {:db glam-db/default-db
    :dispatch [pdb/load-projects set-projects set-projects-failure]
    }))

