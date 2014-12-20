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

(deftask add-js-lib
  "Add non-boot ready js files to the fileset"
  [p path PATH str "The path of file in classpath"
   t target TARGET str "Target path"
   x package bool "Don't include files in result"]
  (let [tmp (temp-dir!)
        state (atom nil)]
    (with-pre-wrap
      fileset
      (when-not @state
        (let [f (io/resource path)]
          (if f
            (pod/copy-url f (io/file tmp target))
            (throw (str "File " path " not found!")))))
      (-> fileset ((if package add-source add-resource) tmp) commit!))))
