(ns glam.events.common
  (:require
   [re-frame.core :as rf]))

(defn reg-simple-event-db
  [id setter]
  (rf/reg-event-db
   id
   (fn [db [_ v]]
     (setter db v))))
