(ns pattern-detection.uml-relationship.collector-relationships
  (:require [pattern-detection.uml-relationship.gather-relationship :refer [gather]]
            [pattern-detection.uml-relationship.id-resolver :refer [build-resolver]]
            [pattern-detection.uml-relationship.qualify-id :refer [make-id-qualified]]))

(defn- all-kinds-relationship []
  (list :generalization
        :realization
        :dependency))

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
                                  (make-id-qualified forest)))
             :kind kind-relationship})
          kinds-relationship))))
