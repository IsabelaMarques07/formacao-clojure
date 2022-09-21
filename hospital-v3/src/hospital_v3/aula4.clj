(ns hospital-v3.aula4
  (:use clojure.pprint)
  (:require [schema.core :as s]))

;ativar validação
(s/set-fn-validation! true)

(def PosInt (s/pred pos-int? 'inteiro-positivo))
(def Plano [s/Keyword])
;quando é colocado um : antes da chave da função, esse keyword é obrigatória
;é possível colocar explicitamente como opcional, porém será necessário validar se essa chave
;existe várias vezes durante o código (optional-key)
(def Paciente {:id PosInt, :nome s/Str, :plano Plano, (s/optional-key :nascimento) s/Str})

(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x, :ultrassom]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano []}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano nil, :nascimento "07/10/2002"}))

;esse é um outro tipo de uso de mapas no mundo real
;Pacientes
;{ 15 {PACIENTE} 32 {PACIENTE}
(def Pacientes
  {PosInt Paciente})

(pprint (s/validate Pacientes {}))

(let [guilherme {:id 15, :nome "Guilherme", :plano [:raio-x]}
      daniela {:id 15, :nome "Guilherme", :plano []}]
  (pprint (s/validate Pacientes {15 guilherme}))
  (pprint (s/validate Pacientes {15 daniela}))
  ;(pprint (s/validate Pacientes {-15 guilherme}))
  ;(pprint (s/validate Pacientes {15 15}))
  ;(pprint (s/validate Pacientes {15 {15 "Guilherme"}}))

  )














