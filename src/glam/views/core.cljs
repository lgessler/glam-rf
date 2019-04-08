(ns glam.views.core
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [glam.interop.material-ui :as mui]
   [glam.db.core :as glam-db]
   [glam.views.app-bar :refer [app-bar]])
  (:require-macros
   [glam.interop.material-ui :refer [defstyled]]))

;; home
(defn styles
  [theme]
  #js{:content #js{:font-size "18pt"}})

(defn home-content [props]
  [:main
   {:class-name (mui/get-class-name props "content")}
   [:div
    [:a {:href "/about"}
     "go to About Page"]]])

(defstyled home-panel styles
  [:main
   {:class-name "content"}
   [:div
    [:a {:href "/about"}
     "go to About Page"]]])

;; about
(defn about-panel []
  [:main
   [:h1 "This is the About Page."]
   [:div
    [:a {:href "/"}
     "go to Home Page"]]])

;; main
(defn- panels [panel-name]
  (condp = panel-name
    glam-db/home-panel [home-panel]
    glam-db/about-panel [about-panel]
    [:div]))

(defn inner-panel []
  (let [active-panel (rf/subscribe [glam-db/active-panel])]
    [panels @active-panel]))

(defn- main-panel-contents [props]
  [:<>
   [mui/css-baseline]
   [:div
    [mui/mui-theme-provider {:theme mui/default-theme}
     [app-bar {:position "static"
               :color "primary"}]
     [inner-panel]]]])

(defn main-panel []
  [(mui/get-styled-component
   styles
   main-panel-contents)])
