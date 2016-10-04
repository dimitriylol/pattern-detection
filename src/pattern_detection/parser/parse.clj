(ns pattern-detection.parser.parse
  (:require [clojure.java.io :refer [file]]
            [pattern-detection.parser.ParseFile :refer :all]
            [pattern-detection.parser.JavaFile :refer [->JavaFile]]))

(defrecord UndefinedFile [file]
  ParseFile
  (parse-src [this] (println "Sorry, can't parse " file)))

(defn- lang-by-path [path-str]
  (keyword (subs path-str (inc (.lastIndexOf path-str ".")))))

(defn- file? [file] (.isFile file))

(defn- get-only-files [path]
  (filter file? (file-seq (file path))))

(defn- dispatch-by-extension [extension]
  (case extension
    :java ->JavaFile
    :default ->UndefinedFile))

(defn- create-records [io-files]
  (map #((dispatch-by-extension (lang-by-path (str %))) %) io-files))

(defn parse-proj [absolute-path-src]
  (map parse-src (create-records (get-only-files absolute-path-src))))
