(ns loja.aula1)
(map println ["isabela" "rogelia" "antonio" "julia" "carol"])
;(println (first ["isabela" "rogelia" "antonio" "julia" "carol"]))
;
;(println (rest ["isabela" "rogelia" "antonio" "julia" "carol"]))
;(println (rest []))
;(println (next ["isabela" "rogelia" "antonio" "julia" "carol"]))
;(println (next []))
;
;;(defn meu-mapa
;;  [funcao sequencia]
;;  (let [primeiro (first sequencia)]
;;    (funcao primeiro)
;;    (meu-mapa funcao (rest sequencia))))
;
;
;(defn meu-mapa
;  [funcao sequencia]
;  (let [primeiro (first sequencia)]
;    (if primeiro
;      (do (funcao primeiro)
;                 (meu-mapa funcao (rest sequencia))))))
;
;(meu-mapa println ["isabela" "rogelia" "antonio" "julia" "carol"])
;
;(meu-mapa println ["isabela" false "antonio" "julia" "carol"])

(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if (not (nil? primeiro))
      (do (funcao primeiro)
          (meu-mapa funcao (rest sequencia))))))


(meu-mapa println ["isabela" "rogelia" "antonio" "julia" "carol"])

(meu-mapa println ["isabela" false "antonio" "julia" "carol"])

(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if (not (nil? primeiro))
      (do (funcao primeiro)
          (recur funcao (rest sequencia))))))

(meu-mapa println (range 3))


