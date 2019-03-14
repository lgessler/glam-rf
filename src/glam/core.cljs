(ns glam.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [glam.events.core :as events]
   [glam.routes :as routes]
   [glam.views.core :as views]
   [secretary.core :as secretary]
   [accountant.core :as accountant]
   ))


(defn dev-setup []
  (when goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (routes/configure-routes!)
  (routes/hook-navigation!)
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (dev-setup)
  (re-frame/dispatch-sync [events/initialize-db])
  (mount-root))
