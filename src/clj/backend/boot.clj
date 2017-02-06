(ns backend.boot
  (:require [boot.core :as b]
            [boot.filesystem :as fs]
            [ring.util.response :as response]
            [reloaded.repl :refer [go stop]]
            [backend.main :refer [setup-app!]]
            [clojure.tools.namespace.repl :refer [disable-reload!]])
  (:import [java.io File]))

(disable-reload!)

(b/deftask start-app
  [p port   PORT int  "Port"]
  (let [x (atom nil)]
    (b/cleanup (stop))
    (b/with-pre-wrap fileset
      (swap! x (fn [x]
                  (if x
                    x
                    (do (setup-app! {:port port})
                        (go)))))
      fileset)))

(defonce ^:private output-dir (atom nil))

(defn get-app-output-dir []
  @output-dir)

(b/deftask catch-output
  "Copy files in fileset with output role under these paths into
  a tmp-dir and remove the files from fileset."
  [p paths PATH #{str} "Paths"]
  (let [prev (atom nil)]

    (swap! output-dir (fn initialize-output-dir [d] (or d (b/tmp-dir!))))

    (fn middleware [next-task]
      (fn handler [fileset]
        (let [prev-output-fileset @prev
              output-dirs (set (b/output-dirs fileset))
              [tree output-tree] (reduce-kv (fn [[tree output-tree] k v]
                                              (if (and (contains? output-dirs (boot.tmpdir/dir v))
                                                       (some #(.startsWith k %) paths))
                                                [tree (assoc output-tree k v)]
                                                [(assoc tree k v) output-tree]))
                                            [{} {}]
                                            (:tree fileset))
              output-fileset (assoc fileset :tree output-tree)]

          (reset! prev output-fileset)

          (try
            (fs/patch! (fs/->path @output-dir) prev-output-fileset output-fileset :link :tmp)
            (catch Throwable t
              (fs/patch! (fs/->path @output-dir) prev-output-fileset output-fileset :link nil)))

          (next-task (b/commit! (assoc fileset :tree tree))))))))

(defn resources
  "Like compojure.route/resources, but serves files from tmp-dir when used with catch-output task,
  and else from classpath."
  [prefix {:keys [root] :as opts}]
  (if-let [x (some-> (get-app-output-dir) .getPath)]
    (fn [req]
      (if (.startsWith (:uri req) prefix)
        (response/file-response (subs (:uri req) (count prefix)) (assoc opts :root (str x File/separator root)))))
    (fn [req]
      (if (.startsWith (:uri req) prefix)
        (response/resource-response (subs (:uri req) (count prefix)) opts)))))

(comment
  (get-app-output-dir)
  ((resources "/js" {:root "js"}) {:uri "/js/main.js"}))
