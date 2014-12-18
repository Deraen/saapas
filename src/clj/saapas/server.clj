(ns saapas.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [ring.util.response :refer [redirect]]
            [ring.util.http-response :refer :all]
            [org.httpkit.server :refer [run-server]]
            [saapas.index :refer [index-page]]))

(defroutes routes
  (resources "/" {:root "public"})
  ; FIXME: boot-cljs will provide reverse routing fn which we
  ; can use to generate proper urls
  (resources "/public" {:root "public"})
  (GET "/" []
    (ok index-page)))

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
