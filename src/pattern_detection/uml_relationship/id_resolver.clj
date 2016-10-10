(ns pattern-detection.uml-relationship.id-resolver
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as cj-str]))

(defn- build-qid [package-id]
  (fn [type-id] (str package-id "." type-id)))

(defn- get-id [id] (nth id 1))

(defn- get-type-id [type-decl]
  (get-id (nth type-decl 2)))

(defn- get-class-id [class-decl] (get-type-id class-decl))

(defn- get-interface-id [interface-decl] (get-type-id interface-decl))

(defn- stop-elements [tree]
  (or (keyword? tree) (string? tree)))

(defn- stop-matcher [tree-traverser tree & traverser-args]
  (if (or (empty? tree) (stop-elements (nth tree 0)))
    '()
    (mapcat #(apply tree-traverser % traverser-args) tree)))

(defn- get-qid [qid-sort]
  (nth qid-sort 1))

(defn- package-decl-p [grammar-sort]
  (= :packageDecl (nth grammar-sort 0)))

(defn- get-package-id [comp-unit]
  (if (package-decl-p (nth comp-unit 1))
    (get-qid (nth (nth comp-unit 1) 1))
    "$default"))

(defn- gather-decls [tree builder-qid]
  (match [tree]
         [[:classDecl & children]]
         (list {(builder-qid (get-class-id tree)) :class})
         [[:interfaceDecl & children]]
         (list {(builder-qid (get-interface-id tree)) :interface})
         :else (stop-matcher gather-decls (rest tree) builder-qid)))

(defn build-resolver [forest]
  (apply merge (mapcat #(gather-decls (:tree %)
                                      (build-qid (get-package-id (:tree %))))
                       forest)))
