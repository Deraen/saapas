(ns saapas.hello)

(defn foo-cljc [x]
  "I don't do a whole lot."
  [x]
  (str "Hello from " #?(:clj "clj" :cljs "cljs") " " x "!"))
