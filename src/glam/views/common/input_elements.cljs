(ns glam.views.common.input-elements
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [cljs.spec.alpha :as s]
   [glam.interop.material-ui :as mui]))

(defmulti input-element
  (fn [ns-kwd]
    (s/get-spec ns-kwd)))
