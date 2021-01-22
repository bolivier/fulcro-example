(ns user
  (:require
   [mount.core :as mount]
   [app.server]
   [shadow.cljs.devtools.api :as shadow]
   [shadow.cljs.devtools.server :as server]
   [app.parser]))


;; TODO: this does not work properly because it isn't loaded by default with
;; deps.edn
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
