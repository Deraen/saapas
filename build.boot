(task-options!
  pom [:project 'saapas
       :version "0.1.0-SNAPSHOT"
       :description "Application template for Cljs/Om with live reloading, using Boot."
       :license {:name "The MIT License (MIT)"
                 :url "http://opensource.org/licenses/mit-license.php"}])

(set-env!
  :src-paths #{"src/clj" "src/cljs" "src/cljx"
               ; Index.html etc.
               "resources"}
  :dependencies '[[adzerk/boot-cljs "0.0-2371-20"]
                  [adzerk/boot-cljs-repl  "0.1.5"]
                  [adzerk/boot-reload     "0.1.3"]
                  [deraen/boot-cljx       "0.1.0-SNAPSHOT"]

                  [ring "1.3.1"]
                  [compojure "1.2.0"]
                  [enlive "1.1.5"]
                  [om "0.7.3"]
                  [http-kit "2.1.19"]
                  [prismatic/om-tools "0.3.3"]])

(require
  '[adzerk.boot-cljs      :refer :all]
  '[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload    :refer :all]
  '[deraen.boot-cljx      :refer :all])

(deftask dev
  "Start the dev env..."
  []
  (comp
    (watch)
    ; This starts a normal repls with piggieback middleware
    (cljs-repl)
    (cljx)
    (cljs :source-map true
          :optimizations :none)
    (reload)))

(deftask dev-repl
  "Connect to the repl started by the dev task."
  []
  (repl :client true))

; vim: ft=clojure:
