;; This ns often needs to be compiled separately to work properly.  I'm not sure
;; why.

(ns app.parser
  (:require
   [mount.core :refer [defstate]]
   [app.resolvers]
   [app.mutations]
   [com.wsscode.pathom.core :as p]
   [com.wsscode.pathom.connect :as pc]
   [taoensso.timbre :as log]))

(defstate resolvers
  :start [app.resolvers/resolvers
          app.mutations/mutations])

(defstate pathom-parser
  :start (p/parser {::p/env     {::p/reader                 [p/map-reader
                                                             pc/reader2
                                                             pc/ident-reader
                                                             pc/index-reader]
                                 ::pc/mutation-join-globals [:tempids]}
                    ::p/mutate  pc/mutate
                    ::p/plugins [(pc/connect-plugin {::pc/register resolvers})
                                 p/error-handler-plugin]}))

(defstate api-parser
  :start (fn [query]
           (log/info "Process" query)
           (pathom-parser {} query)))

(comment
  (api-parser [{[:task-list/id 1] [:task-list/name
                                   {:task-list/tasks
                                    [:task/label
                                     :task/id]}]}])

  (api-parser [{[:task/id 1] [:task/id :task/done? :task/label]}])
  )
