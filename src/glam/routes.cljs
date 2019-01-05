(ns glam.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require
   [re-frame.core :as re-frame]
   [glam.events :as events]
   [accountant.core :as accountant]
   [secretary.core :as secretary]
   ))

(defn configure-routes! []
  ;; --------------------
  ;; define routes here
  (defroute "/" []
    (re-frame/dispatch [::events/set-active-panel :home-panel]))

  (defroute "/about" []
    (re-frame/dispatch [::events/set-active-panel :about-panel]))

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
