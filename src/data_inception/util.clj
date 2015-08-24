(ns data-inception.util
  (:require [clojure.string :as st]
            [incanter.core :refer [to-dataset]]))

(defn valid-location?
  [s]
  (when (seq s)
    (let [a (st/split s #",")]
      (boolean (= 2 (count a))))))

(defn valid-location
  [s]
  (when (seq s)
    (if (valid-location? s) s nil)))

(defn location->lat
  [s]
  (when (seq s)
    (let [[lat lon] (st/split s #",")]
      lat)))

(defn location->long
  [s]
  (when (seq s)
    (let [[lat lon] (st/split s #",")]
      lon)))

(defn lat-lon->loc
  [lat lon]
  (str lat "," lon))

(defn complete-url?
  [url]
  (boolean
   (re-find #"http" url)))

(defn complete-url
  [url]
  (if (complete-url? url) url (str "http://" url)))

(defn trim
  [s]
  (if (seq s) (st/trim s) ""))

(defn titleize
  [s]
  (when (seq s)
    (let [a (st/split s #" ")
          c (map st/capitalize a)]
      (->> c (interpose " ") (apply str) trim))))


