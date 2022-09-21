(ns hospital-v3.aula2
  (:use clojure.pprint)
  (:require [schema.core :as s]))

;ativar validação
(s/set-fn-validation! true)

;é possível criar um Record extendido com Schema
;(s/defrecord Paciente
;             [id :- Long, nome :- s/Str])

;(pprint (Paciente. 15 "Guilherme"))
;(pprint (Paciente. "15" "Guilherme"))

;o Schema é um mapa com os dados e os tipos
(def Paciente
  "Schema de um paciente"
  {:id s/Num, :nome s/Str})

;explica o que é esse Schema
(pprint (s/explain Paciente))

(pprint (s/validate Paciente {:id 15, :nome "Guilherme"}))

;retorna um erro
;type é pego pelo schema, mas poderíamos argumentar que esse tipo de erro
;seria pego em testes automatizados com cobertura boa
; mas entra a questão de querer ser forward compatible OU NÃO
;entender  esse trade-of
;sistemas externos não me quebrarão ao adicionar campos novos (forward compatible)
;no nosso validate  não estamos sendo forward compatible (pode ser interessante quando quero analisar mudanças)
;(pprint (s/validate Paciente {:id 15, :name "Guilherme"}))
;(pprint (s/validate Paciente {:id 15, :name "Guilherme", :plano :saude}))
;chaves que são keywords em schemas são por padrão obrigatórias
;(pprint (s/validate Paciente {:id 15}))

;é possível validar o que uma função vai retornar
;força a validação na saída da função
;dessa forma, é possível testar a entrada e a saída da função

;;nesse caso retorna erro
;(s/defn novo-paciente :- Paciente
;        [id :- Long, nome :- s/Str]
;        {:id id, :nome nome, :plano [] })

;nesse caso tem um retorno válido
(s/defn novo-paciente :- Paciente
        [id :- Long, nome :- s/Str]
        {:id id, :nome nome})

(pprint (novo-paciente 15 "Guilherme"))

;função simples, fácil de testar e pura
(defn estritamente-positivo? [x]
  (> x 0))

;definição do schema - a função anterior precisa dar verdadeiro
;funções do tipo são chamadas de predicates/predicados
;o terceiro argumento da função é para que esteja escrito apenas 'estritamente-positivo na mensagem
(def EstritamentePositivo (s/pred estritamente-positivo? 'estritamente-positivo))

;(pprint (s/validate EstritamentePositivo 0))


;id além de ser inteiro, ser estritamente positivo
;restringir (constrained) aos números positivos
(def Paciente
  "Schema de um paciente"
  {:id (s/constrained s/Int estritamente-positivo?), :nome s/Str})

;função de estritamente positivo já existe (pos)
(def Paciente
  "Schema de um paciente"
  {:id (s/constrained s/Int pos?), :nome s/Str})

(pprint (s/validate Paciente {:id -2, :nome "Guilherme"}))


















