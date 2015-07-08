(set-env!
  :source-paths #{"src/cljs" "src/less"}
  :resource-paths #{"src/clj" "src/cljc"}
  :dependencies '[[org.clojure/clojure    "1.7.0"]
                  [org.clojure/clojurescript "0.0-3308"]
                  [adzerk/boot-cljs       "0.0-3308-0" :scope "test"]
                  [adzerk/boot-cljs-repl  "0.1.10-SNAPSHOT" :scope "test"]
                  [adzerk/boot-reload     "0.3.1"      :scope "test"]
                  [deraen/boot-less       "0.4.0"      :scope "test"]

                  ; Backend
                  [http-kit "2.1.19"]
                  [org.clojure/tools.namespace "0.2.10"]
                  [metosin/ring-http-response "0.6.3"]
                  [prismatic/om-tools "0.3.11"]
                  [prismatic/plumbing "0.4.4"]
                  [prismatic/schema "0.4.3"]
                  [ring "1.3.2"]
                  [compojure "1.3.4"]
                  [hiccup "1.0.5"]

                  ; Frontend
                  [org.omcljs/om "0.8.8"]
                  [sablono "0.3.4"]

                  [org.webjars/bootstrap "3.3.4"]])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl repl-env]]
  '[adzerk.boot-reload    :refer [reload]]
  '[deraen.boot-less      :refer [less]]
  '[saapas.boot           :refer [start-app]])

(task-options!
  pom {:project 'saapas
       :version "0.1.0-SNAPSHOT"
       :description "Application template for Cljs/Om with live reloading, using Boot."
       :license {"The MIT License (MIT)" "http://opensource.org/licenses/mit-license.php"}}
  aot {:namespace #{'saapas.main}}
  jar {:main 'saapas.main}
  cljs {:source-map true}
  less {:source-map true})

(deftask dev
  "Start the dev env..."
  [s speak           bool "Notify when build is done"
   r clj-reload      bool "Use r.m.reload to reload changed clj namespaces on each request"
   p port       PORT int  "Port for web server"]
  (comp
    (watch)
    ; Should be before cljs so the generated code is picked up
    ; FIXME: Shouldn't matter, file is created in pre-wrap?
    (reload :on-jsload 'saapas.core/start!)
    (less)
    ; This starts a normal repls with piggieback middleware
    (cljs-repl)
    (cljs :optimizations :none)
    (if speak (boot.task.built-in/speak) identity)
    (start-app :port port :reload clj-reload)))

(deftask package
  "Build the package"
  []
  (comp
    (less :compression true)
    (cljs :optimizations :advanced)
    (aot)
    (pom)
    (uber)
    (jar)))
