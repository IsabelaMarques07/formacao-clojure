(ns hospital-v2.aula2
  (:use clojure.pprint))

(defrecord Paciente [id, nome, nascimento])

;Paciente Plano de Saúde ===> + plano de saúde
;Paciente Particular ===> + 0

;caminho não muito recomendado, com prováveis problemas e tipo n^2
;(defrecord PacientePlanoDeSaúde HERDA Paciente [plano])

;digitação não é o maior problema nesse caso
(defrecord PacienteParticular [id, nome, nascimento])
(defrecord PacientePlanoDeSaude [id, nome, nascimento, plano])

;REGRAS DIFERENTES PARA TIPOS DIFERENTES
;deve-assinar-pre-autorizacao?
; Particular ===> valor >= 50
; PlanoDeSaude ===> procedimento NAO esta no plano

;VANTAGEM: Tudo no mesmo lugar
;DESVANTAGEM: Tudo no mesmo lugar
;(defn deve-assinar-pre-autorizacao? [paciente procedimento valor]
;  (if (= PacienteParticular (type paciente))
;    (>= valor 50)
;    ;assumindo que existe os dois tipos
;    (if (= PacientePlanoDeSaide (type paciente))
;      (let [plano (get paciente :plano)]
;        (not (some #(= % procedimento) plano)))
;      true)))


(defprotocol Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]))

(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (>= valor 50)))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (let [plano (:plano paciente)]
      ;ninguém dentro de procedimento pode ser igual a raio-x
      (not (some #(= % procedimento) plano)))))
;o contains verifica indice, dessa forma você fica dependendendo da estrutura de dados
;vetor é numérico

(let [particular (->PacienteParticular 15 "Guilherme", "18/9/1981")
      plano (->PacientePlanoDeSaude 15, "Guilherme", "19/9/1981", [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 10))
  (pprint (deve-assinar-pre-autorizacao? plano, :raio-x, 10)))


;Uma variação baseada na palestra a seguir, mas com extend-type e com greagorian Calendar
; From Sean Devlin's talk on protocols at Clojure Conj
(defprotocol Dateable
  (to-ms [this]))

; para o tipo number
(extend-type java.lang.Number
  Dateable
  (to-ms [this] this))
(pprint (to-ms 56))

;para o tipo Date
(extend-type java.util.Date
  Dateable
  (to-ms [this] (.getTime this)))
(pprint (to-ms (java.util.Date.)))

;para o tipo Calendar
(extend-type java.util.Calendar
  Dateable
  ;o getTime do calendar retorna um Date, assim chamamos um getTime para esse Date e ele retorna em ms
  (to-ms [this] (to-ms (.getTime this))))
(pprint (to-ms (java.util.GregorianCalendar.)))








