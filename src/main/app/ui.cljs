(ns app.ui
  (:require
   [app.mutations :as api]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]))

(defsc Person [this {:person/keys [name age id] :as props} {:keys [onDelete]}]
  {:ident         (fn [] [:person/id (:person/id props)])
   :query         [:person/id :person/name :person/age]
   :initial-state (fn [{:keys [name age id]}]
                    {:person/id   id
                     :person/name name
                     :person/age  age})}
  (dom/li (dom/h5 (str name "(age: " age ")")
                  (dom/button {:onClick #(onDelete id)} "X"))))

(def ui-person (comp/computed-factory Person {:keyfn :person/name}))

(defsc PersonList [this {:list/keys [id label people] :as props}]
  {:ident (fn [] [:list/id (:list/id props)])
   :query [:list/id :list/label {:list/people (comp/get-query Person)}]
   :initial-state
   (fn [{:keys [id label]}]
     {:list/id     id
      :list/label  label
      :list/people (if (= label "Friends")
                     [(comp/get-initial-state Person {:id 1 :name "Sally" :age 32})
                      (comp/get-initial-state Person {:id 2 :name "Joe" :age 22})]
                     [(comp/get-initial-state Person {:id 3 :name "Fred" :age 11})
                      (comp/get-initial-state Person {:id 4 :name "Bobby" :age 55})])})}
  (let [delete-person (fn [person-id]
                        (comp/transact! this [(api/delete-person {:person/id person-id
                                                                  :list/id id})]))]
    (dom/div
     (dom/h4 label)
     (dom/ul
      (map
       #(ui-person % {:onDelete delete-person})
       people)))))

(def ui-person-list (comp/factory PersonList))

(defsc Root [this {:keys [friends enemies]}]
  {:query [{:friends (comp/get-query PersonList)}
           {:enemies (comp/get-query PersonList)}]
   :initial-state (fn [params] {:friends (comp/get-initial-state PersonList {:id :friends :label "Friends"})
                                :enemies (comp/get-initial-state PersonList {:id :enemies :label "Enemies"})})}
  (dom/div
   (ui-person-list friends)
   (ui-person-list enemies)))
