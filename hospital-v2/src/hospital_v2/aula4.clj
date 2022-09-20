(ns hospital-v2.aula4
  (:use clojure.pprint))

(defrecord PacienteParticular [id, nome, nascimento, situacao])
(defrecord PacientePlanoDeSaude [id, nome, paciente,situacao, plano])


(defprotocol Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]))

(defn nao-eh-urgente? [paciente]
  (not= :urgente (:situacao paciente :normal)))

(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (and (>= valor 50) (nao-eh-urgente? paciente))))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (let [plano (:plano paciente)]
      ;ninguém dentro de procedimento pode ser igual a raio-x
      (and (not (some #(= % procedimento) plano)) (nao-eh-urgente? paciente)))))

(let [particular (->PacienteParticular 15, "Guilherme", "18/9/1981", :normal)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "19/9/1981",:normal, [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 40))
  (pprint (deve-assinar-pre-autorizacao? plano, :raio-x, 48542))
  (pprint (deve-assinar-pre-autorizacao? plano, :coleta-de-sangue, 5454654)))



(let [particular (->PacienteParticular 15, "Guilherme", "18/9/1981", :urgente)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "19/9/1981",:urgente, [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 40))
  (pprint (deve-assinar-pre-autorizacao? plano, :raio-x, 48542))
  (pprint (deve-assinar-pre-autorizacao? plano, :coleta-de-sangue, 5454654)))



;a classe vai definir o tipo do que está sendo passado como parâmetro
;class não é parâmetro, pode ser uma função
(defmulti deve-assinar-pre-autorizacao-multi? class)
;eu tenho uma função -: deve-assinar-pre-autorizacao-multi
;ela usa a classe do parametro como base para definir se vai usar um method ou outro
(defmethod deve-assinar-pre-autorizacao-multi? PacienteParticular [paciente]
  (println "Invocando paciente particular")
  true)
(defmethod deve-assinar-pre-autorizacao-multi? PacientePlanoDeSaude [paciente]
  (println "Invocando paciente de plano de saúde")
  false)

(let [particular (->PacienteParticular 15, "Guilherme", "18/9/1981", :urgente)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "19/9/1981",:urgente, [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao-multi? particular))
  (pprint (deve-assinar-pre-autorizacao-multi? plano)))


;explorando como funciona a funcao que define a estrategia de um defmulti
(defn minha-funcao [p]
  (println p)
  (class p))

(defmulti multi-teste minha-funcao)
;(multi-teste "guilherme")
;(multi-teste :guilherme)


;pedido { :paciente paciente, :valor valor, :procedimento procedimento }

;um pouco feio, pois mistura keyword e classe como chave
;é uma função tradicional, pdoe-se testar
(defn tipo-de-autorizador [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)
        urgencia? (= :urgente situacao)]
    (if urgencia?
      :sempre-autorizado
      (class paciente))
    )
  )
;o defmulti permite que se crie definidor de estratégias que são testáveis
;como é o caso do tipo-de-autorizador que é uma função tradicional
(defmulti deve-assinar-pre-autorizacao-do-pedido? tipo-de-autorizador)
(defmethod deve-assinar-pre-autorizacao-do-pedido? :sempre-autorizado [pedido]
  false)
(defmethod deve-assinar-pre-autorizacao-do-pedido? PacienteParticular [pedido]
  (>= (:valor pedido 0 )50))

(defmethod deve-assinar-pre-autorizacao-do-pedido? PacientePlanoDeSaude [pedido]
  (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))


(let [particular (->PacienteParticular 15, "Guilherme", "18/9/1981", :normal)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "19/9/1981",:normal, [:raio-x, :ultrassom])]
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente particular, :valor 1000, :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar-pre-autorizacao-do-pedido?  {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))















