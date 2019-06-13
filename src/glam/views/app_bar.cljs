(ns glam.views.app-bar
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [glam.interop.material-ui :as mui]))

(defn app-bar [props]
  [mui/app-bar props
   [mui/toolbar {:variant "dense"}
    [mui/icon-button {:color "inherit"}
     [(mui/get-icon "Menu")]]
    [mui/typography {:variant "h6" :color "inherit"}
     "Test"]]])
