(ns glam.db.document.core
  (:require [cljs.spec.alpha :as s]
            [re-frame.core :as rf])
  (:require-macros [cljs.spec.alpha :as s]
                   [glam.db.common :refer [defdbkey]]))

(defdbkey id string?)
(defdbkey active-document (s/spec
                           (s/or
                            :doc (s/keys :req [id])
                            :empty-doc (s/keys :req []))))

(rf/reg-sub
 id
 (fn [db _]
   (id db)))
