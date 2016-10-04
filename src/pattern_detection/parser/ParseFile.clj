(ns pattern-detection.parser.ParseFile)

(defprotocol ParseFile
  "The protocol for file that can be parsed"
  (parse-src [this] "method for getting AST of file"))
