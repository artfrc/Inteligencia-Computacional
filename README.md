# Inteligência Computacional — SEND + MORE = MONEY

> Solução do clássico problema criptoaritmético **SEND + MORE = MONEY** usando Algoritmo Genético, desenvolvido para a disciplina de Inteligência Computacional da UFU.

---

## O Problema

```
  S E N D
+ M O R E
---------
M O N E Y
```

Cada letra representa um dígito único de **0 a 9**. O objetivo é encontrar a atribuição correta tal que a soma seja válida. A solução correta é:

```
  9 5 6 7
+ 1 0 8 5
---------
1 0 6 5 2
```

> S=9, E=5, N=6, D=7, M=1, O=0, R=8, Y=2

---

## Arquitetura do Algoritmo Genético

### Representação do Cromossomo

Cada cromossomo é um array de 10 caracteres onde o **índice representa o dígito** e o **caractere representa a letra** mapeada para aquele dígito.

```
Posição:    0    1    2    3    4    5    6    7    8    9
           [ ]  [M]  [ ]  [ ]  [ ]  [E]  [N]  [D]  [R]  [S]
```

Nesse exemplo: S=9, E=5, N=6, D=7, M=1, R=8. Posições sem letra são preenchidas com `'-'`.

- **Tamanho do cromossomo:** 10 (dígitos 0–9)
- **Genes:** 8 letras — `S E N D M O R Y`
- **Restrição implícita:** cada letra ocupa uma posição única (sem repetição)

### Parâmetros

| Parâmetro          | Valor |
|--------------------|-------|
| Tamanho da população | 100   |
| Taxa de crossover  | 60%   |
| Taxa de mutação    | 10%   |
| Número de gerações | 10    |

---

## Função de Fitness

A função calcula o erro absoluto entre a soma e o resultado esperado:

```java
int SEND  =             S*1000 + E*100 + N*10 + D;
int MORE  =             M*1000 + O*100 + R*10 + E;
int MONEY = M*10000 + O*1000  + N*100 + E*10 + Y;

fitness = |( SEND + MORE ) - MONEY|
```

- **fitness = 0** → solução perfeita encontrada
- Quanto menor o valor, mais próximo da solução correta

---

## Teste Monte Carlo

Como ponto de partida experimental, o `Main.java` implementa um **teste Monte Carlo** para estimar a probabilidade de encontrar a solução por geração aleatória pura de populações — sem crossover nem mutação ainda.

```
Para cada experimento (10.000 experimentos):
  Para cada iteração (até 10.000 iterações):
    Gera população aleatória de 100 cromossomos
    Avalia fitness de cada cromossomo
    Se encontrar fitness == 0 → registra e vai para o próximo experimento
```

Isso serve como **baseline**: quantas iterações são necessárias no caso puramente aleatório, antes de o AG com operadores evolutivos entrar em ação.

---

## Status de Implementação

| Componente              | Status |
|-------------------------|--------|
| Representação cromossômica | ✅ Implementado |
| Geração da população inicial | ✅ Implementado |
| Função de fitness       | ✅ Implementado |
| Teste Monte Carlo       | ✅ Implementado |
| Seleção                 | 🔧 Em desenvolvimento |
| Crossover               | 🔧 Em desenvolvimento |
| Mutação                 | 🔧 Em desenvolvimento |
| Loop evolutivo completo | 🔧 Em desenvolvimento |

---

## Tecnologias

- **Java 25**
- **Maven**
- Disciplina: Inteligência Computacional — UFU
