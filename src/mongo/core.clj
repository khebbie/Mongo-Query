(ns mongo.core
  (:require [monger.core :as mg]
            [monger.operators :refer :all]
            [monger.query :as mq]
            [clj-time.core :as t]
            [clojure.pprint :refer :all]
            [monger.json]
            [monger.joda-time])
  (:import [com.mongodb MongoOptions ServerAddress]))


(defn all-events [event_name max-limit]
  (let [conn (mg/connect)
        db   (mg/get-db conn "bemyeyes")
        yesterday (t/minus (t/now) (t/days 1))]
    (mq/with-collection db "event_logs"
                     (mq/find {:name event_name, :created_at { $gt yesterday}})
                     (mq/fields [ :created_at :name ])
                     ;; note the use of sorted maps with sort
                     (mq/sort (sorted-map :created_at -1))
                     (mq/limit max-limit))))

(defn print-events
  ([] (print-events "helper_notified" 10))
  ([event-name] (print-events event-name 10))
  ([event-name max-limit] (doseq [el (seq (all-events event-name max-limit))] (pprint el))))
