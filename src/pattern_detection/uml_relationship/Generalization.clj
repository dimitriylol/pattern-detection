(ns pattern-detection.uml-relationship.Generalization
  (:require [pattern-detection.uml-relationship.uml-relationship :as uml]
            [clojure.core.match :refer [match]]
            [pattern-detection.uml-relationship.utils.traverser :refer [full-tree-matcher]]
            [pattern-detection.uml-relationship.utils.match-rules :refer [get-id]]
            [pattern-detection.uml-relationship.utils.graph-node :refer [connect-nodes]]))

(defmethod uml/gather :generalization [uml-relationship]
  (flatten
   (full-tree-matcher
    (:tree uml-relationship)
    (fn [local-tree]
      (match [local-tree]
             [[(:or :classDecl :interfaceDecl) _ [:id decl-name] [:extends & qid] & children]]
             (connect-nodes decl-name (map get-id qid) (:id-resolution uml-relationship))
             :else nil)))))
