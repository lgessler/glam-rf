(ns glam.events.document.core
  (:require
   [re-frame.core :as rf]
   [glam.events.common :refer [reg-simple-event-db]]
   [glam.db.document.core :as doc-db]))

(reg-simple-event-db doc-db/text doc-db/set-text)
(reg-simple-event-db doc-db/rating doc-db/set-rating)
