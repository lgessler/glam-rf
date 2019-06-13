(ns glam.views.document.core
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [glam.interop.material-ui :as mui]
   [glam.db.core :as glam-db]
   [glam.db.document.core :as doc-db]
   [glam.views.app-bar :refer [app-bar]])
  (:require-macros
   [glam.interop.material-ui :refer [defstyled]]))


(defn styles
  [theme]
  #js{:content #js{:font-size "18pt"}})

;(defstyled home-panel styles
;  [:main
;   {:class-name "content"}
;   [:div
;    [:a {:href "/about"}
;     "go to About Page"]]])


;; about
(defn document-panel []
  (let [doc-id (rf/subscribe [doc-db/id])]
    [:main
     [:h1 (str "Hello! " @doc-id)]
     [:div
      [:a {:href "/"}
       "go to Home Page"]]]))

