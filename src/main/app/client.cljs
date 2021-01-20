(ns app.client
  (:require
   [app.ui :refer [Root]]
   [app.application :refer [app]]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom :as dom]))

(defn ^:export init []
  (app/mount! app Root "app")
  (js/console.log "Loaded"))

(defn ^:export refresh []
  (app/mount! app Root "app")
  #_(comp/refresh-dynamic-queries! app)
  (js/console.log "Hot Reload"))
