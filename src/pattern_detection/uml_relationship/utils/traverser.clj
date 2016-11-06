(ns pattern-detection.uml-relationship.utils.traverser)

(defn- flatten-only-list [x]
  (filter (complement seq?)
          (rest (tree-seq seq? seq x))))

(defn- full-tree-matcher-helper [tree matcher-function]
  (filter some?
          (mapcat (fn [local-tree]
                    (if (coll? local-tree)
                      (concat (list (matcher-function local-tree))
                              (full-tree-matcher-helper local-tree matcher-function))))
                  tree)))

(defn full-tree-matcher [tree matcher-function]
  (flatten-only-list (full-tree-matcher-helper tree matcher-function)))

(defn full-tree-rebuilder [tree replacer-function]
  (if (coll? tree)
    (into [] (map #(full-tree-rebuilder % replacer-function) (replacer-function tree)))
    tree))
