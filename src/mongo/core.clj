(ns mongo.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [clj-time.core :as t])
  (:import [com.mongodb MongoOptions ServerAddress]))

(defn all-events []
  (let [conn (mg/connect)
      db   (mg/get-db conn "bemyeyes")
      coll "event_logs"]

  (mc/find db coll {:name "helper_notified"})))
