(ns pattern-detection.core
  (:gen-class)
  (:require [pattern-detection.parser.parse :refer [parse-proj]]))

(defn -main
  "accepts one parameter with absolute path to project for parsing"
  [absolute-path-proj]
  (println (parse-proj absolute-path-proj)))
