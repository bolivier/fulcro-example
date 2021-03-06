(ns app.mutations
  (:require [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
            [com.fulcrologic.fulcro.algorithms.merge :as merge]))

(defmutation delete-person
  "Mutation: delete the person with `name` from the list with `list-name`"
  [{list-id   :list/id
    person-id :person/id}]
  (action [{:keys [state]}]
          (swap! state
                 merge/remove-ident*
                 [:person/id person-id]
                 [:list/id list-id :list/people]))
  (remote [env] true))

(defmutation delete-task
  "Mutation to delete a task item"
  [{task-id :task/id}]
  (action [{:keys [state]}]
          (swap!
           state
           update :tasks (fn [tasks]
                           (remove
                            #(= (:task/id %) task-id)
                            tasks))))
  (remote [env] true))
