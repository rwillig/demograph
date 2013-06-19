(ns demograph.core
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv])
  (:require [clojure.string :refer [trim]])
)

(def third #(nth % 2))
(def fourth #(nth % 3))
(def extract #(str (trim (first %)) (trim (second %)) (trim (third %)) (trim (fourth %))))

(defn read-master-file
 [file-name]
 (with-open [rdr (io/reader file-name)]
   (doall 
    (into #{} (map extract (csv/read-csv rdr))))))

(defn filter-data-file
 [in-file out-file master-list]
   (with-open [rdr (io/reader in-file)]
     (with-open [rtr (io/writer out-file)]
       (csv/write-csv rtr (take 1 (csv/read-csv rdr)))
       (doall
         (csv/write-csv rtr (filter #(get master-list (extract %))(csv/read-csv rdr))))))) 

(defn take-last-n-from-csv
  [n in-file out-file]
    (with-open [rdr (io/reader in-file)]
      (with-open [rtr (io/writer out-file)]
        (csv/write-csv rtr (take 1 (csv/read-csv rdr)))
	(csv/write-csv rtr (take-last n (csv/read-csv rdr))))))

(defn split-csv-files
 [in-file & out-files]
   (let [rdr  (io/reader in-file)
         head (take 1 (csv/read-csv rdr))
         outs (map (fn[x] (io/writer x)) out-files)]
    (mapv #(csv/write-csv % head) outs)
    (mapv #(.close %) outs)))



;   (while (let [b (take 1 (csv/read-csv rdr))](not (nil? b))(csv/write-csv (nth outs (mod (Integer/parseInt (first b)) (count outs))) b)))
