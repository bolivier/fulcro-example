(ns app.client
  (:require
   [app.ui :as ui]
   [app.application :refer [app]]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.data-fetch :as df]))

(defn ^:export init []
  (df/load! app :friends ui/PersonList)
  #_(df/load! app :enemies ui/PersonList)
  (df/load! app :todos ui/TodoList)
  (app/mount! app ui/Root "app")
  (js/console.log "Loaded"))

(defn ^:export refresh []
  (app/mount! app ui/Root "app")

  (js/console.log "Hot Reload"))
