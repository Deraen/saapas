(ns saapas.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defonce app-state (atom {:text "Hello Saapas!"}))

(defn main []
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/h1 (:text app)))))
    app-state
    {:target (. js/document (getElementById "app"))}))
