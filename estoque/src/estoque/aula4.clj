(ns estoque.aula4)

(def precos [30 700 1000])

(println (precos 0))
(println (precos 2))
(println (get precos 17))                                   ;não dá erro de bound, caso passe o número de elementos
(println (get precos 17 0))                                 ;coloca valor padrão, caso não haja elemento

(println (conj precos 5))

(println (inc 5))

(println (update precos 0 inc))
(println (update precos 1 dec))

(defn soma-1
  [valor]
  (println "estou somando um em" valor)
  (+ valor 1))

(println (update precos 0 soma-1))

(defn soma-3
  [valor]
  (println "estou somando três em" valor)
  (+ valor 3))

(println (update precos 0 soma-3))


(defn aplica-desconto?
  [valor-bruto]
  (if (> valor-bruto 100)
    true
    false))

(defn valor-descontado
  "Retorna o valor com desconto de 10% se o valor bruto for estritamente maior que 100."
  [valor-bruto]
  (if (aplica-desconto? valor-bruto)
    (let [taxa-de-desconto (/ 10 100)
          desconto (* valor-bruto taxa-de-desconto)]
      (- valor-bruto desconto))
    valor-bruto))

(println (map valor-descontado precos))

(println (range 10))
(println (filter even? (range 10)))

(println (filter aplica-desconto? precos))

(defn minha-soma
  [valor-1 valor-2]
  (println "somando" valor-1 valor-2)
  (+ valor-1 valor-2))

(println (reduce minha-soma precos))















