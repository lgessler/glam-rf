(ns glam.views.common.app-bar
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   ["@material-ui/icons" :as mui-icons]
   [glam.interop.material-ui :as mui]))


(defn app-bar [props]
  [mui/app-bar props
   [mui/toolbar {:variant "dense"}
    [mui/icon-button {:color "inherit"}
     [:> mui-icons/Menu]]
    [mui/typography {:variant "h6" :color "inherit"}
     "Test"]]])
