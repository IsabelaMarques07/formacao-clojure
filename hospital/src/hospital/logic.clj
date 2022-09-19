(ns hospital.logic)

(defn cabe-na-fila?
  [hospital departamento]
 ( -> hospital
  (get ,,, departamento)
  count ,,,
  (< ,,, 5)))


(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa}))))

(defn chega-em-pausado
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
   (do (Thread/sleep 1000)
    (update hospital departamento conj pessoa))
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa}))))

(defn atende
  [hospital departamento]
  (update hospital departamento pop))

(defn proxima
  "Retorna próximo paciente da fila"
  [hospital departamento]
  (-> hospital
      departamento
      peek))

(defn transfere
  "Transfere o próximo paciente da fila de para a fila para"
  [hospital de para]
  (let [pessoa (proxima hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))))

(defn atende-completo
  "Retornar quem estava no começo da fila e a fila sem a pessoa"
  [hospital departamento]
  {:paciente (update departamento hospital peek)
   :hospital     (update hospital departamento pop)})

(defn atende-completo-que-chama-ambos
  "Retornar quem estava no começo da fila e a fila sem a pessoa, juntando as duas funções"
  [hospital departamento]
  (let [fila (get hospital departamento)
        peek-pop (juxt peek pop)
        [pessoa fila-atualizada](peek-pop fila)
        hospital-atualizado (update hospital assoc departamento fila-atualizada)]
  {:paciente pessoa
   :hospital    hospital-atualizado}))

















