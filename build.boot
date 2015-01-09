(set-env!
  :source-paths #{"src/cljs" "src/cljx" "src/less"}
  :resource-paths #{"src/clj"}
  :dependencies '[[adzerk/boot-cljs       "0.0-2629-1" :scope "test"]
                  [adzerk/boot-cljs-repl  "0.1.7"      :scope "test"]
                  [adzerk/boot-reload     "0.2.3"      :scope "test"]
                  [deraen/boot-cljx       "0.2.1"      :scope "test"]
                  [deraen/boot-less       "0.2.0"      :scope "test"]
                  [cljsjs/boot-cljsjs     "0.4.0"      :scope "test"]

                  [org.clojure/clojure "1.6.0"]
                  [org.clojure/tools.namespace "0.2.8"]
                  [ring "1.3.2"]
                  [metosin/ring-http-response "0.5.2"]
                  [compojure "1.3.1"]
                  [om "0.8.0-rc1" :exclusions [com.facebook/react]]
                  [cljsjs/react "0.12.2-3"]
                  [http-kit "2.1.19"]
                  [org.webjars/bootstrap "3.3.1"]])

(require
  '[adzerk.boot-cljs      :refer :all]
  '[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload    :refer :all]
  '[cljsjs.boot-cljsjs    :refer :all]
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
        :source-map true}
  less {:source-map true})

(deftask dev
  "Start the dev env..."
  [s speak bool "Notify when build is done"]
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
    (if speak (boot.task.built-in/speak) identity)
    (start-app)))

(deftask package
  "Build the package"
  []
  (comp
    (from-cljsjs :profile :production)
    (less :compression true)
    (cljx)
    (cljs :optimizations :advanced)
    (aot)
    (pom)
    (uber)
    (jar)))
