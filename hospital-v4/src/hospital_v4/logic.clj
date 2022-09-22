(ns hospital-v4.logic
  (:require [hospital-v4.model :as h.model]
            [schema.core :as s]))

;Test Driven Development
;Teste Driven Design
;Os testes indicam o caminho do código

;(defn cabe-na-fila?
;  [hospital departamento]
;  (-> hospital
;      departamento
;      ;dessa forma, o count retorna 0 quando o departamento é nil
;      ;assim, ele aceita um departamento nulo e não apresenta erro
;      count
;      (< 5)))


;(defn cabe-na-fila?
;  [hospital departamento]
;  ;(count (get hospital departamento))
;  (when-let [fila (get hospital departamento)]
;    (-> fila
;        count
;        (< 5))))

(defn cabe-na-fila?
  [hospital departamento]
  ;some
  ;vai "threadiando" e se alguém for nulo, devolve nulo
  (some-> hospital
          departamento
          count
          (< 5)))
;
;
;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento )
;        (update hospital departamento conj pessoa)
;        (throw (IllegalStateException. "Não cabe ninguém neste departamento"))))

;precisa tratar o nil
;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento )
;    (update hospital departamento conj pessoa)))

;exemplo para extrair com ex-data
;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento )
;        (update hospital departamento conj pessoa)
;        (throw (ex-info "Não cabe ninguém neste departamento" {:paciente pessoa, :tipo :impossivel-colocar-pessoa-na-fila}))))

;
;(defn- tenta-colocar-na-fila
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento )
;    (update hospital departamento conj pessoa)))
;
;(defn chega-em
;  [hospital departamento pessoa]
;  (if-let [novo-hospital (tenta-colocar-na-fila hospital departamento pessoa)]
;    {:hospital novo-hospital, :resultado :sucesso}
;    {:hospital hospital, :resultado :impossivel-colocar-pessoa-na-fila}
;    ))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Não cabe ninguém neste departamento" {:paciente pessoa}))))

(s/defn atende :- h.model/Hospital
  [hospital :- h.model/Hospital, departamento :- s/Keyword]
  (update hospital departamento pop))

(s/defn proxima :- h.model/PacienteID
  "Retorna próximo paciente da fila"
  [hospital :- h.model/Hospital,  departamento :- s/Keyword]
  (-> hospital
      departamento
      peek))

(defn mesmo-tamanho? [hospital, outro-hospital, de, para]
  (= (+ (count (get outro-hospital de)) (count (get outro-hospital para)))
     (+ (count (get hospital de)) (count (get hospital para)))))

(s/defn transfere :- h.model/Hospital
  "Transfere o próximo paciente da fila de para a fila para"
  [hospital :- h.model/Hospital, de :- s/Keyword, para :- s/Keyword]
  ;pré condições para o transfere funcionar
  ;caso não aconteça, gera um erro de Assert
  ;pos condições devem ser true depois que o transfere for executado
  ;% é o primeiro parâmetro que é o retorno
  ;essas verificações normalmente são throwable
  ;ou seja, coisas que devem para a aplicação
  ;em clojure muitas vezes essa parte voltada a contratos não é usada
  ;é favorecido ifs, schemas, testes, etc
  {
   :pre [(contains? hospital de), (contains? hospital para)]
   :post [(mesmo-tamanho? hospital % de para)]
   }
  (let [pessoa (proxima hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))))











