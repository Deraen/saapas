(set-env!
  ; Test path can be included here as source-files are not included in JAR
  ; Just be careful to not AOT them
  :source-paths #{"src/cljs" "src/less" "src/scss" "test/clj" "test/cljs"}
  :resource-paths #{"src/clj" "src/cljc"}
  :dependencies '[[org.clojure/clojure    "1.9.0-alpha13"]
                  [org.clojure/clojurescript "1.9.229"]

                  [boot/core              "2.6.0"      :scope "test"]
                  [adzerk/boot-cljs       "1.7.228-1"  :scope "test"]
                  [adzerk/boot-cljs-repl  "0.3.3"      :scope "test"]
                  [crisptrutski/boot-cljs-test "0.2.2-SNAPSHOT" :scope "test"]
                  [com.cemerick/piggieback "0.2.1"     :scope "test"]
                  [weasel                 "0.7.0"      :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12"    :scope "test"]
                  [adzerk/boot-reload     "0.4.13-SNAPSHOT"     :scope "test"]
                  [metosin/boot-alt-test  "0.1.2"      :scope "test"]
                  [deraen/boot-less       "0.5.1-SNAPSHOT"      :scope "test"]
                  ;; For boot-less
                  [org.slf4j/slf4j-nop    "1.7.21"     :scope "test"]
                  [deraen/boot-sass       "0.3.0-SNAPSHOT"      :scope "test"]

                  ; Backend
                  [http-kit "2.2.0"]
                  [org.clojure/tools.namespace "0.3.0-alpha3"]
                  [reloaded.repl "0.2.3"]
                  [com.stuartsierra/component "0.3.1"]
                  [metosin/ring-http-response "0.8.0"]
                  [ring "1.5.0"]
                  [compojure "1.5.1"]
                  [hiccup "1.0.5"]

                  ; Frontend
                  [reagent "0.6.0"]
                  [binaryage/devtools "0.8.2"]

                  ; LESS
                  [org.webjars/bootstrap "3.3.6"]
                  ; SASS
                  [org.webjars.bower/bootstrap "4.0.0-alpha" :exclusions [org.webjars.bower/jquery]]])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl repl-env]]
  '[adzerk.boot-reload    :refer [reload]]
  '[metosin.boot-alt-test  :refer [alt-test]]
  '[deraen.boot-less      :refer [less]]
  '[deraen.boot-sass      :refer [sass]]
  '[crisptrutski.boot-cljs-test :refer [test-cljs prep-cljs-tests run-cljs-tests]]
  '[backend.boot          :refer [start-app]]
  '[reloaded.repl         :refer [go reset start stop system]])

(task-options!
  pom {:project 'saapas
       :version "0.1.0-SNAPSHOT"
       :description "Application template for Cljs/Om with live reloading, using Boot."
       :license {"The MIT License (MIT)" "http://opensource.org/licenses/mit-license.php"}}
  aot {:namespace #{'backend.main}}
  jar {:main 'backend.main}
  cljs {:source-map true}
  less {:source-map true}
  sass {:source-map true})

(deftask dev
  "Start the dev env..."
  [s speak           bool "Notify when build is done"
   p port       PORT int  "Port for web server"
   a use-sass        bool "Use Scss instead of less"
   t test-cljs       bool "Compile and run cljs tests"]
  (comp
    (watch)
    (reload :open-file "vim --servername saapas --remote-silent +norm%sG%s| %s"
            :ids #{"js/main"})
    (if use-sass
      (sass)
      (less))
    ; This starts a repl server with piggieback middleware
    (cljs-repl :ids #{"js/main"})
    (cljs :ids #{"js/main"})
    (start-app :port port)
    (if speak (boot.task.built-in/speak) identity)))

(deftask run-tests
  [a autotest bool "If no exception should be thrown when tests fail"]
  (comp
    (alt-test :fail (not autotest))
    ;; FIXME: This is not a good place to define which namespaces to test
    (test-cljs :namespaces #{"frontend.core-test"})))

(deftask autotest []
  (comp
    (watch)
    (run-tests :autotest true)))

(deftask package
  "Build the package"
  []
  (comp
    (less :compression true)
    (cljs :optimizations :advanced)
    (aot)
    (pom)
    (uber)
    (jar)
    (target)))
