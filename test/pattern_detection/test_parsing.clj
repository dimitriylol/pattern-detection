(ns pattern-detection.test-parsing
  (:require [clojure.test :refer :all]
            [pattern-detection.parser.parse :refer [parse-proj]]
            [pattern-detection.uml-relationship.utils.traverser :refer [full-tree-matcher]]
            [pattern-detection.uml-relationship.utils.match-rules :refer :all]
            [clojure.java.io :as io]))

(def ^:const java-parsing-tests "parse_java/")

(defn- has-children [node]
  (> (count node) 1))

(defn- first-tree [forest]
  (:tree (nth forest 0)))

(defn- parse-test-file [file-name]
  (first-tree (parse-proj (io/resource (str java-parsing-tests file-name)))))

(deftest test1-parsing
  "class/interface declaration with extends/implements.
   Methods with formal params, empty body."
  (let [parsed-tree (parse-test-file "Simple.java")]
    (is (= (count (full-tree-matcher
                   parsed-tree (matcher :classDecl)))
           2))
    (is (= (count (full-tree-matcher
                   parsed-tree (matcher :interfaceDecl)))
           3))
    (is (= (count (matcher-combination
                   full-tree-matcher
                   parsed-tree
                   (match-decl-with-id :classDecl "Simple")
                   (matcher :methodDecl)))
           6))
    (is (= (matcher-combination
            full-tree-matcher
            parsed-tree
            (match-decl-with-id :classDecl "EmptyPrivateClass")
            (matcher :extends)
            (matcher :qid))
           '([:qid "PrivateStaticClass"])))
    (is (= (matcher-combination
            full-tree-matcher
            parsed-tree
            (match-decl-with-id :classDecl "EmptyPrivateClass")
            (matcher :implements)
            (matcher :qid))
           '([:qid "MyInterface"]
             [:qid "AnotherInterface"])))
    (is (= (matcher-combination
            full-tree-matcher
            parsed-tree
            (match-decl-with-id :interfaceDecl "AnotherInterface")
            (matcher :extends)
            (matcher :qid))
           '([:qid "Inter"])))))

(deftest test2-parsing
  "declaration attributes and localVariables.
   Possible initialization by primitive values"
  (let [parsed-tree (parse-test-file "AttributeLocalVariable.java")]
    (is (= (count (full-tree-matcher parsed-tree (matcher :attrDecl)))
           4))
    (is (= (count (full-tree-matcher parsed-tree (matcher :localVar)))
           5))
    (is (= (matcher-combination
            full-tree-matcher
            parsed-tree
            (matcher :attrDecl)
            (matcher-decl-var "staticPublicInt")
            (matcher :primitiveVal))
           '([:primitiveVal "0"])))
    (is (= (matcher-combination
            full-tree-matcher
            parsed-tree
            (matcher :attrDecl)
            (matcher-decl-var "preciseLong")
            (matcher :initVal))
           '()))    
    (is (= (some #(= (nth % 1) "var")
                 (matcher-combination
                  full-tree-matcher
                  parsed-tree
                  (matcher :attrDecl)
                  (matcher :id)))
           nil))
    (is (= (matcher-combination
            full-tree-matcher
            parsed-tree
            (matcher :localVar)
            (matcher-decl-var "var")
            (matcher-rule [:type [:qid "float"]]))
           '([:type [:qid "float"]])))
    (is (= (matcher-combination
            full-tree-matcher
            parsed-tree
            (matcher :localVar)
            (matcher-decl-var "privateLong"))
           '()))))

