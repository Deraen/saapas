(ns saapas.hello)

(defn foo-cljx [x]
  "I don't do a whole lot."
  [x]
  (println x (str "Hello from " #+clj "clj" #+cljs "cljs" "!")))
