(ns pattern-detection.uml-relationship.gather-relationship
  (:require [clojure.core.match :refer [match]]
            [pattern-detection.uml-relationship.utils.traverser :refer [full-tree-matcher]]
            [pattern-detection.uml-relationship.utils.graph-node :refer [connect-nodes]]))

(defn- get-id [qid]
  (nth qid 1))

(defmulti gather (fn [uml-relationship] (:kind uml-relationship)))

(defmethod gather :generalization [{tree :tree id-resolver :id-resolver}]
  (full-tree-matcher
   tree
   (fn [local-tree]
     (match [local-tree]
            [[(:or :classDecl :interfaceDecl) _ [:id decl-name] [:extends & qid] & children]]
            (connect-nodes decl-name (map get-id qid) id-resolver)
            :else nil))))

(defmethod gather :realization [{tree :tree id-resolver :id-resolver}]
  (full-tree-matcher
   tree
   (fn [local-tree]
     (match [local-tree]
            [[:classDecl _ [:id class-name] _ [:implements & qid] & children]]
            (connect-nodes class-name (map get-id qid) id-resolver)
            :else nil))))

(defn find-dependency [decl-body]
  (full-tree-matcher
   decl-body
   (fn [local-tree]
     (match [local-tree]
            [[:declPair [:type [:qid qid]] _]] qid
            [[:declVar [:type [:qid qid]] & children]] qid
            :else nil))))

(defmethod gather :dependency [{tree :tree id-resolver :id-resolver}]
  (full-tree-matcher
   tree
   (fn [local-tree]
     (match [local-tree]
            [(:or [:classDecl _ [:id decl-name] _ _ decl-body]
                  ;; [:interfaceDecl _ [:id decl-name] _ decl-body]]
                  )]
            (connect-nodes decl-name (find-dependency decl-body) id-resolver)
            :else nil))))
