(ns ^{:doc "A re-frame wrapper around PouchDB"}
    glam.events.pouchdb
  (:require [re-frame.core :as rf]
            [glam.db.core :as glam-db]
            ["pouchdb" :as PouchDB]))

;; keep a private list of dbs we know about in a var
(def ^:private registry (atom {}))
(defn- create-db-object!
  "Make and register a PouchDB JS object"
  [db-name]
  (let [db (PouchDB. db-name)]
    (swap! registry assoc db-name db)
    db))

;; this will store information about other databases
(def ^:private glam-db-name "__glam__")
(def ^:private project-list-doc-id "projectlist")
(def ^:private glam-db (create-db-object! glam-db-name))

;; Make a re-frame effect for pouchdb, which will let re-frame events kick off
;; those loathsome, vexing side-effects
(rf/reg-fx
 :pouchdb
 (fn [{:keys [method args db-name]
      :as request}]

   ;; TODO: is this necessary?
   (when-not (#{"allDocs" "bulkDocs" "post" "get" "put"} method)
     (throw
      (js/Error. (str "Unsupported PouchDB method: " method))))

   ;; find the DB ref or create it, then invoke the method
   (let [db (or (get @registry db-name)
                (create-db-object! db-name))
         combined-args (clj->js
                        (into [db method] args))]
     (js/console.log "Attempting to invoke:" combined-args)
     (apply js-invoke combined-args))))

;; wrappers for generic PDB functions ---------------------------------------------------
(defn- callback-dispatcher
  "We commonly want to have a callback function that dispatches one
  event or another depending on whether we get a failure."
  [success-event failure-event]
  (fn [err res]
    (if err
      (rf/dispatch [failure-event (clj->js err :keywordize-keys true)])
      (rf/dispatch [success-event (clj->js res :keywordize-keys true)]))))

(def unexpected-pdb-error ::unexpected-pdb-error)
(rf/reg-event-db
 unexpected-pdb-error
 (fn [db [_ err]]
   (when goog.DEBUG
     (js/Error. "Unexpected PouchDB error: " err))))

(def put-doc ::put-doc)
(rf/reg-event-fx
 put-doc
 (fn [{:keys [db]} [_ doc opts success-event failure-event]]
   (let [db-name (glam-db/active-project db)]
     {:db db
      :pouchdb {:method "put"
                :db-name db-name
                :args [(clj->js doc)
                       opts
                       (callback-dispatcher success-event failure-event)]}})))

(def get-doc ::get-doc)
(rf/reg-event-fx
 get-doc
 (fn [{:keys [db]} [_ id opts success-event failure-event]]
   (let [db-name (glam-db/active-project db)]
     {:db db
      :pouchdb {:method "get"
                :db-name db-name
                :args [id
                       opts
                       (callback-dispatcher success-event failure-event)]}})))

;; some glam-specific events
(def load-projects ::load-projects)
(rf/reg-event-fx
 load-projects
 (fn [{:keys [db]} [_ success-event failure-event]]
   {:db db
    :pouchdb
    {:method "get"
     :db-name glam-db-name
     :args [project-list-doc-id
            (fn [err ^js/Object res]
              (cond
                ;; the special document hasn't been made yet--make it
                (and err (= (.-status err) 404))
                (rf/dispatch [put-doc #js{:_id project-list-doc-id
                                          :projects #js[]}
                              {}
                              load-projects
                              unexpected-pdb-error])
                ;; it failed for some reason--use the supplied event
                err (rf/dispatch [failure-event err])
                ;; we got it--return the list of projects and init their pouchdb objects
                :else (let [projects (js->clj (.-projects res))]
                        (rf/dispatch [success-event projects]))))]}}))
