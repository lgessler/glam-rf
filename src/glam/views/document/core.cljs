(ns glam.views.document.core
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [glam.interop.material-ui :as mui]
   [glam.db.core :as glam-db]
   [glam.db.document.core :as doc-db]
   [glam.views.common.input-elements :refer [input-element]])
  (:require-macros
   [glam.interop.material-ui :refer [defstyled]]))

(defn styles
  [theme]
  #js{:base #js{:padding "1rem"
                :margin "1rem"}
      :test #js{:color "blue"}})

(defn- header []
  (fn []
    (let [id (rf/subscribe [doc-db/id])]
      [:header @id])))

(defn- form []
  [:form
   [:a {:href "/"}
    "go to home"]])

(defstyled testc styles
  [{:keys [classes x] :as props}]
  [:p
   {:class-name (.-test classes)}
   (str classes " " x)
   "test"])

(defn document-panel
  [{:keys [classes] :as props}]
  [mui/paper
   [header]
   [testc {:x "qwe"}]
   [:main
    [form]]])
