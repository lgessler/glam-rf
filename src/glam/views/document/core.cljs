(ns glam.views.document.core
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [glam.interop.material-ui :as mui]
   [glam.db.core :as glam-db]
   [glam.db.document.core :as doc-db])
  (:require-macros
   [glam.interop.material-ui :refer [defstyled]]))

(defn styles
  [theme]
  #js{:content #js{:font-size "14pt"}})

(defstyled stysub styles
  [x y]
  [:main
   {:class-name "content"}
   [:p x " " y]
   [:div
    [:a {:href "/about"}
     "go to About Page"]]])

(defn document-panel []
  (let [doc-id (rf/subscribe [doc-db/id])]
    [mui/paper
     [:main
      [:h1 (str "Hello! " @doc-id)]
      [stysub "one" "two"]
      [:div
       [:a {:href "/"}
        "go to Home Page"]]]]))
