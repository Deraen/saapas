(ns frontend.core
  (:require-macros [frontend.macro :refer [foobar]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponentk]]
            [sablono.core :as html :refer-macros [html]]
            [common.hello :refer [foo-cljc]]
            [foo.bar]
            [cljs-http.client :as http]))

(js/foo)

(defonce app-state (atom {:y 2014}))

; Edit cljs_http.client/get and e.g. add js/console.log call
(http/get "/index.html")

(defcomponentk main
  [[:data y :as cursor]]
  (render [_]
    (html
      [:div
       [:h1 (foo-cljc y)]
       [:div.btn-toolbar
        [:button.btn.btn-danger {:type "button" :on-click #(om/transact! cursor :y (partial + 5))} "+"]
        [:button.btn.btn-success {:type "button" :on-click #(om/transact! cursor :y dec)} "-"]]])))

(defn start! []
  (js/console.log "Starting the app")
  (om/root main app-state {:target (. js/document (getElementById "app"))}))

(start!)

; Macro test
(foobar :abc 3)
