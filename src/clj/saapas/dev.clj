(ns saapas.dev
  (:require [net.cgrand.enlive-html :refer [set-attr prepend append html]]))

(def is-dev? true)

(def inject-devmode-html
  (comp
     (set-attr :class "is-dev")
     (prepend (html [:script {:type "text/javascript" :src "/out/goog/base.js"}]))
     (prepend (html [:script {:type "text/javascript" :src "/react/react.js"}]))
     (append  (html [:script {:type "text/javascript"} "goog.require('saapas.core')"]))
     (append  (html [:script {:type "text/javascript"} "goog.require('saapas.dev')"]))))
