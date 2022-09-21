(ns hospital-v3.aula5
  (:use clojure.pprint)
  (:require [schema.core :as s]))

;ativar validação
(s/set-fn-validation! true)

(def PosInt (s/pred pos-int? 'inteiro-positivo))
(def Plano [s/Keyword])
(def Paciente {:id PosInt, :nome s/Str, :plano Plano, (s/optional-key :nascimento) s/Str})

(def Pacientes
  {PosInt Paciente})

(def Visitas
  {PosInt [s/Str]})
;if e throw removidos pois o schema já garantiu o a EXISTÊNCIA e a VALIDADE do id
; ...se a validação estiver ativa :)
(s/defn adiciona-paciente :- Pacientes
  [pacientes :- Pacientes,  paciente :- Paciente]
  (let [id (:id paciente)]
    (assoc pacientes id paciente)
    ))

(s/defn adiciona-visita :- Visitas
  [visitas :- Visitas, paciente :- PosInt, novas-vistas :- [s/Str]]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-vistas)
    (assoc visitas paciente novas-vistas))
  )

(s/defn imprime-relatorio-de-paciente [visitas :- Visitas, paciente :- PosInt]
  (println "Vistas do paciente" paciente "são" (get visitas paciente)))

(defn testa-uso-de-pacientes []
  (let [guilherme {:id 15 :nome "Guilherme", :plano []}
        daniela {:id 20 :nome "Daniela", :plano []}
        paulo {:id 25 :nome "Paulo", :plano []}
        pacientes (reduce adiciona-paciente {} [guilherme daniela paulo])
        visitas {}
        visitas (adiciona-visita visitas 15 ["01/01/2019"])
        visitas (adiciona-visita visitas 20 ["01/01/2019", "01/01/2020"])
        visitas (adiciona-visita visitas 15 ["01/03/2019"])]
    (pprint pacientes)
    (pprint (adiciona-visita visitas 15 ["01/01/2019"]))
    (pprint (adiciona-visita visitas 20 ["01/01/2019", "01/01/2020"]))
    (pprint (adiciona-visita visitas 15 ["01/03/2019"]))
    (pprint [guilherme paulo daniela]);retorna um vetor
    (pprint visitas)

    (imprime-relatorio-de-paciente visitas 15)
    ))
(testa-uso-de-pacientes)









