(ns pattern-detection.uml-relationship.uml-relationship)

(defmulti gather (fn [uml-relationship] (:kind uml-relationship)))
