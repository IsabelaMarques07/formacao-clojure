(ns hospital.aula4
  (:use [clojure pprint])
  (:require [hospital.logic :as h.logic]
            [hospital.model :as h.model]))

(defn chega-em-malvado! [hospital pessoa]
  (swap! hospital h.logic/chega-em-pausado :espera pessoa)
  (println "apos inserir" pessoa)
  )


(defn simula-um-dia-em-paralelo
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]]
  (mapv #(.start (Thread. (fn [] (chega-em-malvado! hospital %)))) pessoas)
  (.start (Thread. (fn [] (Thread/sleep 8000)
                     (pprint hospital))))))

(simula-um-dia-em-paralelo)
