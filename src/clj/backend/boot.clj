(ns backend.boot
  {:boot/export-tasks true}
  (:require [boot.core :refer :all]
            [reloaded.repl :refer [go]]
            [backend.main :refer :all]))

(deftask start-app
  [p port   PORT int  "Port"
   r reload      bool "Add reload mw"]
  (let [x (atom nil)]
    (with-post-wrap fileset
      (swap! x (fn [x]
                  (if x
                    x
                    (do (setup-app! {:port port
                                     :reload reload
                                     :reload-dirs (get-env :directories)})
                        (go)))))
      fileset)))
