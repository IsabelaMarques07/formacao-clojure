(ns hospital-v4.logic-test
  ;para não ter que ficar referenciando tudo toda vez que for usar
  ;clojure.test/deftest, por exemplo
  ;dessa forma ele já refere tudo
  (:require [clojure.test :refer :all]
            [hospital-v4.logic :refer :all]
            [hospital-v4.model :as h.model]
            [schema.core :as s]))

(s/set-fn-validation! true)

(deftest cabe-na-fila?-test
  (let [hospital-cheio {:espera [1 35 42 64 21]}]
    ;boundary tests
    ;exatamente na borda e one off. -1, +1. <=, >=, =.

    ;borda do zero
    (testing "Que cabe na fila"
      (is (cabe-na-fila? {:espera []} :espera)))

    ;borda do limite
    (testing "Que não cabe na fila quando a fila está cheia"
      (is (not (cabe-na-fila? {:espera [1 2 3 4 5]}, :espera))))

    ;one off da borda do limite pra cima
    (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
      (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]}, :espera))))

    ;one off da borda do limite pra baixo
    (testing "Que cabe na fila quando tem pouco menos do que uma fila cheia"
      (is (cabe-na-fila? {:espera [1 2 3 4]}, :espera)))

    ;pouco na fila
    (testing "Que cabe na fila quando tem pouco na fila"
      (is (cabe-na-fila? {:espera [1 2, 3, 4]}, :espera))
      (is (cabe-na-fila? {:espera [1 2]}, :espera)))

    (testing "Que não cabe quando o departamento não existe"
      (is (not (cabe-na-fila? {:espera [1 2 3 4]}, :raio-x))))
    )
  )

;código de teste não muito bom
;exatamente o que está escrito na função chega-em está escrito no teste
;que é o update
;(deftest chega-em-test
;  (testing "Aceita pessoas enquanto cabem pessoas na fila"
;    (is (= (update {:espera [1, 2, 3, 4]} :espera conj 5)
;           (chega-em {:espera [1, 2, 3, 4]}, :espera, 5))))
;  (testing "Aceita pessoas enquanto cabem pessoas na fila"
;    (is (= (update {:espera [1, 2]} :espera conj 5)
;           (chega-em {:espera [1, 2]}, :espera, 5))))
;  )

(deftest chega-em-test
  (let [hospital-cheio {:espera [1 35 42 64 21]}]
    ;;(testing "Aceita pessoas enquanto cabem pessoas na fila"
    (is (= {:espera [1, 2, 3, 4, 5]}
           (chega-em {:espera [1, 2, 3, 4]}, :espera, 5)))
    (is (= {:espera [1, 2, 5]}
           (chega-em {:espera [1, 2]}, :espera, 5)))
    ;(testing "não aceita quando não cabe na fila"

    ;(testing "Aceita pessoas enquanto cabem pessoas na fila"
    ;  (is (= {:hospital {:espera [1, 2, 3, 4, 5]}, :resultado :sucesso}
    ;         (chega-em {:espera [1, 2, 3, 4]}, :espera, 5)))
    ;  (is (= {:hospital {:espera [1, 2, 5]} :resultado :sucesso}
    ;         (chega-em {:espera [1, 2]}, :espera, 5))))

    (testing "não aceita quando não cabe na fila"
      ;verificando que uma exceptio foi jogada
      ;código clássico horrível, usamos uma exceptin GENÉRICA
      ;mas qq outro erro genérico vai jogar essa exception, e nós vamos achar que deu certo
      ;quando deu errado :/
      ;strings de texto SOLTO são super fáceis de quebrar
      (is (thrown? clojure.lang.ExceptionInfo
                   (chega-em hospital-cheio, :espera 76)))
      ;(is (thrown? IllegalStateException (chega-em hospital-cheio, :espera 76)))

      ;no caso do atom, quando a fila estiver cheia, retornará nil
      ;outra abordagem do nil
      ;mas o perigo do swap, teríamos que trabalhar em outro ponto a condição de erro
      ;(is (nil? (chega-em hospital-cheio, :espera 76)))

      ;outra maneira de testar
      ;onde ao invés de como Java, utilziar o TIPO da exception para entender
      ;o TIPO(outro tipo) de erro que ocorreu, estou usando os dados da exception para isso
      ;menos sensível que a mensagem de erro (mesmo que usasse regex)
      ;mas ainda é uma validação trabalhosa
      ;(is (try
      ;        (chega-em hospital-cheio, :espera, 76)
      ;        false
      ;        (catch clojure.lang.ExceptionInfo e
      ;          (= :impossivel-colocar-pessoa-na-fila (:tipo (ex-data e)))
      ;          )))

      ;(is (= {:hospital hospital-cheio, :resultado :impossivel-colocar-pessoa-na-fila}
      ;       (chega-em hospital-cheio, :espera 76)))))
      )))

(deftest transfere-test
  (testing "aceita pessoas se cabe"
    (let [hospital-original {:espera (conj h.model/fila-vazia "5"), :raio-x h.model/fila-vazia}]
      (is (= {:espera []
              :raio-x ["5"]}
             (transfere hospital-original :espera :raio-x)
             )))
    (let [hospital-original {:espera (conj h.model/fila-vazia "51" "5"), :raio-x (conj h.model/fila-vazia "13")}]
      (is (= {:espera ["5"]
              :raio-x ["13" "51"]}
             (transfere hospital-original :espera :raio-x)
             )))
    )
  (testing "recusa pessoas se não cabe"
    (let [hospital-cheio {:espera (conj h.model/fila-vazia "5"), :raio-x (conj h.model/fila-vazia "1" "2" "53" "42" "13")}]
      (is (thrown? clojure.lang.ExceptionInfo
                   (transfere hospital-cheio :espera :raio-x)
                   )))
    )

  ; para garantir que a validação de Schema está presente
  ; será que faz sentido eu garantir que o Schema está do outro lado
  ; lembrando que este teste não garante exatamente isso, apenas o erro do nulo
  ;
  (testing "Não pode invocar transferência sem hospital"
    (is (thrown? clojure.lang.ExceptionInfo (transfere nil :espera :raio-x))))

  (testing "condições obrigatórias"
    (let [hospital {:espera (conj h.model/fila-vazia "5"), :raio-x (conj h.model/fila-vazia "1" "2" "53" "42")}]
      (is (thrown? AssertionError (transfere hospital :nao-existe :raio-x)))
      (is (thrown? AssertionError (transfere hospital :raio-x :nao-existe)))))


  )
