(ns pattern-detection.uml-relationship.id-resolver
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as cj-str]
            [pattern-detection.parser.JavaAST :refer :all]
            [pattern-detection.uml-relationship.utils.traverser :refer [full-tree-matcher]]))

(defn- gather-decls [tree builder-qid]
  (full-tree-matcher
   tree
   (fn [local-tree]
     (match [local-tree]
            [[:classDecl & children]]
            {(builder-qid (get-class-id local-tree)) :class}
            [[:interfaceDecl & children]]
            {(builder-qid (get-interface-id local-tree)) :interface}   
            :else nil))))

(defn build-resolver [forest]
  (apply merge (mapcat #(gather-decls (:tree %)
                                      (build-qid (get-package-id (:tree %))))
                       forest)))
