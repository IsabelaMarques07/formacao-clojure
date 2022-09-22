(ns hospital-v5.logic-test
      (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [hospital-v5.logic :refer :all]
            [hospital-v5.model :as h.model]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [schema.core :as s]
            [schema-generators.generators :as g]))

(s/set-fn-validation! true)

(deftest cabe-na-fila?-test
  (let [hospital-cheio {:espera [1 35 42 64 21]}]

    ;testes ESCRITOS baseados em exemplos
    (testing "Que cabe na fila vazia"
      (is (cabe-na-fila? {:espera []} :espera)))

    ;o doseq com um símbolo e uma sequencia gerada funciona
    ;mas talvez não seja o que queremos m examplo-based manual
    (testing "Que cabe pessoas em filas de tamanho até 4 inclusive"
             ;doseq faz para cada valor da sequencia
             ;pega um valor do vetor vazio, atribui a fila e executa o código seguinte
             (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4))]
                (is (cabe-na-fila? {:espera fila}, :espera))))


    (testing "Que não cabe na fila quando a fila está cheia"
      (is (not (cabe-na-fila? {:espera [1 2 3 4 5]}, :espera))))

    (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
      (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]}, :espera))))

    (testing "Que cabe na fila quando tem pouco menos do que uma fila cheia"
      (is (cabe-na-fila? {:espera [1 2 3 4]}, :espera)))

    (testing "Que cabe na fila quando tem pouco na fila"
      (is (cabe-na-fila? {:espera [1 2, 3, 4]}, :espera))
      (is (cabe-na-fila? {:espera [1 2]}, :espera)))

    (testing "Que não cabe quando o departamento não existe"
      (is (not (cabe-na-fila? {:espera [1 2 3 4]}, :raio-x))))
    )

  ;aqui tivemos um problema
  ;doseq gira uma multiplicação de casos
  ;incluindo muitos casos repetidos
  ;não é o que queremos
  ;(deftest chega-em-test
  ;  (testing "Que é colocada uma pessoa em filas menores que 5"
  ;    ;cruza os dados
  ;    ;10 filas e 10 pessoas
  ;    ;     pega as 10 filas e cruzar com 10 pessoas, ou seja, 100 casos
  ;    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4)10)
  ;            pessoa (gen/sample gen/string-alphanumeric)]
  ;      (println pessoa fila)
  ;    ))))
  ;  ;(is (cabe-na-fila? {:espera fila}, :espera))

  ;o teste a seguir é generativo e funciona
  ;mas o resultado dele parece muito uma cópia do código implementado
  ;se tem um big lá, provavelmente o mesmo bug está aqui
  ;e retornará true
  ;"fez o teste, mas não testou"
  ;(defspec coloca-uma-pessoa-em-filas-menores-que-5 10
  ;  ;gerar um vetor de string de 0 a 4 caracteres
  ;  ;não precisa do sample, pq o prop/for-all já trabalha nessa parte
  ;  (prop/for-all [fila (gen/vector gen/string-alphanumeric 0 4)
  ;                 pessoa  gen/string-alphanumeric]
  ;    ;(println pessoa fila)
  ;    (= {:espera (conj pessoa fila)}
  ;      (chega-em {:espera fila} :espera pessoa))
  ;    ))

  (def nome-aleatorio-gen
    ;aplica a função de join para cada dado criado pelo gerador
    (gen/fmap clojure.string/join
              (gen/vector gen/char-alphanumeric 5 10)))

  (defn transforma-vetor-em-fila [vetor]
    (reduce conj h.model/fila-vazia vetor )
    )

  (def fila-nao-cheia-gen
    (gen/fmap
      transforma-vetor-em-fila
      (gen/vector nome-aleatorio-gen 1 4)))



  ;uma abordagem razoável, uma vez que usmos o tipo e o tipo do tipo
  ;para fazer um cond e pegar a exception que queremos
  ;uma alternativa seria usar bibliotecas como a catch-data
  ;LOG AND RETRHOW é ruim
  ;pq se você pegou, é porque você queria tratar
  ;pq vc pegou se vc sabia que não ia tratar?
;(defn transfere-ignorando-erro [hospital para]
;  (try
;      (transfere hospital :espera para)
;    (catch clojure.lang.ExceptionInfo e
;      (cond
;        (= :fila-cheia (:type (ex-data e))) hospital
;        :else (throw e)
;        )
;      ;hospital
;      )))

  ;abordagem interessante pois evista log and rethrow
  ;mmas perde o "poder" de ex-info (ExceptionInfo)
  ;e ainda tem o problema de que outras partes do meu código ou
  ;do código de outras pessoas pode jogar IllegaStateException
  ;e eu estou confundindo isso com fila-cheia
  ;para resolver isso, só criando minha própria exception
  ;mas ai caio no boom de exceptions no sistema (tenho que criar vários tipos)
  ;OU criar variações de tipos como fizemos no ex-info
  (defn transfere-ignorando-erro [hospital para]
    (try
      (transfere hospital :espera para)
      (catch IllegalStateException e
        hospital
        )))


  (defspec transfere-tem-que-manter-a-quantidade-de-pessoas 5
    (prop/for-all
      [espera (gen/fmap transforma-vetor-em-fila (gen/vector nome-aleatorio-gen 10 50))
       raio-x fila-nao-cheia-gen
       ultrassom fila-nao-cheia-gen
       vai-para (gen/vector (gen/elements [:raio-x :ultrassom]) 0 50)]
      ;(println (count espera) (count vai-para) vai-para)
      (let [hospital-inicial {:espera espera, :raio-x raio-x, :ultrassom ultrassom}
            hospital-final (reduce transfere-ignorando-erro hospital-inicial vai-para)]
        (=
              (total-de-pacientes hospital-inicial)
              (total-de-pacientes hospital-final))
        )
      ))

  (defn adiciona-fila-de-espera [[hospital fila]]
    (assoc hospital :espera fila))

  (def hospital-gen
    (gen/fmap
      ;sempre adiciona uma fila de espera
      adiciona-fila-de-espera
      ;gera uma tupla, mais de um elemento. Um hospital e uma fila
      (gen/tuple
        ;qualquer hospital tem que ter pelo menos uma fila
        (gen/not-empty (g/generator h.model/Hospital))
        fila-nao-cheia-gen))
    )

  ;o gen/tuple recebe apenas geradores
  ;o gen/return é um gerador que retorna sempre o valor indidicado
  (def chega-em-gen
    "Gerador de chegadas no hospital"
    (gen/tuple (gen/return chega-em), (gen/return :espera), nome-aleatorio-gen, (gen/return 1)))

  (defn adiciona-inexistente-ao-departamento [departamento]
    (keyword (str departamento "-inexistente")))

  (defn transfere-gen [hospital]
    "Gerador de transferências no hospital"
    (let [departamentos (keys hospital)
          departamentos-inexistentes (map adiciona-inexistente-ao-departamento departamentos)
          todos-os-departamentos (concat departamentos departamentos-inexistentes)]
      (gen/tuple (gen/return transfere),
                 (gen/elements todos-os-departamentos),
                 (gen/elements todos-os-departamentos),
                 (gen/return 0))))

  ;geradores de ações
  (defn acao-gen [hospital]
      (gen/one-of [chega-em-gen (transfere-gen hospital)]))
  (defn acoes-gen [hospital]
    (gen/not-empty (gen/vector (acao-gen hospital) 1 100)))

  (defn executa-uma-acao [situacao [funcao param1 param2 diferenca-se-sucesso]]
    (let [hospital (:hospital situacao)
          diferenca-atual (:diferenca situacao)]
      (try
        (let [hospital-novo (funcao hospital param1 param2)]
          {:hospital hospital-novo, :diferenca (+ diferenca-se-sucesso diferenca-atual)})
        (catch IllegalStateException e
          situacao)
        (catch AssertionError e
          situacao)
        ))
    )


  (defspec simula-um-dia-do-hospital-acumula-pessoas 50
    (prop/for-all
      [hospital-inicial hospital-gen]
      ;gerar acoes apenas depois de ter gerado o hospital
      (let [acoes (gen/generate (acoes-gen hospital-inicial))
            situacao-inicial {:hospital hospital-inicial, :diferenca 0}
            total-de-pacientes-inicial (total-de-pacientes hospital-inicial)
            situacao-final (reduce executa-uma-acao situacao-inicial acoes)
            total-de-pacientes-final (total-de-pacientes (:hospital situacao-final))]
                  ;(pprint acoes)
                  (println total-de-pacientes-final total-de-pacientes-inicial (:diferenca situacao-final))
                  ;(println total-de-pacientes-inicial total-de-pacientes-final)
            (is (= (- total-de-pacientes-final (:diferenca situacao-final)) total-de-pacientes-inicial)))))

  ;;gera 10 testes
  ;(defspec explorando-a-api 10
  ;         ;gerar um vetor de string de 0 a 4 caracteres
  ;         ;não precisa do sample, pq o prop/for-all já trabalha nessa parte
  ;         (prop/for-all [fila (gen/vector gen/string-alphanumeric 0 4)
  ;                        pessoa  gen/string-alphanumeric]
  ;                       (println pessoa fila)
  ;                       true
  ;                       )))
  )