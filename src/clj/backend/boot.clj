(ns backend.boot
  {:boot/export-tasks true}
  (:require [boot.core :refer :all]
            [reloaded.repl :refer [go]]
            [backend.main :refer :all]
            [clojure.tools.namespace.repl :refer [disable-reload!]]))

(disable-reload!)

(deftask start-app
  [p port   PORT int  "Port"]
  (let [x (atom nil)]
    (with-pre-wrap fileset
      (swap! x (fn [x]
                  (if x
                    x
                    (do (setup-app! {:port port})
                        (go)))))
      fileset)))
