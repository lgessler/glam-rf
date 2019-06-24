(ns glam.db.document.core
  (:require [cljs.spec.alpha :as s]
            [re-frame.core :as rf]
            [glam.db.common :refer [reg-simple-sub]])
  (:require-macros [cljs.spec.alpha :as s]
                   [glam.db.common :refer [defdbkey]]))


(defdbkey id string?)
(defdbkey active-document (s/spec
                           (s/or
                            :doc (s/keys :req [id])
                            :empty-doc (s/keys :req []))))
(def base-path [active-document])

(reg-simple-sub id (conj base-path id))
