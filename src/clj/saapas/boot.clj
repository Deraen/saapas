(ns saapas.boot
  {:boot/export-tasks true}
  (:require
    [boot.core :refer :all]
    [boot.pod :as pod]
    [clojure.string :a s]
    [clojure.java.io :as io]
    [saapas.main :refer :all]))

(deftask start-app []
  (let [f (delay (go))]
    (with-post-wrap fileset @f fileset)))
