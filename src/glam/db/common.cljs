(ns glam.db.common
  (:require [re-frame.core :as rf]))

(defn reg-simple-sub
  ([key]
   (rf/reg-sub
    key
    (fn [db _]
      (key db))))
  ([key path]
   (rf/reg-sub
    key
    (fn [db _]
      (get-in db path)))))
