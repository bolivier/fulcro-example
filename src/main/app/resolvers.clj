(ns app.resolvers
  (:require [com.wsscode.pathom.core :as p]
            [taoensso.timbre :as log]
            [mount.core :refer [defstate]]
            [com.wsscode.pathom.connect :as pc :refer [defresolver]]
            [app.db :as db]))

(def people-table
  (atom
   {1 {:person/id 1 :person/name "Sally" :person/age 32}
    2 {:person/id 2 :person/name "Joe" :person/age 22}
    3 {:person/id 3 :person/name "Fred" :person/age 11}
    4 {:person/id 4 :person/name "Bobby" :person/age 55}}))

(def list-table
  (atom
   {:friends {:list/id     :friends
              :list/label  "Friends"
              :list/people [1 2]}
    :enemies {:list/id     :enemies
              :list/label  "Enemies"
              :list/people [4 3]}}))

(defresolver person-resolver [env {:person/keys [id]}]
  {::pc/input #{:person/id}
   ::pc/output [:person/name :person/age]}
  (get @people-table id))


(defn ->person-ident [id]
  {:person/id id})

(defresolver list-resolver [env {:list/keys [id]}]
  {::pc/input #{:list/id}
   ::pc/output [:list/label {:list/people [:person/id]}]}
  (when-let [list (get @list-table id)]
    (log/info "Resolving a list")
    (assoc list
           :list/people
           (mapv (fn [id] {:person/id id})
                 (:list/people list)))))

(defresolver friends-resolver [env input]
  {::pc/output [{:friends [:list/id]}]}
  {:friends {:list/id :friends}})

(defresolver enemies-resolver [env input]
  {::pc/output [{:enemies [:list/id]}]}
  {:enemies {:list/id :enemies}})

(def task-table
  (atom {1 #:task{:label "learn Fulcro" :id 1 :done? false}
         2 #:task{:label "populate frontend code" :id 2 :done? false }}))

(defresolver task-resolver [env {:task/keys [id]}]
  {::pc/input #{:task/id}
   ::pc/output [:task/label :task/id :task/done?]}
  (db/get-task id))

(defresolver task-list-resolver [env _]
  {::pc/output [{:tasks [:task/id]}]}
  {:tasks
   (mapv
    (fn [[id]] {:task/id id})
    (db/all-task-list))})

(defstate resolvers
  :start [person-resolver list-resolver enemies-resolver friends-resolver task-list-resolver task-resolver])

(comment
  @task-table
  nil)
