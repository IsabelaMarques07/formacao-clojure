(ns hospital-v5.core
  (:use clojure.pprint)
  (:require [clojure.test.check.generators :as gen]
            [schema-generators.generators :as g]
            [hospital-v5.model :as h.model]))

;(println (gen/sample gen/boolean 3))
;(println (gen/sample gen/int 10))
;(println (gen/sample gen/string 10))
;(println (gen/sample gen/string-alphanumeric 6))
;
;(println (gen/sample (gen/vector gen/int 5), 2))

;o g/sample está deduzindo generator a partir do schema

;gera 10 elementos
;(pprint (g/sample 10 h.model/PacienteID))
;(pprint (g/sample 10 h.model/Departamento))
;(pprint (g/sample 10 h.model/Hospital))

;gerar apenas 1 elemento
(pprint (g/generate h.model/Hospital))

