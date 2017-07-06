(ns backend.server
  (:require [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources files]]
            [compojure.handler :refer [api]]
            [ring.util.response :refer [redirect]]
            [ring.util.http-response :refer :all]
            [org.httpkit.server :refer [run-server]]
            [backend.index :refer [index-page test-page]]))

(defroutes routes
  (if (.exists (io/file "dev-output/js"))
    (files "/js" {:root "dev-output/js"})
    (resources "/js" {:root "js"}))

  (if (.exists (io/file "dev-output/css"))
    (files "/css" {:root "dev-output/css"})
    (resources "/css" {:root "css"}))

  (GET "/" []
    ; Use (resource-response "index.html") to serve index.html from classpath
    (-> (ok index-page) (content-type "text/html")))
  (GET "/test" []
    (-> (ok test-page) (content-type "text/html"))))

(defrecord HttpKit [port reload reload-dirs]
  component/Lifecycle
  (start [this]
    (let [port (or port 10555)]
      (println (str "Starting web server on http://localhost:" port))
      (assoc this :http-kit (run-server #'backend.server/routes
                                        {:port port :join? false}))))
  (stop [this]
    (if-let [http-kit (:http-kit this)]
      (http-kit))
    (assoc this :http-kit nil)))

(defn new-system [opts]
  (component/system-map
    :http-kit (map->HttpKit opts)))
