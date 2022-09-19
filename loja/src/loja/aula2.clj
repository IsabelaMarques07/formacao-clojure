(ns loja.aula2)

(defn conta
  [total-ate-agora elementos]
  (recur (inc total-ate-agora) (rest elementos)))

;(conta 0 ["isabela" "rogelia" "antonio" "julia" "carol"])

(defn conta
  [total-ate-agora elementos]
  (if (next elementos)
    (recur (inc total-ate-agora) (next elementos))))

;(println (conta 0 ["isabela" "rogelia" "antonio" "julia" "carol"]))

(defn conta
  [total-ate-agora elementos]
  (println total-ate-agora elementos)
  (if (next elementos)
    (recur (inc total-ate-agora) (next elementos))
   (inc total-ate-agora)))

(println (conta 0 ["isabela" "rogelia" "antonio" "julia" "carol"]))

(defn conta
  [total-ate-agora elementos]
  (println total-ate-agora elementos)
  (if (seq elementos)
    (recur (inc total-ate-agora) (next elementos))
    total-ate-agora))

(println (conta 0 ["isabela" "rogelia" "antonio" "julia" "carol"]))

(defn conta
  [total-ate-agora elementos]
  (println total-ate-agora elementos)
  (if (seq elementos)
    (recur (inc total-ate-agora) (next elementos))
    total-ate-agora))

(println (conta 0 ["isabela" "rogelia" "antonio" "julia" "carol"]))
(println (conta 0 []))

(defn conta
  ([elementos] (conta 0 elementos))
  ([total-ate-agora elementos]
  (println total-ate-agora elementos)
  (if (seq elementos)
    (recur (inc total-ate-agora) (next elementos))
    total-ate-agora)))

(println (conta ["isabela" "rogelia" "antonio" "julia" "carol"]))
(println (conta []))
(println (conta 0 ["isabela" "rogelia" "antonio" "julia" "carol"]))

(defn conta
  [elementos]
  (loop [total-ate-agora 0
         elementos-restantes elementos]
    (if (seq elementos-restantes)
      (recur (inc total-ate-agora) (next elementos-restantes))
      total-ate-agora)))

(println (conta ["isabela" "rogelia" "antonio" "julia" "carol"]))
(println (conta []))




















