(ns app.db
  (:require [datascript.core :as d]))

(def conn (d/create-conn {}))

(defn get-task [id]
  (ffirst (d/q '[:find (pull ?e [:*])
                 :in $ ?id
                 :where [?e :task/id ?id]]
               @conn
               id)))

(defn delete-task [id]
  (d/transact! conn [[:db.fn/retractEntity id]]))

(defn all-task-list []
  (d/q '[:find ?e
         :where [?e :task/id]]
       @conn))

(defn task-lists []
  (d/q '[:find (pull ?e [:*])
         :where [?e :task-list/id]]
       @conn))

(defn get-task-list [id]
  (d/q
   '[:find (pull ?e [:*])
     :in $ ?id
     :where [?e :task-list/id ?id]]
   @conn
   id))

(comment
  (def id 1)

  (defn seed-tasks []
    (d/transact! conn [{:task/id    1
                        :task/label "Add datascript support"
                        :task/done? true}
                       {:task/id 2
                        :task/label "Proceed with new features"
                        :task/done? false}]))

  (defn seed-task-lists []
    (d/transact! conn [{:task-list/id 1
                        :task-list/name "Fulcro Project"
                        :task-list/tasks [1 2]}]))

  (do
    (seed-tasks)
    (seed-task-lists)
    nil)
  nil)
