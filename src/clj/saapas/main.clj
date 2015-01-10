(ns saapas.main
  (:require
    [clojure.tools.namespace.repl :as ns-tools]
    [clojure.java.io :as io])
  (:gen-class))

(ns-tools/disable-reload!)

(let [reload-dirs (->> (or (try
                             (require 'boot.core)
                             ((resolve 'boot.core/get-env) :directories)
                             (catch Exception _
                               nil))
                           (do (require 'clojure.tools.namespace.dir)
                               ((resolve 'clojure.tools.namespace.dir/dirs-on-classpath))))
                       (remove #(.exists (io/file % ".no-reload"))))]
  (apply ns-tools/set-refresh-dirs reload-dirs))

(defonce opts (atom {}))
(defonce system (atom nil))

(defn init []
  (require 'saapas.server)
  (swap! system (constantly {})))

(defn start []
  (swap! system (resolve 'saapas.server/start) @opts))

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
