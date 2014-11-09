(task-options!
  pom [:project 'saapas
       :version "0.1.0-SNAPSHOT"
       :description "Application template for Cljs/Om with live reloading, using Boot."
       :license {:name "The MIT License (MIT)"
                 :url "http://opensource.org/licenses/mit-license.php"}])

(set-env!
  :src-paths #{"src/clj" "src/cljs" "src/cljx"}
  :rsc-paths #{"resources"}
  :dependencies '[[adzerk/boot-cljs "0.0-2371-23"]
                  [adzerk/boot-cljs-repl  "0.1.5"]
                  [adzerk/boot-reload     "0.1.4"]
                  [deraen/boot-cljx       "0.1.0-SNAPSHOT"]

                  [ring "1.3.1"]
                  [compojure "1.2.0"]
                  [om "0.7.3"]
                  [http-kit "2.1.19"]
                  [prismatic/om-tools "0.3.3"]])

(require
  '[adzerk.boot-cljs      :refer :all]
  '[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload    :refer :all]
  '[deraen.boot-cljx      :refer :all])

(require 'saapas.server)

(deftask dev
  "Start the dev env..."
  []
  (comp
    (watch)
    ; This starts a normal repls with piggieback middleware
    (cljs-repl)
    (cljx)
    (cljs :output-to "public/main.js"
          :source-map true
          :unified true
          :optimizations :none)
    (reload :on-jsload 'saapas.core/main)))

(deftask dev-repl
  "Connect to the repl started by the dev task."
  []
  (repl :client true))

; vim: ft=clojure:
