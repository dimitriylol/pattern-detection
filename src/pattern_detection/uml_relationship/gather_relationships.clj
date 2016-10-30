(ns pattern-detection.uml-relationship.gather-relationships
  (:require [pattern-detection.uml-relationship.uml-relationship :refer [gather]]
            [pattern-detection.uml-relationship.id-resolver :refer [build-resolver]]))

(defn- all-kinds-relationship []
  (list :generalization))

(defn gather-relationships
  ([forest]
   (gather-relationships forest (all-kinds-relationship)))
  ([forest kinds-relationship]
   (let [id-resolver (build-resolver forest)]
     (map (fn [kind-relationship]
            {:graph (into {}
                          (mapcat (fn [tree]
                                    (gather {:kind kind-relationship
                                             :tree tree
                                             :id-resolver id-resolver}))
                                  forest))
             :kind kind-relationship})
          kinds-relationship))))
