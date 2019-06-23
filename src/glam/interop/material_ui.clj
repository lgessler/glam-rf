(ns glam.interop.material-ui
  (:require [reagent.core :as r]
            [glam.interop.material-ui :as mui]))

(defmacro defstyled
  "Abstracts away some syntactic messiness: MUI has an idiosyncratic
  way of accommodating component-specific styles that becomes a
  little cumbersome in ClojureScript."
  [name styles props component]
  `(defn ~name ~props
     (fn ~props
       [(mui/get-styled-component
         ~styles
         (fn []
           (mui/replace-class-name-val-in-hiccup ~component)))])))
