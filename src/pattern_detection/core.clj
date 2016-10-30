(ns pattern-detection.core
  (:gen-class)
  (:require [pattern-detection.parser.parse :refer [parse-proj]]
            [pattern-detection.uml-relationship.gather-relationships :refer [gather-relationships]]
            [pattern-detection.uml-relationship.build-graph :refer [build-graph]]
            [loom.io :refer [view]]))

(defn -main
  "accepts one parameter with absolute path to project for parsing"
  [absolute-path-proj]
  (view (build-graph (gather-relationships (parse-proj absolute-path-proj)))))
