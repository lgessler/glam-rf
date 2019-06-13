(ns glam.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:require
   [re-frame.core :as re-frame]
   [glam.events.core :as events]
   [glam.db.core :as glam-db]
   [accountant.core :as accountant]
   [secretary.core :as secretary]
   ))

(defn configure-routes! []
  ;; --------------------
  ;; define routes here
  (defroute "/" []
    (re-frame/dispatch [events/set-active-panel glam-db/home-panel]))

  (defroute "/document/:id" {:as params}
    (re-frame/dispatch [events/set-active-panel glam-db/document-panel (:id params)]))
  )

(defn hook-navigation! []
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))})
  (accountant/dispatch-current!))
