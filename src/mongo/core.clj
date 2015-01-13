(ns mongo.core
  (:require [monger.core :as mg]
            [monger.operators :refer :all]
            [monger.query :refer :all]
            [clj-time.core :as t]
            [clojure.pprint :refer :all]
            [monger.json]
            [monger.joda-time])
  (:import [com.mongodb MongoOptions ServerAddress]))


(defn all-events [event_name max-limit]
  (let [conn (mg/connect)
        db   (mg/get-db conn "bemyeyes")
        yesterday (t/minus (t/now) (t/days 1))]
    (with-collection db "event_logs"
                     (find {:name event_name, :created_at { $gt yesterday}})
                     (fields [ :created_at :name ])
                     ;; note the use of sorted maps with sort
                     (sort (sorted-map :created_at -1))
                     (limit max-limit))))

(defn all-helper-notified
  ([] (all-helper-notified 10))
  ([max-limit] (doseq [el (seq (all-events "helper_notified" max-limit))] (pprint el))))
