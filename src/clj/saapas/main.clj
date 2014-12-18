(ns saapas.main
  (:require
    [clojure.tools.namespace.repl :as ns-tools])
  (:gen-class))

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

(defn -main [& args]
  (require 'saapas.server)
  ((resolve 'saapas.server/start) {}))
