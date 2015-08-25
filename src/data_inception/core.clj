(ns data-inception.core
  (:refer-clojure :exclude [update])
  (:require [grafter.tabular :as grafter]
            [incanter.core :refer [to-dataset]]
            [clojure.string :as st]
            [data-inception.util :refer :all]
            [data-inception.conversions :refer [dms->dec]]))

(defn okfn-pipeline
  [okfn-data]
  (let [cleaned (-> (grafter/read-dataset okfn-data)
                    (grafter/make-dataset grafter/move-first-row-to-header)
                    (grafter/columns ["title" "url" "author" "description" "location"])
                    (grafter/mapc {"location" valid-location})
                    (grafter/derive-column "latitude" ["location"] location->lat)
                    (grafter/derive-column "longitude" ["location"] location->long)
                    (grafter/mapc {"url" (comp complete-url st/lower-case)
                                   "title" titleize
                                   "author" titleize})
                    (grafter/make-dataset ["Name" "URL" "Organisation" "Description" "Location" "Latitude" "Longitude"]))]
    (grafter/write-dataset "clean-data/okfn.csv" cleaned)))

(defn opengeocode-pipeline
  [opengeocode-data]
  (let [cleaned (-> (grafter/read-dataset opengeocode-data)
                    (grafter/make-dataset grafter/move-first-row-to-header)
                    (grafter/columns ["Country"  " Web" " Full Name" " Latitude" " Longitude"])
                    (grafter/make-dataset ["Organisation" "URL" "Name" "Latitude" "Longitude"])
                    (grafter/swap "Organisation" "Name")
                    (grafter/mapc {"Latitude" dms->dec
                                   "Longitude" dms->dec
                                   "URL" (comp complete-url st/lower-case)
                                   "Name" titleize
                                   "Organisation" titleize})
                    (grafter/add-columns {"Description" ""})
                    (grafter/derive-column "Location" ["Latitude" "Longitude"] lat-lon->loc)
                    (grafter/swap "Description" "Latitude" "Location" "Longitude"))]
    (grafter/write-dataset "clean-data/opengeocode.csv" cleaned)))

(defn awesome-ds-pipeline
  [awesome-ds-data]
  (let [cleaned (-> (grafter/read-dataset awesome-ds-data)
                    (grafter/make-dataset grafter/move-first-row-to-header)
                    (grafter/add-columns {"Organisation" ""})
                    (grafter/swap "Description" "Name" "Description" "URL" "Description" "Organisation")
                    (grafter/add-columns {"Location" ""
                                          "Latitude" ""
                                          "Longitude" ""}))]
    (grafter/write-dataset "clean-data/awesome-ds.csv" cleaned)))

(defn merge-datafiles
  [data-file-1 data-file-2]
  (let [ds1 (grafter/read-dataset data-file-1)
        ds2 (grafter/read-dataset data-file-2)
        ds (-> (concat (:rows ds1) (:rows ds2))
                distinct
                to-dataset
                (grafter/make-dataset grafter/move-first-row-to-header))
        merged (->> ds :rows (filter #(not (empty? (% "Name")))) to-dataset)]
    (grafter/write-dataset "clean-data/inception.csv" merged)))
