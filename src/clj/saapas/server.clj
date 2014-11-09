(ns saapas.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [ring.util.response :refer [redirect]]
            [org.httpkit.server :refer [run-server]]))

(defroutes routes
  (resources "/")
  ; FIXME: https://github.com/adzerk/boot-cljs/issues/4
  ; src="../public/out/goog/base.js"
  (resources "/public")
  (resources "/react" {:root "react"})
  (GET "/" []
    (redirect "/index.html")))

; FIXME:
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
