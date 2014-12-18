(ns saapas.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defonce app-state (atom {:y 2014}))

(defn main []
  (js/console.log "Starting the app")
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/div
            (dom/h1 "Hello World! " (:y app))
            (dom/button
              #js
              {:onClick (fn [_] (om/transact! app :y (partial + 5)))}
              "+")
            (dom/button
              #js
              {:onClick (fn [_] (om/transact! app :y dec))}
              "-")))))
    app-state
    {:target (. js/document (getElementById "app"))}))

(main)
