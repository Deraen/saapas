(ns saapas.boot
  {:boot/export-tasks true}
  (:require [boot.core :refer :all]
            [reloaded.repl :refer [go]]
            [saapas.main :refer :all]))

(deftask start-app
  [p port   PORT int  "Port"
   r reload      bool "Add reload mw"]
  (let [f (delay
            (setup-app! {:port port
                        :reload reload
                        :reload-dirs (get-env :directories)})
            (go))]
    (with-post-wrap fileset @f fileset)))
