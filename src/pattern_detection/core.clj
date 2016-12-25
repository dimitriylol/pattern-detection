(ns pattern-detection.core
  (:gen-class)
  (:require [pattern-detection.parser.parse :refer [parse-proj]]
            [pattern-detection.uml-relationship.collector-relationships :refer [gather-relationships]]
            [pattern-detection.uml-relationship.build-graph :refer [build-graph build-graphs]]
            [pattern-detection.matcher.matcher :refer [find-exact-matching]]
            [loom.io :refer [view]]))

(defn visualize [path]
  ((comp build-graph gather-relationships parse-proj) path))

(defn builder-graph [path]
  ((comp build-graphs gather-relationships parse-proj) path))

(defn pretty-correspondence [list-correspondences]
  (doseq [map list-correspondences]
    (println "One of exact possibility")
    (doseq [correspondence-sys-dp map]
      (println (str ((key correspondence-sys-dp) :name)
                    "->"
                    ((val correspondence-sys-dp) :name))))))

(defn -main
  "accepts two pathes. First is pattern, second is project"
  [dp-pattern-path project-path]
  (pretty-correspondence (find-exact-matching (builder-graph dp-pattern-path)
                                              (builder-graph project-path))))
  
