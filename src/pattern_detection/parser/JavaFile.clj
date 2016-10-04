(ns pattern-detection.parser.JavaFile
  (:require [clojure.string :as cj-str]
            [clojure.java.io :as io]
            [pattern-detection.parser.ParseFile :refer [ParseFile]]
            [instaparse.core :as insta]))

(def ^:const java-grammar (io/resource "java.grammar"))
(def ^:const java-parser (insta/parser (slurp java-grammar)))
(def ^:const java-comments #"//.*|/\*(?s)(.*)\*/")

(defn- remove-comment [src-str] 
  (cj-str/replace src-str java-comments ""))

(defn- prepare-file [file-name]
  (remove-comment (slurp file-name)))

(defrecord JavaFile [file]
  ParseFile
  (parse-src [this] (insta/parses java-parser (prepare-file file))))
