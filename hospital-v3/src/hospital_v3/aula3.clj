(ns hospital-v3.aula3
  (:use clojure.pprint)
  (:require [schema.core :as s])
  (:import (javax.swing.plaf.basic BasicSplitPaneDivider$MouseHandler)))

;ativar validação
(s/set-fn-validation! true)

(def PosInt (s/pred pos-int? 'inteiro-positivo))

(def Paciente
  {:id PosInt, :nome s/Str})

(s/defn novo-paciente :- Paciente
        [id :- PosInt
         nome :- s/Str]
        {:id id, :nome nome})

(pprint (novo-paciente 15, "Guilherme"))
;(pprint (novo-paciente -15, "Guilherme"))

(defn maior-ou-igual-a-zero? [x] (>= x 0))
(def ValorFinanceiro (s/constrained s/Num maior-ou-igual-a-zero?))

(def Pedido
  {:paciente      Paciente
   :valor         ValorFinanceiro
   :procedimento  s/Keyword}
  )
;faz sentido mini schemas?
;(def Procedimento s/Keyword)

(s/defn novo-pedido :- Pedido
        [paciente  :- Paciente, valor :- ValorFinanceiro, procedimento :- s/Keyword]
        {:paciente paciente, :valor valor, :procedimento procedimento})

(pprint (novo-pedido (novo-paciente 15 "Guilherme"), 15.53, :raio-x))
;(pprint (novo-pedido (novo-paciente 15 "Guilherme"), -15.53, :raio-x))

;vetor de números
(def Numeros [s/Num])
(pprint (s/validate Numeros [15]))
(pprint (s/validate Numeros [15, 4, 6, 7, 56, 3]))
(pprint (s/validate Numeros [0]))
;vetor de nil não é número
;(pprint (s/validate Numeros [nil]))
(pprint (s/validate Numeros []))
;um nil é como se fosse uma sequencia vazia
(pprint (s/validate Numeros nil))



;vetor do tipo Keyword
(def Plano [s/Keyword])
(pprint (s/validate Plano [:raio-x]))

(def Paciente
  {:id PosInt, :nome s/Str, :plano Plano})

(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x, :ultrassom]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano []}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano nil}))
;plano é uma keyword obrigatória no mapa, mas pode ter valor vazio
;(pprint (s/validate Paciente {:id 15, :nome "Guilherme"}))



















