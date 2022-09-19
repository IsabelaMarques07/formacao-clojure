(ns loja.aula5
  (:require [loja.db :as l.db]
            [loja.logic :as l.logic]))

(defn gastou-bastante?
  [info-do-usuario]
  (> (:preco-total info-do-usuario) 500))

(let [pedidos (l.db/todos-os-pedidos)
      resumo (l.logic/resumo-por-usuario pedidos)]
  (println "keep" (keep gastou-bastante? resumo))
  (println "filter" (filter gastou-bastante? resumo)))


(println "TENTANDO ENTENDER")

(defn gastou-bastante?
  [info-do-usuario]
  (println "gastou-bastante? " (:usuario-id info-do-usuario))
  (> (:preco-total info-do-usuario) 500))

(let [pedidos (l.db/todos-os-pedidos)
      resumo (l.logic/resumo-por-usuario pedidos)]
  (println "keep" (keep gastou-bastante? resumo)))

(println "VAMOS ISOLAR .....")

(println (range 100))
 ;a sequencia não é gulosa "eager"
 ;a sequencia é gerada de acordo com o necessário
 ;o take só precisa dos dois primeiros
 ;portanto, só são gerados os dois primeiros
(println (take 2 (range 100000000)))

(let [sequencia (range 1000000000)]
  (println (take 2 sequencia))
  (println (take 2 sequencia))
  )


(defn filtro1 [x]
  (println "filtro1" x)
  x)

(println (map filtro1 (range 10)))


(defn filtro2 [x]
  (println "filtro2" x)
  x)

(println (map filtro2(map filtro1 (range 10))))

(->> (range 10)
     (map filtro1)
     (map filtro2)
     println)


; CHUNKS pedaços de 32 em 32
; Não roda todos os elementos, trabalha por blocos
; consegue ser preguiçoso, mas é eager também
(->> (range 50)
     (map filtro1)
     (map filtro2)
     println)

(->> (range 50)
     (mapv filtro1)
     (mapv filtro2)
     println)



















