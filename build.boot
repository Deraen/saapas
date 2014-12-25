(set-env!
  :source-paths #{"src/cljs" "src/cljx" "src/less"}
  :resource-paths #{"src/clj"}
  :dependencies '[[adzerk/boot-cljs       "0.0-2411-5"]
                  [adzerk/boot-cljs-repl  "0.1.7"]
                  [adzerk/boot-reload     "0.2.1"]
                  [deraen/boot-cljx       "0.2.1"]
                  [deraen/boot-less       "0.1.1"]
                  [cljsjs/boot-cljsjs     "0.3.0"]

                  [org.clojure/clojure "1.6.0"]
                  [org.clojure/tools.namespace "0.2.8"]
                  [ring "1.3.1"]
                  [metosin/ring-http-response "0.5.2"]
                  [compojure "1.2.1"]
                  [om "0.8.0-beta5" :exclusions [com.facebook/react]]
                  [cljsjs/react "0.12.2-1"]
                  [http-kit "2.1.19"]
                  [org.webjars/bootstrap "3.3.1"]])

(require
  '[adzerk.boot-cljs      :refer :all]
  '[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload    :refer :all]
  '[cljsjs.app            :refer :all]
  '[deraen.boot-cljx      :refer :all]
  '[deraen.boot-less      :refer :all]
  '[saapas.boot           :refer :all])

(task-options!
  pom {:project 'saapas
       :version "0.1.0-SNAPSHOT"
       :description "Application template for Cljs/Om with live reloading, using Boot."
       :license {:name "The MIT License (MIT)"
                 :url "http://opensource.org/licenses/mit-license.php"}}
  aot {:namespace #{'saapas.main}}
  jar {:main 'saapas.main}
  cljs {:output-to "public/main.js"
        :source-map true})

(deftask dev
  "Start the dev env..."
  []
  (comp
    (from-cljsjs)
    (watch)
    ; Should be before cljs so the generated code is picked up
    (reload :on-jsload 'saapas.core/main)
    (less)
    (cljx)
    ; This starts a normal repls with piggieback middleware
    (cljs-repl)
    (cljs :optimizations :none :unified-mode true)
    (start-app)))

(deftask package
  "Build the package"
  []
  (comp
    (from-cljsjs :profile :production)
    (less)
    (cljx)
    (cljs :optimizations :advanced)
    (aot)
    (pom)
    (uber)
    (jar)))
