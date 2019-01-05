(ns glam.views
  (:require
   [re-frame.core :as re-frame]
   [glam.subs :as subs]
   [reagent.core :as r]
   ))

(defn iframe []
  (let [zip-url (r/atom nil)]
    (fn []
      (js/console.log "re-render")
      [:div
       [:div
        [:input {:type "file"
                 :on-change
                 (fn [e]
                   (reset! zip-url (-> e
                                       .-target
                                       .-files
                                       (aget 0)
                                       (js/URL.createObjectURL))))}]]
       [:iframe {:src (str "/nohost/index.html?install=" @zip-url)
                 :style {:width "100%"
                         :height "700px"}}]])))

;; home

(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 (str "Hello from " @name ". This is the Home Page.")]

     [:div
      [:p "yo"]
      [:a {:href "/about"}
       "go to About Page"]]

     [iframe]
     ]))

;; about

(defn about-panel []
  [:div
   [:h1 "This is the About Page."]

   [:div
    [:a {:href "/"}
     "go to Home Page"]]])

;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
