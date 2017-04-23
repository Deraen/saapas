(set-env!
  ; Test path can be included here as source-files are not included in JAR
  ; Just be careful to not AOT them
  :source-paths #{"src/cljs" "src/less" "src/scss" "test/clj" "test/cljs"}
  :resource-paths #{"src/clj" "src/cljc"}
  :dependencies '[[org.clojure/clojure    "1.9.0-alpha15"]
                  [org.clojure/clojurescript "1.9.495" :scope "test"]

                  [adzerk/boot-cljs       "2.1.0-SNAPSHOT" :scope "test"]
                  [crisptrutski/boot-cljs-test "0.3.0" :scope "test"]
                  [doo "0.1.7" :scope "test"]
                  [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                  [com.cemerick/piggieback "0.2.1" :scope "test"]
                  [weasel "0.7.0" :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                  [adzerk/boot-reload     "0.5.1" :scope "test"]
                  [metosin/boot-alt-test  "0.3.0"      :scope "test"]
                  [metosin/boot-deps-size "0.1.0-SNAPSHOT" :scope "test"]
                  [deraen/boot-less       "0.6.2"      :scope "test"]
                  ;; For boot-less
                  [org.slf4j/slf4j-nop    "1.7.21"     :scope "test"]
                  [deraen/boot-sass       "0.3.0"      :scope "test"]

                  ; Backend
                  [http-kit "2.2.0"]
                  [org.clojure/tools.namespace "0.3.0-alpha3"]
                  [reloaded.repl "0.2.3"]
                  [com.stuartsierra/component "0.3.1"]
                  [metosin/ring-http-response "0.8.0"]
                  [ring/ring-core "1.5.1"]
                  [javax.servlet/servlet-api "2.5"] ;; Required by ring multipart middleware
                  [compojure "1.5.1"]
                  [hiccup "1.0.5"]
                  [camel-snake-kebab "0.4.0"] ;; Cljc lib for testing c.t.n refresh

                  ; Frontend
                  [reagent "0.6.1-SNAPSHOT" :scope "test"]
                  [binaryage/devtools "0.8.2" :scope "test"]

                  ; LESS
                  [org.webjars/bootstrap "3.3.6"]
                  ; SASS
                  [org.webjars.bower/bootstrap "4.0.0-alpha" :exclusions [org.webjars.bower/jquery]]])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl repl-env]]
  '[adzerk.boot-reload    :refer [reload]]
  '[metosin.boot-alt-test  :refer [alt-test]]
  '[metosin.boot-deps-size :refer [deps-size]]
  '[deraen.boot-less      :refer [less]]
  '[deraen.boot-sass      :refer [sass]]
  '[crisptrutski.boot-cljs-test :refer [test-cljs]]
  '[backend.boot          :refer [start-app catch-output]]
  '[reloaded.repl         :refer [go reset start stop system]])

(task-options!
  pom {:project 'saapas
       :version "0.1.0-SNAPSHOT"
       :description "Application template for Cljs/Om with live reloading, using Boot."
       :license {"The MIT License (MIT)" "http://opensource.org/licenses/mit-license.php"}}
  aot {:namespace #{'backend.main}}
  jar {:main 'backend.main}
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
    ;; It is important that catch-output is the first task, this way all other
    ;; task can do everything they want with fileset, e.g. reload sees to output js files.
    (catch-output :paths #{"js/" "css/"})
    ;; Start-app must be after catch-output, so the output directory exists.
    (start-app :port port)
    ;; Reload must be before any tasks that write files that should be reloaded.
    (reload :open-file "vim --servername saapas --remote-silent +norm%sG%s| %s"
            :ids #{"js/main"})
    (if use-sass
      (sass)
      (less))
    ; This starts a repl server with piggieback middleware
    (cljs-repl :ids #{"js/main"})
    (cljs :ids #{"js/main"})
    (if speak (boot.task.built-in/speak) identity)))

(ns-unmap *ns* 'test)

(deftask test
  []
  (comp
    (alt-test)
    ;; FIXME: This is not a good place to define which namespaces to test
    (test-cljs :namespaces #{"frontend.core-test"})))

(deftask autotest []
  (comp
    (watch)
    (test)))

(deftask package
  "Build the package"
  []
  (comp
    (less :compression true)
    (cljs :optimizations :advanced
          :compiler-options {:preloads nil})
    (aot)
    (pom)
    (uber)
    (jar :file "saapas.jar")
    (sift :include #{#".*\.jar"})
    (target)))
