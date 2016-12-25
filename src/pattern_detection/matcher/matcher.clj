(ns pattern-detection.matcher.matcher
  (:require [loom.graph :refer [edges, in-degree, nodes, out-edges, dest]])
  (:require [clojure.math.combinatorics :refer [combinations, permutations]]))

(defn root-nodes [graph]
  (filter #(= (in-degree graph %) 0) (nodes graph)))

(defn get-out-edges [graph nodes]
  (remove empty? (mapcat #(out-edges graph %) nodes)))

(defn get-destinations [layer-edges]
  (into #{} (map dest layer-edges)))

(defn get-layers [graph nodes]
  (butlast (loop [nodes nodes
                  layers '()]
             (if (empty? nodes)
               layers
               (let [layer-edges (get-out-edges graph nodes)]
                 (recur (get-destinations layer-edges)
                        (concat layers (list layer-edges))))))))

(defn graph-to-layers [graph]
  (get-layers graph (root-nodes graph)))

(defn layers-for-relationship [subsytem-graph dp-graph kind]
  [ kind {:model (graph-to-layers (get subsytem-graph kind))
          :design-pattern (graph-to-layers (get dp-graph kind))}])

(defn- correspondence-elem [dp-elem model-elem index]
  (list (nth dp-elem index) (nth model-elem index)))

;; TODO: refactor overlay checking
(defn- overlay-key-helper [correspondence-el dp-el model-el index]
  (and (= (nth correspondence-el 0) (nth dp-el index))
       (not= (nth correspondence-el 1) (nth model-el index))))

(defn- overlay-key? [correspondence-el dp-el model-el]
  (or (overlay-key-helper correspondence-el dp-el model-el 1)
      (overlay-key-helper correspondence-el dp-el model-el 0)))

(defn- overlay-value-helper [correspondence-el dp-el model-el index]
  (and (= (nth correspondence-el 1) (nth model-el index))
       (not= (nth correspondence-el 0) (nth dp-el index))))

(defn overlay-value? [correspondence-el dp-el model-el]
  (or (overlay-value-helper correspondence-el dp-el model-el 1)
      (overlay-value-helper correspondence-el dp-el model-el 0)))

(defn pair-overlay? [correspondence-map dp-elem model-elem]
  (some #(or (overlay-value? % dp-elem model-elem)
             (overlay-key? % dp-elem model-elem))
        (into [] correspondence-map)))

(defn add-correspondence [correspondence-map dp-elem model-elem]
  (apply assoc
         correspondence-map
         (concat (correspondence-elem dp-elem model-elem 0)
                 (correspondence-elem dp-elem model-elem 1))))

(defn correspondence-map [dp-layer model-combination]
  (loop [dp-layer dp-layer
         model-combination model-combination
         correspondence-map {}]
    (if (empty? dp-layer) 
      correspondence-map
      (let [dp-elem (first dp-layer)
            model-elem (first model-combination)]
        (if (pair-overlay? correspondence-map dp-elem model-elem)
          (recur '() '() (assoc correspondence-map :error true))
          (recur (next dp-layer)
                 (next model-combination)
                 (add-correspondence correspondence-map
                                     dp-elem
                                     model-elem)))))))

(defn possible-correspondence [dp-layer model-layer]
  (map #(correspondence-map dp-layer %)
       (mapcat permutations 
               (combinations model-layer (count dp-layer)))))

(defn throwable-merge-with [map1 map2]
  (try 
    (merge-with (fn [val1 val2] (if (= val1 val2)
                                  val1
                                  (throw (Throwable.))))
                map1 map2)
    (catch Throwable e (merge map1 {:error true}))))

(defn safe-merge-layers [layer next-layer]
  (if (empty? next-layer)
    layer
    (mapcat (fn [layer-el]
              (map (fn [next-layer-el]
                     (throwable-merge-with layer-el next-layer-el))
                   next-layer))
            layer)))

(defn merge-correspondence [correspondence-layers]
  (loop [correspondence-layer (first correspondence-layers)
         rest-layers (rest correspondence-layers)]
    (if (empty? rest-layers)
      correspondence-layer
      (recur (safe-merge-layers correspondence-layer
                                (first rest-layers))
             (rest rest-layers)))))

(defn analyse-layers [dp-layers model-layers]
  (let [correspondence-maps (merge-correspondence
                             (map possible-correspondence dp-layers model-layers))]
    {:exact (filter (comp nil? :error) correspondence-maps)
     :possible (filter #(and (% :error) (> (count %) 1)) correspondence-maps)
     :absent (filter empty? correspondence-maps)}))

(defn- find-design-pattern [dp-graphs subsystem-graphs]
  (map #(analyse-layers (graph-to-layers %1)
                        (graph-to-layers %2))
       dp-graphs
       subsystem-graphs))

(defn find-exact-matching [dp-graphs subsystem-graphs]
  (let [relationships-exact-matching (map :exact (find-design-pattern subsystem-graphs dp-graphs))]
    (filter (comp nil? :error)
            (merge-correspondence (filter not-empty relationships-exact-matching)))))
