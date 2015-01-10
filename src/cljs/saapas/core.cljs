(ns saapas.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponentk]]
            [sablono.core :as html :refer-macros [html]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [goog.string :as gs]))

(def empty-state {:links []
                  :new-link {:uri "http://"}})

(defonce app-state (atom empty-state))

(defn prettify-url [url]
  (.replace url #"^http[s]?://" ""))

(defn date->str [ts]
  ; FIXME: Ts is string
  (let [ts (js/Date. ts)]
    (gs/format "%d.%d.%d" (.getDate ts) (inc (.getMonth ts)) (.getFullYear ts))))

(defn insert-link [new-link]
  (go (swap! app-state update-in [:links] conj (-> "/api/links" (http/post {:json-params @new-link}) <! :body))
      (om/update! new-link (:new-link empty-state))))

(defn vote [{:keys [id] :as cursor}]
  (go (om/update! cursor (-> (str "/api/links/" id "/vote") http/post <! :body))))

(defcomponentk link-component
  [[:data uri votes id timestamp :as cursor]]
  (render [_]
    (html
      [:li
       [:a.vote {:on-click #(vote cursor)} [:span.caret-up]]
       [:span votes]
       [:a {:href uri :target "new"} (prettify-url uri)]
       [:span.time (date->str timestamp)]])))

(defcomponentk new-link-component
  [[:data uri :as cursor]]
  (render [_]
    (html
      [:form.form-inline
       [:div.form-group
        [:label.sr-only "URI"]
        [:input.form-control {:type "text" :value uri :on-change #(om/update! cursor :uri (.. % -target -value))}]]
       " " ; Inline form paddings require whitespaces
       [:button.btn.btn-success {:type "button" :on-click #(insert-link cursor)} "Add new link"]])))

(defcomponentk main
  [[:data links new-link :as cursor]]
  (render [_]
    (html
      [:div
       [:h1 "Link list"]
       [:ul.links (om/build-all link-component links {:key :id})]
       (om/build new-link-component new-link)])))

(defn start! []
  (js/console.log "Starting the app")
  (om/root main app-state {:target (. js/document (getElementById "app"))})
  (go (swap! app-state assoc :links (-> "/api/links" http/get <! :body))))

(start!)
