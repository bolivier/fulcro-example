(ns app.resolvers
  (:require [com.wsscode.pathom.core :as p]
            [taoensso.timbre :as log]
            [mount.core :refer [defstate]]
            [com.wsscode.pathom.connect :as pc :refer [defresolver]]))

;; TODO: I can't figure out why the UI isn't receiving the props.  The network
;; response shows that friends/enemies are :not-found by pathom.  However, I can
;; run the query on the api-parser and they work _just fine_.

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

(defstate resolvers
  :start [person-resolver list-resolver enemies-resolver friends-resolver])
