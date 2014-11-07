(ns saapas.dev
  (:require [net.cgrand.enlive-html :refer [set-attr prepend append html]]))

(def is-dev? true)

(def inject-devmode-html
  (comp
     (set-attr :class "is-dev")
     (prepend (html [:script {:type "text/javascript" :src "/js/out/goog/base.js"}]))
     (prepend (html [:script {:type "text/javascript" :src "/react/react.js"}]))
     (append  (html [:script {:type "text/javascript"} "goog.require('saapas.dev')"]))))
