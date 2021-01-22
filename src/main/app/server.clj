(ns app.server
  (:require [app.parser :refer [api-parser]]
            [com.fulcrologic.fulcro.server.api-middleware :as server]
            [org.httpkit.server :as http]
            [mount.core :refer [defstate]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.resource :refer [wrap-resource]]))

(def ^:private not-found-handler
  (fn [_]
    {:status 404
     :headers {"Content-Type" "text/plain"}
     :body "Not Found"}))

(def middleware
  (-> not-found-handler
      (server/wrap-api {:uri    "/api"
                        :parser api-parser})
      (server/wrap-transit-params)
      (server/wrap-transit-response)
      (wrap-resource "public")
      wrap-content-type))

(defstate server
  :start (http/run-server middleware {:port 3000})
  :stop (server))
