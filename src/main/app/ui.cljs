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

(defsc Todo [this {:todo/keys [label]}]
  {:query [:todo/label :todo/id :todo/done?]
   :ident :todo/id}
  (dom/li
   (dom/span label)))

(def ui-todo (comp/factory Todo {:keyfn :todo/id}))

(defsc TodoList [this todos]
  {:query (fn [] (comp/get-query Todo))}
  (map ui-todo todos))

(def ui-todo-list (comp/factory TodoList))

(defsc Root [this {:keys [todos]}]
  {:query         [{:todos (comp/get-query TodoList)}
                   {:friends (comp/get-query PersonList)}]
   :initial-state {}}
  (dom/div
   (ui-todo-list todos)))
