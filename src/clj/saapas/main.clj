(ns saapas.main
  {:boot/export-tasks true}
  (:require [clojure.tools.namespace.repl :as ns-tools]
            [boot.core :as boot]
            [boot.task-helpers :refer [once]]))

(ns-tools/disable-reload!)

(defonce system (atom nil))

(defn init []
  (require 'saapas.server)
  (swap! system (constantly {})))

(defn start []
  (swap! system (resolve 'saapas.server/start)))

(defn stop []
  (swap! system (resolve 'saapas.server/stop)))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (ns-tools/refresh :after 'saapas.main/go))

(boot/deftask start-app
  "Start the ring app"
  []
  (once
    (boot/with-post-wrap
      (go))))
