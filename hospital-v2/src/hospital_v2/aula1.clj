(ns hospital-v2.aula1
  (:use clojure.pprint))


(defn adiciona-paciente
  "Os pacientes são um mapa da seguinte forma {15 {paciente 15}, 23 {paciente 23}
  O paciente {:id 15...}"
  [pacientes paciente]
  (let [id (:id paciente)]
    (if id
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente não possui id" {:paciente paciente})))))

(defn adiciona-paciente
  "Os pacientes são um mapa da seguinte forma {15 {paciente 15}, 23 {paciente 23}
  O paciente {:id 15...}"
  [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente não possui id" {:paciente paciente}))))

(defn testa-uso-de-pacientes []
  (let [pacientes {}
        guilherme {:id 15 :nome "Guilherme" :nascimento "18/9/1981"}
        daniela {:id 20 :nome "Daniela" :nascimento "18/9/1982"}
        paulo { :nome "Paulo" :nascimento "18/10/1983"}]
    (pprint (adiciona-paciente pacientes guilherme))
    (pprint (adiciona-paciente pacientes daniela))
    (pprint (adiciona-paciente pacientes paulo))))

;(testa-uso-de-pacientes)

(defrecord Paciente [^Long id nome nascimento])

(println (->Paciente 15 "Guilherme " "18/9/1981"))
(pprint (->Paciente 15 "Guilherme " "18/9/1981"))

(pprint (Paciente. 15 "Guilherme" "18/09/1981"))

(pprint (map->Paciente {:id 15, :nome "Guilherme", :nascimento "18/9/1981"}))

(let [guilherme  (->Paciente 15 "Guilherme " "18/9/1981")]
  (println (:id guilherme))
  (println (vals guilherme))
  (pprint (record? guilherme))
  (pprint (.nome guilherme)))

(pprint (map->Paciente {:id 15, :nome "Guilherme", :nascimento "18/9/1981" :rg "222222"}))
(pprint (map->Paciente { :nome "Guilherme", :nascimento "18/9/1981" :rg "222222"}))

(pprint (assoc (Paciente. 15 "Guilherme" "18/09/1981") :id 38))
(pprint (class (assoc (Paciente. 15 "Guilherme" "18/09/1981") :id 38)))

(pprint (=(Paciente. 15 "Guilherme" "18/09/1981") (Paciente. 15 "Guilherme" "18/09/1981")))

;(pprint (record? guilherme))










