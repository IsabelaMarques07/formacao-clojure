(ns hospital-v3.aula1
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(defn adiciona-paciente [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente não possui id" {:paciente paciente}))))

; { 15 [], 20 [], 25 [] }
(defn adiciona-visita [visitas, paciente, novas-vistas]
  (if (contains? visitas paciente)
    ;fazer update no vetor visitas com a chave paciente e realizar a função concat com as novas-visitas
    (update visitas paciente concat novas-vistas)
    (assoc visitas paciente novas-vistas))
  )

(defn imprime-relatorio-de-paciente [visitas, paciente]
  (println "Vistas do paciente" paciente "são" (get visitas paciente)))

;dessa forma, visitas está sempre vazio
(defn testa-uso-de-pacientes []
  (let [guilherme {:id 15 :nome "Guilherme"}
        daniela {:id 20 :nome "Daniela"}
        paulo {:id 25 :nome "Paulo"}
        ;uma variacao com reduce, mais natural
        pacientes (reduce adiciona-paciente {} [guilherme daniela paulo])
        ;uma variação com shadowing, fica feio
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
    ;o símbolo paciente é usado em vários lugares do programa com significados diferentes
    ;as vezes apenas o id, as vezes o paciente completo
    ;isso gera problemas e confusões no código
    (imprime-relatorio-de-paciente visitas daniela)
    ));retorna um mapa



;é possível redefinir o valor de visitas, com shadowing
;(defn testa-uso-de-pacientes []
;  (let [guilherme {:id 15 :nome "Guilherme"}
;        daniela {:id 20 :nome "Daniela"}
;        paulo {:id 25 :nome "Paulo"}
;        pacientes (reduce adiciona-paciente {} [guilherme daniela paulo])
;        visitas {}
;        visitas (adiciona-visita visitas 15 ["01/01/2019"])
;        visitas (adiciona-visita visitas 20 ["01/01/2019", "01/01/2020"])
;        visitas (adiciona-visita visitas 15 ["01/03/2019"])]
;    (pprint visitas)
;    ))
(testa-uso-de-pacientes)

;valida se é long
;(pprint (s/validate Long 15))
;retorna um erro
;(pprint (s/validate Long "guilherme"))

;apenas define uma função
;;macro que define uma função
;(s/defn teste-simples [x]
;        (println x))
;(teste-simples 15)
;(teste-simples "guilherme")

;pode-se dizer que o parâmetro segue o schema long
;da forma como está, por padrão não é validado
;;vai exibir os dados normalmente, mesmo sendo String
;(s/defn teste-simples-com-schema [x :- Long]
;        (println x))
;(teste-simples-com-schema 15)
;(teste-simples-com-schema "guilherme")

;para validar, precisa-se definir a validação explicitamente
(s/set-fn-validation! true)

;(s/defn teste-simples-com-schema [x :- Long]
;        (println x))
;(teste-simples-com-schema 15)
;(teste-simples-com-schema "guilherme")

(s/defn imprimi-relatorio-de-paciente
  [visitas, paciente :- Long]
  (println "Vistas do paciente" paciente "são" (get visitas paciente)))

;agora conseguimos o erro em tempo de execução que diz que o valor
;passado como parâmetro nao condiz com o schema Long
(testa-uso-de-pacientes)

(s/defn novo-paciente
        [id :- Long, nome :- s/Str]
        {:id id, :nome nome})

(pprint (novo-paciente 15 "Guilherme"))
(pprint (novo-paciente "Guilherme" 15))












