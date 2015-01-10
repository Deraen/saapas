(ns saapas.server
  (:require [clojure.java.io :as io]
            [schema.core :as s]
            [compojure.api.sweet :refer :all]
            [compojure.route :refer [resources]]
            [ring.util.http-response :refer :all]
            [org.httpkit.server :refer [run-server]]
            [saapas.index :refer [index-page]]
            [saapas.domain :as domain]
            [saapas.service :as service]))

(defapi app
  (swagger-ui "/api-docs")
  (swagger-docs
    :title "foobar")

  (resources "/" {:root "public"})
  ; FIXME: boot rc6 should make these unnecessary
  (resources "/public" {:root "public"})
  (resources "/cljsjs" {:root "cljsjs"})

  (swaggered "links"
    (context "/api" []
      (GET* "/links" []
        :return [domain/Link]
        (ok (service/find-links)))
      (POST* "/links" []
        :return domain/Link
        :body [body domain/NewLink]
        (ok (service/insert-link body)))
      (POST* "/links/:id/vote" []
        :path-params [id :- s/Int]
        (ok (service/vote id)))))

  (GET* "/" []
    (ok index-page)))

(defn stop
  [{:keys [http-kit] :as ctx}]
  (when http-kit
    (http-kit))
  {})

(defn start
  [ctx & [{:keys [port]}]]
  (let [port (Integer. (or port 10555))
        http-kit (run-server #'saapas.server/app {:port port :join? false})]
    (println "Starting web server on port" port)
    {:http-kit http-kit}))
