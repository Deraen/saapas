(ns frontend.core
  (:require-macros [frontend.macro :refer [foobar]])
  (:require [reagent.core :as r]
            [common.hello :refer [foo-cljc]]
            [foo.bar]))

(js/foo)

(defonce app-state (r/atom {:y 2015}))

(defn main []
  [:div
   [:h1 (foo-cljc (:y @app-state))]
   [:div.btn-toolbar
    [:button.btn.btn-danger {:type "button" :on-click #(swap! app-state update :y (partial + 5))} "+5"]
    [:button.btn.btn-success {:type "button" :on-click #(swap! app-state update :y dec)} "-"]]])

(defn start! []
  (js/console.log "Starting the app")
  (r/render-component [main] (js/document.getElementById "app")))

(start!)

; Macro test
(foobar :abc 3)
