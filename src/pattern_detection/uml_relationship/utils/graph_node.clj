(ns pattern-detection.uml-relationship.utils.graph-node)

(defn- build-graph-node [name-str node-kind]
  {:name name-str
   :node-kind node-kind})

(defn connect-nodes [node-name children-names id-resolution]
  { (build-graph-node node-name
                      (get id-resolution node-name))
   (map #(build-graph-node % (get id-resolution %)) children-names) })
