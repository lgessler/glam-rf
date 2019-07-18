(ns glam.db.document.core
  (:require [cljs.spec.alpha :as s]
            [re-frame.core :as rf]
            [glam.db.common :refer [reg-simple-sub]])
  (:require-macros [cljs.spec.alpha :as s]
                   [glam.db.common :refer [defdbkey]]))

(def base-path [:document])
(defdbkey id base-path string?)
(defdbkey text base-path string?)
(defdbkey rating base-path number?)
(defdbkey document [] (s/spec
                       (s/or
                        :doc (s/keys :req [id])
                        :empty-doc (s/keys :req []))))

(reg-simple-sub id get-id)
(reg-simple-sub text get-text)
(reg-simple-sub rating get-rating)
