(ns app.db
  (:require [datascript.core :as d]))

(def conn (d/create-conn {}))

(d/transact! conn [{:task/id    1
                    :task/label "Add datascript support"
                    :task/done? true}
                   {:task/id 2
                    :task/label "Proceed with new features"
                    :task/done? false}])

(defn get-task [id]
  (ffirst (d/q '[:find (pull ?e [:*])
                 :in $ ?id
                 :where [?e :task/id ?id]]
               @conn
               id)))

(defn delete-task [id]
  (d/transact! conn [[:db.fn/retractEntity id]]))

(defn task-id-list []
  (d/q '[:find ?e
         :where [?e :task/id]]
       @conn))

(comment
  (def id 1))
