(ns glam.interop.material-ui
  (:require [reagent.core :as r]
            [glam.interop.material-ui :as mui]))

;; NOTE: `component` can only be a form-1 reagent component, i.e., it must be pure
;; hiccup. Form-2 and form-3 components are not supported. If you need to use
;; something other than a form-1 component, try to shift whatever you need into a
;; subcomponent, or manually do the work inside this macro.
(defmacro defstyled
  "Abstracts away some syntactic messiness: MUI has an idiosyncratic
  way of accommodating component-specific styles that becomes tiresome to type.
  The component returned by this function will have the `classes` key
  available in its properties, which were the classes supplied in `styles`"
  [name styles args component]
  `(def ~name (mui/styled-component
               ~styles
               (fn ~args
                 ~component))))
