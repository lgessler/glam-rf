(ns glam.views.common.spaced-paper
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [glam.interop.material-ui :as mui])
  (:require-macros
   [glam.interop.material-ui :refer [defstyled]]))

(defn styles
  [theme]
  #js{:base #js{:padding "1rem"
                :margin "1rem"}})

(defstyled spaced-paper styles
  [{:keys [classes]}]
  (let [this (r/current-component)]
    (into [mui/paper
           {:class-name (.-base classes)}]
          (r/children this))))
