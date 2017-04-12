(ns frontend.core
  (:require-macros [frontend.macro :refer [foobar]])
  (:require [reagent.core :as r]
            [common.hello :refer [foo-cljc]]
            [foo.bar]))

;; Reagent application state
;; Defonce used to that the state is kept between reloads
(defonce app-state (r/atom {:y 2017}))

(defn main []
  [:div
   [:h1 (foo-cljc (:y @app-state))]
   [:div.btn-toolbar
    [:button.btn.btn-danger
     {:type "button"
      :on-click #(swap! app-state update :y inc)} "+"]
    [:button.btn.btn-success
     {:type "button"
      :on-click #(swap! app-state update :y dec)} "-"]
    [:button.btn.btn-default
     {:type "button"
      :on-click #(js/console.log @app-state)}
     "Console.log"]]])

(defn start! []
  (js/console.log "Starting the app")
  (r/render-component [main] (js/document.getElementById "app")))

;; When this namespace is (re)loaded the Reagent app is mounted to DOM
(start!)

;; Macro test
(foobar :abc 3)

;; Example of interop call to plain JS in src/cljs/foo.js
(js/foo)

(goog-define MODE "nil")
(goog-define DEFAULT "nil")

(js/console.log "mode" MODE)
(js/console.log "default" DEFAULT)

(comment
  (println "foo"))
