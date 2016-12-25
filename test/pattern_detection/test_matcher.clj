(ns pattern-detection.test-matcher
  (:require [pattern-detection.matcher.matcher :refer [graph-to-layers analyse-layers]]
            [loom.graph :refer [digraph]]
            [clojure.test :refer :all]))

(deftest test1-layers
  (testing
      "simple test for splitting edges of graph to layers"
    (let [graph (digraph {:a [:c :b] :b [:c :d] :c [:d]})
          expected-res (set '(([:a :c] [:a :b]) ([:c :d] [:b :c] [:b :d]) ([:c :d])))]
      (is (= (set (graph-to-layers graph)) expected-res)))))

(defn- exact-detection-test-template
  [sys-layer dp-layer expected-result]
  (is (= (set ((analyse-layers dp-layer sys-layer) :exact))
         (set expected-result))))

(deftest test1-detection
  (testing
      "simple test for exact detection between layers"
    (exact-detection-test-template
     '(([:a :c] [:a :b]) ([:c :d] [:b :c] [:b :d]) ([:c :d]))
     '(([:a :b]) ([:b :c]))
     '({:a :a, :b :c, :c :d}
       {:a :a, :b :b, :c :c}
       {:a :a, :b :b, :c :d}))))

(deftest test2-detection
  (testing
      "simple test for missing exact detection between layers"
    (exact-detection-test-template
     '(([:a :c] [:a :b]) ([:c :d] [:b :c] [:b :d]) ([:c :d]))
     '(([:a :b] [:c :d]))
     '())))

(deftest test3-detection
  (testing
      "simple test for exact detection between identical layers"
    (exact-detection-test-template
     '(([:a :b] [:c :d]))
     '(([:a :b] [:c :d]))
     '({:a :a, :b :b, :c :c, :d :d} {:a :c, :b :d, :c :a, :d :b}))))

(deftest test4-detection
  (testing
      "simple test for showing combinations of exact detection between layers"
    (exact-detection-test-template
     '(([:a :b] [:c :d] [:e :f]))
     '(([:a :b] [:c :d]))
     '({:a :e, :b :f, :c :c, :d :d}
       {:a :a, :b :b, :c :e, :d :f}
       {:a :a, :b :b, :c :c, :d :d}
       {:a :c, :b :d, :c :a, :d :b}
       {:a :e, :b :f, :c :a, :d :b}
       {:a :c, :b :d, :c :e, :d :f}))))

(deftest test5-detection
  (testing 
      "another simple test for showing combinations of exact detection between layers"
    (exact-detection-test-template
     '(([:a :c] [:b :c]))
     '(([:d :f] [:e :f]))
     '({:d :a, :e :b, :f :c}
       {:e :a, :d :b, :f :c}))))

(deftest test6-detection
  (testing
      (exact-detection-test-template
       '(([:a :b] [:a :c]) ([:b :d] [:b :c]))
       '(([:e :f]) ([:f :g]))
       '({:e :a, :f :b, :g :d}
         {:e :a, :f :b, :g :c}))))

(deftest test7-detection
  (testing
      (exact-detection-test-template
       '(([:a :b]) ([:b :c]))
       '(([:a :b]) ([:b :c]) ([:c :d]))
       '({:a :a, :b :b, :c :c}))))

;; TODO: fix me
(deftest test8-detection
  (testing
      (exact-detection-test-template
       '(([:a :b]) ([:b :c]))
       '(([:a1 :b1]) ([:b1 :c1]) ([:c1 :d1]))
       '())))
