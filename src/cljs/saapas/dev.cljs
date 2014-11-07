(ns saapas.dev
  (:require [adzerk.boot-reload.client :as reload]
            [saapas.core :as saapas]))

(enable-console-print!)

(reload/connect "ws://localhost:8090"
                {:on-jsload (fn []
                              (saapas/main))})
