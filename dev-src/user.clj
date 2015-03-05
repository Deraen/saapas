(ns user)

; Looks like user ns is being loaded before other tasks (such as cljx)
; get a chance to run.
(defn abc []
  (require 'abc)
  ((resolve 'abc/hello-world)))
