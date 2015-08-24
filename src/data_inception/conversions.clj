(ns data-inception.conversions
  (:require [clojure.string :as st]))

(defn clean-quotes
  [s]
  (-> s
      (st/replace "″" "''")
      (st/replace "′" "'")))

(defn remove-nsew-char
  [s]
  (when (seq s)
    (let [s (clean-quotes s)
          chars #{\0,\1,\2,\3,\4,\5,\6,\7,\8,\9,\.,\',\°}]
      (apply str
             (filter #(chars %) s)))))

(defn zero-first->number
  [s]
  (when (seq s)
    (let [n (re-find #"[1-9][0-9]*" s)]
      (when-not (nil? n)
        (read-string n)))))

(defn dms->dec
  [s]
  (when (seq s)
    (let [nsew (re-find #"[A-Z]" s)
          sign ({"N" "" "E" "" "W" "-" "S" "-"} nsew)
          s (if (= \. (first s)) (st/replace-first s "." "0.") s)
          [whole min sec] (re-seq #"\d+" s)
          [min sec] (if (re-find #"′" s) (map zero-first->number [min sec]) [min sec] )
          tot (if (re-find #"′" s)
                (cond
                 (and (nil? min) (nil? sec)) 0
                 (nil? min) (format "%.5f" (/ sec 3600.0))
                 (nil? sec) (format "%.5f" (/ (* min 60) 3600.0))
                 :else (format "%.5f" (/ (+ (* min 60) sec) 3600.0)))
                min)
          decimal (str sign whole (when tot ".") (last (st/split (str tot) #"\.")))]
      decimal)))
