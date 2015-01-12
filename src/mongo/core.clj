(ns mongo.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [clj-time.core :as t]
            [monger.joda-time])
  (:import [com.mongodb MongoOptions ServerAddress]))


(defn all-events []
  (let [conn (mg/connect)
      db   (mg/get-db conn "bemyeyes")
      coll "event_logs"
      yesterday (t/minus (t/now) (t/days 1))]

  (mc/find db coll {:name "helper_notified", :created_at { $gt yesterday}})))
