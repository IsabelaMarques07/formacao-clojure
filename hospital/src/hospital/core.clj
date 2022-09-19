(ns hospital.core
  (:require [hospital.model :as h.model])
  (:use [clojure.pprint]))

; espera FILA DE ESPERA
;laboratorio1
;laboratorio2
;laboratorio3


(let [hospital-da-isa (h.model/novo-hospital)]
  (pprint hospital-da-isa))

(pprint h.model/fila-vazia)