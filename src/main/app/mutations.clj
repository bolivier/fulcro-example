(ns app.mutations
  (:require [app.resolvers :refer [list-table todo-table]]
            [com.wsscode.pathom.connect :as pc]
            [mount.core :refer [defstate]]
            [taoensso.timbre :as log]))

(pc/defmutation delete-person [env {list-id :list/id
                                    person-id :person/id}]
  {::pc/sym `delete-person}
  (log/info "Deleting person" person-id "from list" list-id)
  (swap! list-table update list-id update :list/people (fn [old-list]
                                                         (filterv #(not= person-id %)
                                                                  old-list))))
(pc/defmutation delete-todo [env {todo-id :todo/id}]
  {::pc/sym `delete-todo}
  (log/info "deleting todo with id " todo-id)
  (swap! todo-table dissoc todo-id))

(defstate mutations
  :start [delete-person delete-todo])
