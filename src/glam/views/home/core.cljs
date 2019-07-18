(ns glam.views.home.core
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [glam.views.common.spaced-paper :refer [spaced-paper]])
  (:require-macros
   [glam.interop.material-ui :refer [defstyled]]))

;; home
(defn styles
  [theme]
  #js{:content #js{:font-size "18pt"}})

(defn home-panel
  []
  [spaced-paper
   {:class-name "content"}
   [:div
    [:a {:href "/document/123"}
     "go to Document Page"]]])
