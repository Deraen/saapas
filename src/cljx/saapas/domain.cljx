(ns saapas.domain
  (:require [schema.core :as s :include-macros true]))

(s/defschema Link {:id s/Int
                   :uri s/Str
                   :timestamp s/Inst
                   :votes s/Int})

(s/defschema NewLink (dissoc Link :id :timestamp :votes))
