(ns glam.interop.material-ui
  (:require [reagent.core :as r]
            [glam.interop.material-ui :as mui]))

(defmacro defstyled
  [name styles component]
  `(defn ~name []
     [(mui/get-styled-component
       ~styles
       (fn []
         (mui/replace-class-name-val-in-hiccup ~component)))]))
