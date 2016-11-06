(ns pattern-detection.parser.JavaAST)

(defn build-qid [package-id type-id]
  (str package-id "." type-id))

(defn get-id [id]
  (nth id 1))

(defn get-qid [qid-sort]
  (nth qid-sort 1))

(defn get-type-id [type-decl]
  (get-id (nth type-decl 2)))

(defn- package-decl-p [grammar-sort]
  (= :packageDecl (nth grammar-sort 0)))

(defn get-package-id [comp-unit]
  (if (package-decl-p (nth comp-unit 1))
    (get-qid (nth (nth comp-unit 1) 1))
    "$default"))
