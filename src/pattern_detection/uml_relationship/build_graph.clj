(ns pattern-detection.uml-relationship.build-graph
  (:require [loom.graph :refer [digraph, edges]]
            [loom.attr :refer [add-attr-to-edges]]))

(defn- colorize-edges [loom-digraph color]
  (add-attr-to-edges
   loom-digraph
   :color color
   (edges loom-digraph)))

(defn- relationship-color [uml-relationship]
  (get {:generalization :blue
        :realization :black
        :dependency :red} uml-relationship))

(defn build-graphs [graphs-hash-map]
  (map #(colorize-edges (digraph (:graph %))
                        (relationship-color (:kind %)))
       graphs-hash-map))

(defn build-graph [graphs-hash-map]
  (apply digraph (build-graphs graphs-hash-map)))
