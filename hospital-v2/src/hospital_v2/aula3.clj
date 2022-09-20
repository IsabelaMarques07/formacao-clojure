(ns hospital-v2.aula3
  (:use clojure.pprint)
  (:require [hospital-v2.logic :as h.logic]))

;cache
;serviço - carregar dados dos pacientes pode ser um processo lerdo

(defn carrega-paciente [id]
  (println "Carregando" id)
  (Thread/sleep 1000)
  { :id id, :carregado-em (h.logic/agora)})
;
;(pprint (carrega-paciente 15))
;(pprint (carrega-paciente 16))

;;carregar paciente somente se eu ainda não carreguei
;(defn carrega-se-nao-existe
;  [pacientes id]
;  (if (contains? pacientes id)
;    pacientes
;    ;dessa forma está atrelado a função carrega-paciente, ou seja, não está muito genérico
;    ;normalmente, a função é passada como parâmetro
;    (let [paciente (carrega-paciente id)]
;      (assoc pacientes id paciente))))


;carregar paciente somente se eu ainda não carreguei
;por passar a função comno parâmetro, o nome 'pacientes' passa a não fazer mais sentido
;por isso mudamos para cache
;função pura - se chamar ela, não causa efeito colateral
;vantagens: pode usar nos atomos, chamar duas vezes, etc.
(defn carrega-se-nao-existe
  [chache id carregadora]
  (if (contains? chache id)
    chache
    ;normalmente, a função é passada como parâmetro
    (let [paciente (carregadora id)]
      (assoc chache id paciente))))
;
;;passa os pacientes como vazio e pede para carregar o 15 - deve carregar e imprimir o valor
;(pprint (carrega-se-nao-existe {}, 15, carrega-paciente))
;
;;passa os pacientes já contendo o 15 e pede para carregar o 15 novamente - não deve imprimir o valor
;(pprint (carrega-se-nao-existe { 15 { :id 15 } }, 15, carrega-paciente))


;a função de cache pode ser inicializada, pode ser encapsulada em um objeto/componente
(defprotocol Carregavel
  (carrega! [this id]))                                     ; dessa forma, isolou o comportamento

(defrecord Cache
  ;cache é o atomo
  [cache carregadora]
  Carregavel
  ;da maneira como está sendo feito, a função carrega vai alterar o cache, por isso o !
  (carrega! [this id]
    ;"swapar" nosso cache chamando a função carrega se não existe
    ;depois de carregar o carrega-se-nao-existe, muda o valor do cache
    (swap! cache carrega-se-nao-existe id carregadora);aqui temos a garantia que o cache está atualizado
    ;como o cache é um átomo, precisamos dereferenciar (@)
    (get @cache id)))


(def pacientes (->Cache (atom {}), carrega-paciente))
(pprint pacientes)

(carrega! pacientes 15)
(carrega! pacientes 30)
(carrega! pacientes 120)
(carrega! pacientes 15)
(pprint pacientes)




















