(ns demograph.alan
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :refer [trim]])
  (:import java.util.concurrent.CountDownLatch))

(defn csv-seq
  ([file]
     (let [rdr (io/reader file)]
       (csv-seq (csv/read-csv rdr) rdr)))
  ([csv rdr]
     (if (seq csv)
       (lazy-seq (cons (first csv) (csv-seq (rest csv) rdr)))
       (.close rdr))))

(defn outerleave
  [n coll]
  (->> (repeat coll)
       (map drop (range n))
       (map (partial take-nth n))))

(defn write-csv
  [coll file]
  (with-open [writer (io/writer file)]
    (csv/write-csv writer coll)))

(defn transpose
  [& colls]
  (apply map vector colls))

(defn split
  [in outs]
  (let [[header & contents] (csv-seq in)
        parts (->> contents
                   (outerleave (count outs))
                   (map (partial cons header)))
        latch (CountDownLatch. (count outs))]
    (doseq [[part out] (transpose parts outs)]
      (future (write-csv part out) (.countDown latch)))
    (.await latch)))

(comment
  ;; example
  (split "data/in.csv" ["data/out1.csv" "data/out2.csv" "data/out3.csv"]))
