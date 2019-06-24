(ns glam.db.common
  (:require [re-frame.core :as rf]))

(defn reg-simple-sub
  [id getter]
  (rf/reg-sub
   id
   (fn [db _]
     (getter db))))
