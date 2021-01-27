(ns user
  (:require
   [mount.core :as mount]
   [app.server]
   [app.resolvers]
   [shadow.cljs.devtools.api :as shadow]
   [shadow.cljs.devtools.server :as server]
   [app.parser]))

(defn start []
  (mount/start))

(defn restart []
  (mount/stop)
  (mount/start))

(comment
  (restart)
  (do
    (server/start!)
    (shadow/watch :main))
  )
