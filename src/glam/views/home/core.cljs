(ns glam.views.home.core
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r])
  (:require-macros
   [glam.interop.material-ui :refer [defstyled]]))

;; home
(defn styles
  [theme]
  #js{:content #js{:font-size "18pt"}})

(defstyled home-panel styles
  []
  [:main
   {:class-name "content"}
   [:div
    [:a {:href "/document/123"}
     "go to Documentt Page"]]])
