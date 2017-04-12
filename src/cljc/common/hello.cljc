(ns common.hello
  (:require [om.dom :as dom]))

(defn foo-cljc [x]
  "I don't do a whole lot."
  [x]
  (str "Hello from " #?(:clj "clj" :cljs "cljs") " " x "!"))


(defn aasd []
  (dom/div #js {:class "list-items"}))
