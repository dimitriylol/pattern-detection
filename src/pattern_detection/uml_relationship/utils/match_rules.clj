(ns pattern-detection.uml-relationship.utils.match-rules
  (:require [clojure.core.match :refer [match]]
            [pattern-detection.uml-relationship.utils.traverser :refer [full-tree-matcher]]))

(defn match-classes [tree]
  (match [tree]
         [[:classDecl & children]] true
         :else nil))

(defn match-interfaces [tree]
  (match [tree]
         [[:interfaceDecl & children]] true
         :else nil))

(defn match-methods [tree]
  (match [tree]
         [[:methodDecl & children]] true
         :else nil))

(defn match-methods-in-class [class-name]
  (fn [tree]
    (map
     #(full-tree-matcher % match-methods)
     (match [tree]
            [[:classDecl _ [:id class-name] _ _ [:declBody & decls]]]
            decls
            :else nil))))

(defn get-id [qid-id-sort]
  (nth qid-id-sort 1))

(defn match-class-extends [class-name]
  (fn [tree]
    (map get-id
         (match [tree]
                [[:classDecl _ [:id class-name] [:extends & qid] & children]]
                qid
                :else nil))))

(defn match-class-implements [class-name]
  (fn [tree]
    (map get-id
         (match [tree]
                [[:classDecl _ [:id class-name] _ [:implements & qids] & children]]
                qids
                :else nil))))

(defn match-interface-extends [interface-name]
  (fn [tree]
    (map get-id
         (match [tree]
                [[:interfaceDecl _ [:id interface-name] [:extends & qids] & children]]
                qids
                :else nil))))
