(ns pattern-detection.uml-relationship.qualify-id
  (:require [clojure.core.match :refer [match]]
            [pattern-detection.uml-relationship.id-resolver :refer [build-resolver]]
            [pattern-detection.uml-relationship.utils.traverser :refer [full-tree-rebuilder,
                                                                        full-tree-matcher]]
            [pattern-detection.uml-relationship.utils.match-rules :refer [matcher]]))

(defn- qualify [qid out-types]  
  (let [res (filter (fn [qid-type] (.contains qid-type qid)) out-types)]
    (if (= (count res) 1)
      (nth res 1)
      (println (str "WARNING! resolution messed up on " qid ", so it will be nil")))))

(defn- imported-types [tree resolver]
  (flatten
   (map (fn [[_ qid asterisk]]
          (if asterisk
            (map (fn [[qid-type]] qid-type) (find resolver qid))
            qid))
        (full-tree-matcher tree (matcher :importId)))))

(defn- resolve-qid [tree resolver]
  (let [out-types (imported-types tree resolver)]
    (full-tree-rebuilder tree (fn [local-tree]
                                (match [local-tree]
                                       [[:qid qid-str]]
                                       [:qid (qualify qid-str out-types)]
                                       :else local-tree)))))

(defn make-id-qualified [forest]
  (let [resolver (build-resolver forest)]
    (map #(resolve-qid (:tree %) resolver)
         forest)))
