(ns saapas.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defonce app-state (atom {:y 2014}))

(defn main []
  (js/console.log "Starting the app")
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/div nil
            (dom/h1 nil
              "Hello World! " (:y app))
            (dom/button
              #js
              {:className "btn btn-danger"
               :onClick (fn [_] (om/transact! app :y (partial + 5)))}
              "+")
            (dom/button
              #js
              {:className "btn btn-primary"
               :onClick (fn [_] (om/transact! app :y dec))}
              "-")))))
    app-state
    {:target (. js/document (getElementById "app"))}))

(main)
