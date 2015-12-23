(set-env!
  ; Test path can be included here as source-files are not included in JAR
  ; Just be careful to not AOT them
  :source-paths #{"src/cljs" "src/less" "src/scss" "test/clj"}
  :resource-paths #{"src/clj" "src/cljc"}
  :dependencies '[[org.clojure/clojure    "1.7.0"]
                  [org.clojure/clojurescript "1.7.170"]

                  [boot/core              "2.5.2"      :scope "test"]
                  [adzerk/boot-cljs       "1.7.170-3"  :scope "test"]
                  [adzerk/boot-cljs-repl  "0.3.0"      :scope "test"]
                  [com.cemerick/piggieback "0.2.1"     :scope "test"]
                  [weasel                 "0.7.0"      :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12"    :scope "test"]
                  [adzerk/boot-reload     "0.4.2"      :scope "test"]
                  [adzerk/boot-test       "1.0.7"      :scope "test"]
                  [deraen/boot-less       "0.4.4"      :scope "test"]
                  ;; For boot-less
                  [org.slf4j/slf4j-nop    "1.7.13"     :scope "test"]
                  [deraen/boot-sass       "0.1.1"      :scope "test"]
                  [deraen/boot-ctn        "0.1.0"      :scope "test"]

                  ; Backend
                  [http-kit "2.1.19"]
                  [org.clojure/tools.namespace "0.2.11"]
                  [reloaded.repl "0.2.1"]
                  [com.stuartsierra/component "0.3.1"]
                  [metosin/ring-http-response "0.6.5"]
                  [prismatic/om-tools "0.4.0"]
                  [prismatic/plumbing "0.5.2"]
                  [prismatic/schema "1.0.4"]
                  [ring "1.4.0"]
                  [compojure "1.4.0"]
                  [hiccup "1.0.5"]

                  ; Frontend
                  [org.omcljs/om "0.8.8"]
                  [sablono "0.3.6"]

                  ; LESS
                  [org.webjars/bootstrap "3.3.6"]
                  ; SASS
                  [org.webjars.bower/bootstrap "4.0.0-alpha" :exclusions [org.webjars.bower/jquery]]])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl repl-env]]
  '[adzerk.boot-reload    :refer [reload]]
  '[adzerk.boot-test      :refer [test]]
  '[deraen.boot-less      :refer [less]]
  '[deraen.boot-sass      :refer [sass]]
  '[deraen.boot-ctn       :refer [init-ctn!]]
  '[backend.boot          :refer [start-app]]
  '[reloaded.repl         :refer [go reset start stop system]])

; Watch boot temp dirs
(init-ctn!)

(task-options!
  pom {:project 'saapas
       :version "0.1.0-SNAPSHOT"
       :description "Application template for Cljs/Om with live reloading, using Boot."
       :license {"The MIT License (MIT)" "http://opensource.org/licenses/mit-license.php"}}
  aot {:namespace #{'backend.main}}
  jar {:main 'backend.main}
  cljs {:source-map true}
  less {:source-map true})

(deftask dev
  "Start the dev env..."
  [s speak           bool "Notify when build is done"
   p port       PORT int  "Port for web server"
   a use-sass        bool "Use Scss instead of less"]
  (comp
    (watch)
    (if use-sass
      (sass)
      (less))
    (reload :open-file "vim --servername saapas --remote-silent +norm%sG%s| %s"
            :ids #{"js/main"})
    ; This starts a repl server with piggieback middleware
    (cljs-repl :ids #{"js/main"})
    (cljs :ids #{"js/main"})
    (start-app :port port)
    (if speak (boot.task.built-in/speak) identity)))

(deftask run-tests []
  (test))

(deftask autotest []
  (comp
    (watch)
    (run-tests)))

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
