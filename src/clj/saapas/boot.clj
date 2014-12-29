(ns saapas.boot
  {:boot/export-tasks true}
  (:require
    [boot.core :refer :all]
    [boot.pod :as pod]
    [clojure.string :a s]
    [clojure.java.io :as io]
    [saapas.main :refer :all]))

(deftask start-app
  "Start the ring app"
  []
  (let [done (atom false)]
    (with-post-wrap fileset
      (when-not @done
        (go)
        (reset! done true))
      fileset)))
