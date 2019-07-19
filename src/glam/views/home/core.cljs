(ns glam.views.home.core
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [glam.db.core :as glam-db]
   [glam.views.common.spaced-paper :refer [spaced-paper]]
   [glam.interop.material-ui :as mui])
  (:require-macros
   [glam.interop.material-ui :refer [defstyled]]))

;; home
(defn styles
  [theme]
  #js{:content #js{:font-size "18pt"}
      :grid #js{:width "100%"
                :height "100%"}})


(defstyled project-grid styles
  [{:keys [classes]}]
  (let [projects (rf/subscribe [glam-db/projects])]
    [mui/grid-list {:cell-height 180
                    :cols 4
                    :class-name (.-grid classes)}
     [mui/grid-list-tile {:key "Projects"
                          :cols 4
                          :style #js{:height "auto"}}
      [mui/typography {:component "h2"}
       "Projects"]]
     (for [project @projects]
       [mui/grid-list-tile {:key project}
        [mui/card
         [mui/card-content
          [mui/typography {:component "h2"} project]
          [mui/typography {:color "textSecondary"} "placeholder for stats"]]]])]))

(defn home-panel
  []
  [spaced-paper
   {:class-name "content"}
   [:div
    [project-grid]
    [:a {:href "/document/123"}
     "go to Document Page"]]])
