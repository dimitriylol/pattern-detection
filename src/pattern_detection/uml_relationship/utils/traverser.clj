(ns pattern-detection.uml-relationship.utils.traverser)

(defn full-tree-matcher [tree matcher-function]
  (filter some?
          (mapcat (fn [local-tree]
                    (if (coll? local-tree)
                      (concat (list (matcher-function local-tree))
                              (full-tree-matcher local-tree matcher-function))
                      '()))
                  tree)))
