(ns saapas.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [ring.util.response :refer [redirect]]
            [org.httpkit.server :refer [run-server]]))

(defroutes routes
  (resources "/")
  ; FIXME: Boot-reload is using from URIs
  (resources "/public")
  (resources "/react" {:root "react"})
  (GET "/" []
    (redirect "/index.html")))

(defn stop
  [{:keys [http-kit] :as ctx}]
  (when http-kit
    (http-kit))
  {})

(defn start
  [ctx & [port]]
  (let [port (Integer. (or port 10555))
        http-kit (run-server #'saapas.server/routes {:port port :join? false})]
    (println "Starting web server on port" port)
    {:http-kit http-kit}))

(defn- main [& _]
  (start {}))
