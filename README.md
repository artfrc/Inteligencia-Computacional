# Inteligência Computacional - Algoritmo Genético para Criptoaritmética

Trabalho da disciplina GBC073 (Inteligência Computacional) da UFU. O objetivo é
resolver problemas de criptoaritmética com um Algoritmo Genético, como o clássico
SEND + MORE = MONEY:

```
  S E N D
+ M O R E
---------
M O N E Y
```

Cada letra vale um dígito de 0 a 9, a mesma letra sempre vale o mesmo dígito e
letras diferentes valem dígitos diferentes. A solução do exemplo é S=9, E=5, N=6,
D=7, M=1, O=0, R=8, Y=2.

## Estrutura

O código está em `evolutionary_computation`. Há uma única implementação do AG,
parametrizável, e cada variação avaliada no trabalho é uma combinação de
operadores e parâmetros.

```
src/main/java/ufu/ci
├── Main.java                      roda as três etapas em sequência
├── ga
│   ├── AlgoritmoGenetico.java     o AG (seleção, crossover, mutação, reinserção)
│   ├── Configuracao.java          parâmetros e escolha de operadores
│   ├── Problema.java              parsing e funções de avaliação
│   └── model/Individuo.java       o cromossomo (permutação de 0..9)
└── experimentos
    ├── Problemas.java             os cinco problemas
    ├── Executor.java              roda N vezes e resume convergência e tempo
    ├── Etapa1.java                16 combinações de operadores
    ├── Etapa2.java                refinamento de parâmetros
    └── Etapa3.java                desempenho geral nos 5 problemas
```

## Como o indivíduo é representado

O cromossomo é um vetor de inteiros de tamanho 10. A posição é o dígito (0 a 9) e
o valor é o índice da letra associada àquele dígito. Como são dez posições e dez
valores, o vetor é sempre uma permutação de 0 a 9. Quando o problema tem menos de
dez letras, os índices que sobram não aparecem em nenhuma palavra (letras
"fantasma"). Isso deixa os operadores de permutação (cíclico, PMX e troca)
funcionando sem casos especiais e cobre qualquer problema com até dez letras.

## Operadores

- Seleção: torneio (tour configurável) e roleta
- Crossover: cíclico e PMX
- Mutação: troca de duas posições do vetor
- Reinserção: ordenada (melhores entre pais e filhos) e pura com elitismo
- Aptidão: erro global `|(SEND+MORE)-MONEY|` e erro posicional (coluna a coluna)

## Como executar

Java 21 e Maven.

```
cd evolutionary_computation
javac -d target/classes $(find src/main/java -name '*.java')
java -cp target/classes ufu.ci.Main
```

A saída traz as tabelas de convergência e tempo médio de cada etapa.

## Relatório

A análise completa das três etapas, com as tabelas de resultados e a justificativa
das escolhas, está em `evolutionary_computation/RELATORIO.md`.
