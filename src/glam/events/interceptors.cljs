(ns glam.events.interceptors
  (:require
   [re-frame.core :as rf]
   [glam.db.core :as glam-db]
   ))

(def check-db-spec
  (rf/->interceptor
   :id :check-db-spec
   :after (fn [context]
            (when-not (-> context
                          :effects
                          :db
                          glam-db/valid-db?)
              (js/alert "Invalid DB!"))
            context)))
