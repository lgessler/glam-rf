(ns glam.views.core
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [glam.interop.material-ui :as mui]
   [glam.db.core :as glam-db]
   [glam.views.common.app-bar :refer [app-bar]]
   [glam.views.home.core :refer [home-panel]]
   [glam.views.document.core :refer [document-panel]]))


;; main
(defn- panels [panel-name]
  (condp = panel-name
    glam-db/home-panel [home-panel]
    glam-db/document-panel [document-panel]
    (throw (js/Error. (str "Unknown panel name: " panel-name)))))

(defn inner-panel []
  (let [active-panel (rf/subscribe [glam-db/active-panel])]
    [panels @active-panel]))

(defn main-panel []
  [:<>
   [mui/css-baseline]
   [:div
    [mui/mui-theme-provider {:theme mui/default-theme}
     [app-bar {:position "static"
               :color "primary"}]
     [inner-panel]]]])
