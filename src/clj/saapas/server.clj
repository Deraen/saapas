(ns saapas.server
  (:require [clojure.java.io :as io]
            [saapas.dev :refer [is-dev? inject-devmode-html]]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [net.cgrand.enlive-html :refer [deftemplate]]
            [ring.middleware.reload :as reload]
            [org.httpkit.server :refer [run-server]]))

(deftemplate page
  (io/resource "index.html") [] [:body] (if is-dev? inject-devmode-html identity))

(defroutes routes
  ; Cljs
  (resources "/" {:root ""})
  ; Css
  (resources "/")
  (resources "/react" {:root "react"})
  (GET "/*" req (page)))

(defn run [& [port]]
  (defonce ^:private server
    (do
      (let [port (Integer. (or port 10555))]
        (print "Starting web server on port" port ".\n")
        (run-server #'saapas.server/routes
                    {:port port
                     :join? false}))))
  server)

(defn -main [& [port]]
  (run port))
