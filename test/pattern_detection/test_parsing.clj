(ns pattern-detection.test-parsing
  (:require [clojure.test :refer :all]
            [pattern-detection.parser.parse :refer [parse-proj]]
            [pattern-detection.uml-relationship.utils.traverser :refer [full-tree-matcher]]
            [pattern-detection.uml-relationship.utils.match-rules :refer :all]
            [clojure.java.io :as io]))

(def ^:const java-parsing-tests "toParseJava/")

(defn- has-children [node]
  (> (count node) 1))

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

