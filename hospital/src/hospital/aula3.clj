(ns hospital.aula3
 (:use [clojure pprint])
  (:require [hospital.logic :as h.logic]
            [hospital.model :as h.model]))

;o hospital-silveira é uma casca que permite mudar o mapa que está lá dentro
;atom faz isso
;(def hospital-silveira (atom {}))

(defn testa-atomao []
  (let [hospital-silveira (atom {:espera h.model/fila_vazia})]
    (println hospital-silveira)
    (pprint hospital-silveira)
    (println (deref hospital-silveira))
    (pprint @hospital-silveira)

    (pprint (assoc @hospital-silveira :laboratorio1 h.model/fila_vazia))
    (pprint @hospital-silveira)

    (swap! hospital-silveira assoc :laboratorio1 h.model/fila_vazia)
    (pprint @hospital-silveira)
    (swap! hospital-silveira assoc :laboratorio2 h.model/fila_vazia)
    (pprint @hospital-silveira)

    ; ataualiza o mapa @hospital na chave lab1 com a função conj com a pessoa 111
    (update @hospital-silveira :laboratorio1 conj "111")
    (pprint @hospital-silveira)

    ; ataualiza o mapa @hospital na chave lab1 com a função conj com a pessoa 111
    (swap! hospital-silveira update :laboratorio1 conj "111")
    (pprint @hospital-silveira)
    ))

(testa-atomao)
