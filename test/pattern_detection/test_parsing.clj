(ns pattern-detection.test-parsing
  (:require [clojure.test :refer :all]
            [clojure.core.match :refer [match]]
            [pattern-detection.parser.parse :refer [parse-proj]]
            [pattern-detection.uml-relationship.utils.traverser :refer [full-tree-matcher]]
            [clojure.java.io :as io]))

(def ^:const java-parsing-tests "toParseJava/")

(defn- has-children [node]
  (> (count node) 1))

(defn- match-classes [tree]
  (match [tree]
         [[:classDecl & children]] true
         :else nil))

(defn- match-interfaces [tree]
  (match [tree]
         [[:interfaceDecl & children]] true
         :else nil))

(defn- match-methods [tree]
  (match [tree]
         [[:methodDecl & children]] true
         :else nil))

(defn- match-methods-in-class [class-name]
  (fn [tree]
    (map
     #(full-tree-matcher % match-methods)
     (match [tree]
            [[:classDecl _ [:id class-name] [:declBody & decls]]]
            decls
            :else nil))))

(defn- get-id [qid-id-sort]
  (nth qid-id-sort 1))

(defn- match-class-extends [class-name]
  (fn [tree]
    (map get-id
         (match [tree]
                [[:classDecl _ [:id class-name] [:extends & qid] & children]]
                qid
                :else nil))))

(defn- match-class-implements [class-name]
  (fn [tree]
    (map get-id
         (match [tree]
                [[:classDecl _ [:id class-name] _ [:implements & qids] & children]]
                qids
                :else nil))))

(defn- match-interface-extends [interface-name]
  (fn [tree]
    (map get-id
         (match [tree]
                [[:interfaceDecl _ [:id interface-name] [:extends & qids] & children]]
                qids
                :else nil))))

(deftest test1-parsing
  "class/interface declaration with extends/implements.
   Methods with formal params, empty body."
  (let [parsed-tree (:tree (nth (parse-proj
                                 (io/resource
                                  (str java-parsing-tests
                                       "Simple.java")))
                                0))]
    (is (= (count (full-tree-matcher parsed-tree match-classes))
           2))
    (is (= (count (full-tree-matcher parsed-tree match-interfaces))
           3))
    (is (= (count (flatten
                   (full-tree-matcher
                    parsed-tree
                    (match-methods-in-class "Simple"))))
           6))
    (is (= (flatten
            (full-tree-matcher
             parsed-tree
             (match-class-extends "EmptyPrivateClass")))
           '("PrivateStaticClass")))
    (is (= (flatten
            (full-tree-matcher
             parsed-tree
             (match-class-implements "EmptyPrivateClass")))
           '("MyInterface" "AnotherInterface")))
    (is (= (flatten
            (full-tree-matcher
             parsed-tree
             (match-interface-extends "AnotherInterface")))
           '("Inter")))))

