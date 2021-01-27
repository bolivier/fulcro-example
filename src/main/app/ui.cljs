(ns app.ui
  (:require
    [app.mutations :as api]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom]))

(defsc Person [this {:person/keys [id name age] :as props} {:keys [onDelete]}]
  {:query [:person/id :person/name :person/age]
   :ident :person/id}
  (dom/li
    (dom/h5 (str name " (age: " age ")") (dom/button {:onClick #(onDelete id)} "X"))))

(def ui-person (comp/factory Person {:keyfn :person/id}))

(defsc PersonList [this {:list/keys [id label people] :as props}]
  {:query [:list/id :list/label {:list/people (comp/get-query Person)}]
   :ident :list/id}
  (let [delete-person (fn [person-id] (comp/transact! this [(api/delete-person {:list/id id :person/id person-id})]))] ; (2)
    (dom/div
     (dom/h3 label)
     (when people
      (dom/ul
       (map #(ui-person (comp/computed % {:onDelete delete-person})) people))))))

(def ui-person-list (comp/factory PersonList))

(defsc Task [this {:task/keys [label id]}]
  {:query [:task/label :task/id :task/done?]
   :ident :task/id}
  (dom/li
   (dom/span label)
   (dom/button {:onClick (fn []
                           (comp/transact! this [(api/delete-task {:task/id id})]))}
               "X")))

(def ui-task (comp/factory Task {:keyfn :task/id}))

(defsc TaskList [this tasks]
  {:query (fn [] (comp/get-query Task))}
  (map ui-task tasks))

(def ui-task-list (comp/factory TaskList))

(defsc Root [this {:keys [tasks]}]
  {:query         [{:tasks (comp/get-query TaskList)}
                   {:friends (comp/get-query PersonList)}]
   :initial-state {}}
  (dom/div
   (ui-task-list tasks)))
