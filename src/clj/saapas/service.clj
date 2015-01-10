(ns saapas.service
  (:require [schema.core :as s]
            [saapas.domain :refer :all])
  (:import [java.util Date]))

(def i (atom 0))
(def links (atom {}))

(defn find-links []
  (->> @links
       vals
       (sort-by :timestamp)))

(defn insert-link [link]
  (let [id (swap! i inc)]
    (swap! links assoc id (-> link (assoc :id id :timestamp (Date.) :votes 0)))
    (get @links id)))

(defn vote [id]
  (swap! links update-in [id :votes] inc)
  (get @links id))
