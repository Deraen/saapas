(ns saapas.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [ring.util.response :refer [redirect]]
            [ring.util.http-response :refer :all]
            [ring.middleware.reload :refer [wrap-reload]]
            [org.httpkit.server :refer [run-server]]
            [saapas.index :refer [index-page]]))

(defroutes routes
  (resources "/" {:root "public"})

  (GET "/" []
    ; Use (resource-response "index.html") to serve index.html from classpath
    (-> (ok index-page) (content-type "text/html"))))

(defn stop
  [{:keys [http-kit] :as ctx}]
  (when http-kit
    (http-kit))
  {})

(defn start
  [ctx & [{:keys [port reload reload-dirs]}]]
  (let [handler (cond-> #'saapas.server/routes
                  reload (wrap-reload {:dirs (seq reload-dirs)}))
        port (Integer. (or port 10555))
        http-kit (run-server handler {:port port :join? false})]
    (println "Starting web server on port" port)
    {:http-kit http-kit}))
