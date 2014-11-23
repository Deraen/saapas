(task-options!
  pom [:project 'saapas
       :version "0.1.0-SNAPSHOT"
       :description "Application template for Cljs/Om with live reloading, using Boot."
       :license {:name "The MIT License (MIT)"
                 :url "http://opensource.org/licenses/mit-license.php"}
       :main 'saapas.server])

(set-env!
  :src-paths #{"src/clj" "src/cljs" "src/cljx"}
  :rsc-paths #{"resources"}
  :dependencies '[[adzerk/boot-cljs "0.0-2371-27"]
                  [adzerk/boot-cljs-repl  "0.1.6"]
                  [adzerk/boot-reload     "0.1.6"]
                  [deraen/boot-cljx       "0.1.0-SNAPSHOT"]

                  [org.clojure/tools.namespace "0.2.7"]
                  [ring "1.3.1"]
                  [compojure "1.2.1"]
                  [om "0.8.0-alpha2"]
                  [http-kit "2.1.19"]
                  [prismatic/om-tools "0.3.6"]])

(require
  '[adzerk.boot-cljs      :refer :all]
  '[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload    :refer :all]
  '[deraen.boot-cljx      :refer :all]
  '[saapas.main           :refer [start-app]]

  '[boot.core :as core]
  '[clojure.java.io :as io])

(task-options!
  cljs [:output-to "public/main.js"
        :source-map true
        :unified true])

(deftask rename-index-hack
  "This will go away"
  []
  (let [tgt-dir (core/mktgtdir!)]
    (core/with-pre-wrap
      (doseq [f (core/by-name ["index.tmp.html"] (core/all-files))]
        (let [out (io/file tgt-dir (core/relative-path (.getParentFile f)) "index.html")]
          (println "Copying" f "to" out)
          (io/make-parents out)
          (spit out (slurp f)))))))

(deftask dev
  "Start the dev env..."
  []
  (comp
    (watch)
    ; This starts a normal repls with piggieback middleware
    (cljs-repl)
    (cljx)
    (start-app)
    (cljs :optimizations :none)
    ; FIXME: Because there will be multiple index.htmls on
    ; classpath... will be fixed by: https://github.com/boot-clj/boot/tree/tempdirs
    (rename-index-hack)
    (reload :on-jsload 'saapas.core/main)))

(deftask dev-repl
  "Connect to the repl started by the dev task."
  []
  (repl :client true))

(deftask package
  "Build the package"
  []
  (comp
    (cljx)
    (cljs :optimizations :advanced)
    (rename-index-hack)
    (pom)
    (add-src)
    (uber)
    (jar)))

; vim: ft=clojure:
