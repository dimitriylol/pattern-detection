(ns pattern-detection.uml-relationship.utils.match-rules
  (:require [clojure.core.match :refer [match]]
            [pattern-detection.uml-relationship.utils.traverser :refer [full-tree-matcher]]))

(defmacro matcher [grammar-sort]
  `(fn [tree#]
     (match [tree#]
            [[~grammar-sort & children#]] tree#
            :else nil)))

(defmacro matcher-rule [rule]
  `(fn [tree#]
     (match [tree#]
            [~rule] tree#
            :else nil)))

(defmacro match-decl-with-id [decl-sort id]
  `(fn [tree#]
     (match [tree#]
            [[~decl-sort _# [:id ~id] & children#]] tree#
            :else nil)))

(defn matcher-combination [matcher-applier tree & matcher-func]
  (reduce (fn [res-forest matcher-fn]
            (mapcat (fn [res-tree]
                      (matcher-applier res-tree matcher-fn))
                    res-forest))
          (list tree)
          matcher-func))

(defmacro matcher-decl-var [decl-var-id]
  `(fn [tree#]
     (match [tree#]
            [[:declVar _# [:id ~decl-var-id] & children#]] tree#
            :else nil)))
