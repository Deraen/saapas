(ns saapas.server
  (:require [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [ring.util.response :refer [redirect]]
            [ring.util.http-response :refer :all]
            [ring.middleware.reload :refer [wrap-reload]]
            [org.httpkit.server :refer [run-server]]
            [saapas.index :refer [index-page]]))

(defroutes routes
  (resources "/js" {:root "js"})
  (resources "/css" {:root "css"})

  (GET "/" []
    ; Use (resource-response "index.html") to serve index.html from classpath
    (-> (ok index-page) (content-type "text/html"))))

(defrecord HttpKit [port reload reload-dirs]
  component/Lifecycle
  (start [this]
    (let [port (or port 10555)]
      (println (str "Starting web server on http://localhost:" port))
      (assoc this :http-kit (run-server (cond->
                                          #'saapas.server/routes
                                          reload (wrap-reload {:dirs (seq reload-dirs)}))
                                        {:port port :join? false}))))
  (stop [this]
    (if-let [http-kit (:http-kit this)]
      (http-kit))
    (assoc this :http-kit nil)))

(defn new-system [opts]
  (component/system-map
    :http-kit (map->HttpKit opts)))
