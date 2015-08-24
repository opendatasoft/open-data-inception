(ns data-inception.core
  (:refer-clojure :exclude [update])
  (:require [grafter.tabular :as gft]
            [clojure.string :as st]
            [data-inception.util :refer :all]
            [data-inception.conversions :refer [dms->dec]]))

(defn okfn-pipeline
  [okfn-data]
  (let [cleaned (-> (gft/read-dataset okfn-data)
                    (gft/make-dataset gft/move-first-row-to-header)
                    (gft/columns ["title" "url" "author" "description" "location"])
                    (gft/derive-column "latitude" ["location"] location->lat)
                    (gft/derive-column "longitude" ["location"] location->long)
                    (gft/mapc {"url" (comp complete-url st/lower-case)
                               "title" titleize
                               "author" titleize})
                    (gft/make-dataset ["Name" "URL" "Organisation" "Description" "Location" "Latitude" "Longitude"]))]
    (gft/write-dataset "clean-data/okfn.csv" cleaned)))

(defn opengeocode-pipeline
  [opengeocode-data]
  (let [cleaned (-> (gft/read-dataset opengeocode-data)
                    (gft/make-dataset gft/move-first-row-to-header)
                    (gft/columns ["Country"  " Web" " Full Name" " Latitude" " Longitude"])
                    (gft/make-dataset ["Organisation" "URL" "Name" "Latitude" "Longitude"])
                    (gft/swap "Organisation" "Name")
                    (gft/mapc {"Latitude" dms->dec
                               "Longitude" dms->dec}))]
    cleaned))


