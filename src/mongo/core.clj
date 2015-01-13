(ns mongo.core
  (:require [monger.core :as mg]
            [monger.operators :refer :all]
            [monger.query :as mq]
            [clj-time.core :as t]
            [clojure.pprint :refer :all]
            [monger.json]
            [monger.joda-time])
  (:import [com.mongodb MongoOptions ServerAddress]))

(defn events-from-yesterday [event-name]
   (let [yesterday (t/minus (t/now) (t/days 1))]
      (fn [m] (mq/find m {:name event-name, :created_at { $gt yesterday}}))))

(defn all-events
  ([event-name max-limit]
     (all-events (events-from-yesterday event-name) max-limit "event_logs"))
  ([query max-limit collection-name]
   (let [conn (mg/connect)
         db   (mg/get-db conn "bemyeyes")]
     (mq/with-collection db collection-name
                         (query)
                         (mq/fields [ :created_at :name ])
                         (mq/sort (sorted-map :created_at -1))
                         (mq/limit max-limit)))))

(defn print-events
  ([] (print-events "helper_notified" 10))
  ([event-name] (print-events event-name 10))
  ([event-name max-limit] (doseq
                            [el (seq (all-events event-name max-limit))]
                            (pprint el))))
