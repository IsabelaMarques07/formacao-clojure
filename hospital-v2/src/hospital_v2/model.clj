(ns hospital-v2.model)

(defprotocol Dateable
  (to-ms [this]))

; para o tipo number
(extend-type java.lang.Number
  Dateable
  (to-ms [this] this))


;para o tipo Date
(extend-type java.util.Date
  Dateable
  (to-ms [this] (.getTime this)))


;para o tipo Calendar
(extend-type java.util.Calendar
  Dateable
  ;o getTime do calendar retorna um Date, assim chamamos um getTime para esse Date e ele retorna em ms
  (to-ms [this] (to-ms (.getTime this))))
