(defproject pattern-detection "0.1.0-SNAPSHOT"
  :description "tool for parsing source code and maybe detection design patterns"
  :url "https://github.com/dimitriylol/pattern-detection"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [instaparse "1.4.1"]
                 [org.clojure/core.match "0.3.0-alpha4"]]
  :main ^:skip-aot pattern-detection.core
  :target-path "target/%s"
  :resource-paths ["resources"]
  :profiles {:uberjar {:aot :all}})
