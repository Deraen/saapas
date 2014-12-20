(set-env!
  :source-paths #{"src/cljs" "src/cljx" "src/less"}
  :resource-paths #{"src/clj"}
  :dependencies '[[adzerk/boot-cljs       "0.0-2411-5"]
                  [adzerk/boot-cljs-repl  "0.1.7"]
                  [adzerk/boot-reload     "0.2.0"]
                  [deraen/boot-cljx       "0.2.1"]
                  [deraen/boot-less       "0.1.0-SNAPSHOT"]

                  [org.clojure/clojure "1.6.0"]
                  [org.clojure/tools.namespace "0.2.8"]
                  [ring "1.3.1"]
                  [metosin/ring-http-response "0.5.2"]
                  [compojure "1.2.1"]
                  [om "0.8.0-alpha2"]
                  [http-kit "2.1.19"]
                  [prismatic/om-tools "0.3.6"]])

(require
  '[adzerk.boot-cljs      :refer :all]
  '[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload    :refer :all]
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
    (watch)
    ; Should be before cljs so the generated code is picked up
    (reload :on-jsload 'saapas.core/main)
    (less)
    (cljx)
    ; This starts a normal repls with piggieback middleware
    (cljs-repl)
    (add-js-lib :path "react/react.js" :target "public/react.inc.js")
    (cljs :optimizations :none :unified-mode true)
    (start-app)))

(deftask package
  "Build the package"
  []
  (comp
    (less)
    (cljx)
    ; Target path doesn't matter but name does
    ; These are not included in uberjar
    (add-js-lib :package true :path "react/react.min.js"     :target "react.inc.js")
    (add-js-lib :package true :path "react/externs/react.js" :target "react.ext.js")
    (cljs :optimizations :advanced)
    (aot)
    (pom)
    (uber)
    (jar)))
