(ns demograph.core
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]))

(def third #(nth % 2))
(def fourth #(nth % 3))
(def extract #(str (first %) (second %) (third %) (fourth %)))

(defn parse-master-file-row
  [row]
  row)

(defn read-master-file
 [file-name]
 (with-open [rdr (io/reader file-name)]
   (doall 
    (mapv (fn[x] (extract x))(csv/read-csv rdr)))))

(defn filter-data-file
 [in-file out-file master-list]
   (with-open [rdr (io/reader in-file)]
     (with-open [rtr (io/writer out-file)]
       (csv/write-csv rtr (take 1 (csv/read-csv rdr)))
       (doall
         (filter #(some (hash-set (extract %)) master-list)(csv/read-csv rdr)))))) 
