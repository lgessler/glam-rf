(ns glam.interop.material-ui
  (:require [reagent.core :as r]
            [glam.interop.material-ui :as mui]))

;; to placate the compiler--these are implemented in .cljs
(declare get-class-name)
(declare replace-class-name-val-in-hiccup)

;; To understand this macro:
;;https://stackoverflow.com/questions/41605589/passing-destructured-args-through-macros
;; NOTE: `component` can only be a form-1 reagent component, i.e., it must be pure
;; hiccup. Form-2 and form-3 components are not supported since I couldn't figure out
;; how to make replace-class-name-val-in-hiccup work with them. The argument form must
;; also contain only up to one element--see comment below. If you need to use something
;; other than a form-1 component, try to shift whatever you need into a subcomponent.
(defmacro defstyled
  "Abstracts away some syntactic messiness: MUI has an idiosyncratic
  way of accommodating component-specific styles that becomes difficult."
  [name styles [p :as args-vec] component]
  ;; A user may either supply nothing, a single symbol, or a single map (for
  ;; destructuring syntax) as the argument of the function. In any case we need to
  ;; make sure we can get the name of the associated symbol, which we accomplish by
  ;; making sure `:as` is populated in the map
  (let [p (cond
            ;; if we get [], pretend we got [{:as props}]
            (nil? p) {:as 'props}
            ;; if we get [props], pretend we got [{:as props}]
            (symbol? p) {:as p}
            ;; if we already have [{..., :as <x>}], we're fine
            (:as p) p
            ;; we have a destructuring form but not an :as--add it
            (map? p) (assoc p :as 'props))]
      `(defn ~name ~[p]
         [(mui/get-styled-component
           ~styles
           (mui/replace-class-name-val-in-hiccup ~component))
          ~(:as p)])))
