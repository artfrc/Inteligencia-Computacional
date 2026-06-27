package ufu.ci.ga;

import ufu.ci.ga.model.Individuo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AlgoritmoGenetico {

    private static final int TAMANHO_CROMOSSOMO = 10;
    private static final int POSICOES_PREENCHIDAS = 8;
    private static final int GERACOES = 50;
    private static final char[] OPCOES_GENES = {'S', 'E', 'N', 'D', 'M', 'O', 'R', 'Y'};
    private static final int TAMANHO_POPULACAO = 100;
    private static final int TAXA_MUTACAO = 20;
    private static final int QUANTIDADE_ELITE = (int) (TAMANHO_POPULACAO * 0.2);

    private final Random aleatorio = new Random();
    private double aptidaoTotal = 0.;

    private List<Individuo> populacao;
    private static final Map<String, Integer> MAPA_INDICE_GENE = construirMapaIndiceGene();

    private static Map<String, Integer> construirMapaIndiceGene() {
        Map<String, Integer> mapa = new HashMap<>();
        for (int i = 0; i < OPCOES_GENES.length; i++) {
            mapa.put(String.valueOf(OPCOES_GENES[i]), i);
        }
        return Collections.unmodifiableMap(mapa);
    }

    public AlgoritmoGenetico() {
        this.populacao = new ArrayList<>();
    }

    public void executar() {
        long inicio = System.nanoTime();
        gerarPopulacaoInicial();
        calcularAptidao(populacao);
        for (Individuo ind : populacao) {
            System.out.println(ind);
        }
        int geracao = 0;
        while (geracao < GERACOES) {
            Individuo melhor = encontrarMelhorIndividuo();
            System.out.println("Geração " + geracao + " | Melhor aptidão: " + melhor.getAptidao());
            if (melhor.getAptidao() == 0.) {
                break;
            }

            // seleciona pais e gera filhos
            List<Individuo> novaPopulacao = new ArrayList<>();
            int contadorCruzamento = 0;
            while (novaPopulacao.size() < TAMANHO_POPULACAO - QUANTIDADE_ELITE) {
                Individuo pai1 = selecaoRoleta();
                Individuo pai2;
                do {
                    pai2 = selecaoRoleta();
                } while (pai2.equals(pai1));

                System.out.println("  [Cruzamento " + (++contadorCruzamento) + "]");
                System.out.println("    Pai 1: " + pai1);
                System.out.println("    Pai 2: " + pai2);

                List<Individuo> filhos = pmx(pai1, pai2);
                Individuo primeiroFilho = filhos.get(0);
                Individuo segundoFilho = filhos.get(1);

                if (!((aleatorio.nextInt(100) + 1) > TAXA_MUTACAO)) {
                    mutacao(primeiroFilho);
                    mutacao(segundoFilho);
                    System.out.println("    >> mutação aplicada");
                }
                calcularAptidao(filhos);

                System.out.println("    Filho 1: " + primeiroFilho);
                System.out.println("    Filho 2: " + segundoFilho);

                novaPopulacao.add(primeiroFilho);
                if (novaPopulacao.size() < TAMANHO_POPULACAO - QUANTIDADE_ELITE) {
                    novaPopulacao.add(segundoFilho);
                }
            }

            // nova população: elites + filhos gerados
            populacao.sort(Comparator.comparingDouble(Individuo::getAptidao));
            novaPopulacao.addAll(populacao.subList(0, QUANTIDADE_ELITE));
            populacao = novaPopulacao;

            geracao++;
        }

        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1_000_000.0;
        System.out.printf("Tempo de execução: %.2f ms%n", tempoMs);
    }

    private void gerarPopulacaoInicial() {
        Set<Individuo> vistos = new HashSet<>();
        while (vistos.size() < TAMANHO_POPULACAO) {
            vistos.add(gerarIndividuo());
        }
        populacao.addAll(vistos);
    }

    private void mutacao(Individuo individuo) {
        int[] cromossomo = individuo.getCromossomo();

        int pos1 = aleatorio.nextInt(TAMANHO_CROMOSSOMO);
        int pos2 = aleatorio.nextInt(TAMANHO_CROMOSSOMO);
        while (pos2 == pos1) {
            pos2 = aleatorio.nextInt(TAMANHO_CROMOSSOMO);
        }

        int temp = cromossomo[pos1];
        cromossomo[pos1] = cromossomo[pos2];
        cromossomo[pos2] = temp;

        individuo.setCromossomo(cromossomo);
    }

    private Individuo gerarIndividuo() {
        int[] cromossomo = new int[TAMANHO_CROMOSSOMO];
        Arrays.fill(cromossomo, -1);

        List<Integer> posicoes = new ArrayList<>();
        for (int i = 0; i < TAMANHO_CROMOSSOMO; i++) {
            posicoes.add(i);
        }
        Collections.shuffle(posicoes);

        List<Integer> genes = new ArrayList<>();
        for (int i = 0; i < OPCOES_GENES.length; i++) {
            genes.add(i);
        }
        Collections.shuffle(genes);

        for (int j = 0; j < POSICOES_PREENCHIDAS; j++) {
            cromossomo[posicoes.get(j)] = genes.get(j);
        }

        Individuo individuo = new Individuo(TAMANHO_CROMOSSOMO);
        individuo.setCromossomo(cromossomo);
        return individuo;
    }

    private List<Individuo> calcularAptidao(List<Individuo> listaIndividuos) {
        for (Individuo ind : listaIndividuos) {
            int[] cromossomo = ind.getCromossomo();

            // Monta mapa inverso: índice da letra -> dígito atribuído
            // cromossomo[dígito] = índice_da_letra  (ou -1 se posição vazia)
            int[] digitoPorLetra = new int[OPCOES_GENES.length];
            Arrays.fill(digitoPorLetra, -1);
            for (int digito = 0; digito < TAMANHO_CROMOSSOMO; digito++) {
                int idxLetra = cromossomo[digito];
                if (idxLetra != -1) {
                    digitoPorLetra[idxLetra] = digito;
                }
            }

            int s = digitoPorLetra[MAPA_INDICE_GENE.get("S")];
            int e = digitoPorLetra[MAPA_INDICE_GENE.get("E")];
            int n = digitoPorLetra[MAPA_INDICE_GENE.get("N")];
            int d = digitoPorLetra[MAPA_INDICE_GENE.get("D")];
            int m = digitoPorLetra[MAPA_INDICE_GENE.get("M")];
            int o = digitoPorLetra[MAPA_INDICE_GENE.get("O")];
            int r = digitoPorLetra[MAPA_INDICE_GENE.get("R")];
            int y = digitoPorLetra[MAPA_INDICE_GENE.get("Y")];

            long send  =  1000L*s + 100*e + 10*n + d;
            long more  =  1000L*m + 100*o + 10*r + e;
            long money = 10000L*m + 1000*o + 100*n + 10*e + y;

            double valorAptidao = Math.abs(send + more - money);

            ind.setAptidao(valorAptidao);
            ind.setPontuacao(1.0 / valorAptidao);
        }
        return listaIndividuos;
    }

    private Individuo selecaoRoleta() {
        double pesoTotal = 0;
        for (Individuo ind : populacao) {
            pesoTotal += ind.getPontuacao();
        }

        double r = aleatorio.nextDouble() * pesoTotal;
        double acumulado = 0;
        for (Individuo ind : populacao) {
            acumulado += ind.getPontuacao();
            if (acumulado >= r) {
                return ind;
            }
        }
        return populacao.get(populacao.size() - 1);
    }

    private Individuo encontrarMelhorIndividuo() {
        Individuo melhor = populacao.get(0);
        for (Individuo individuo : populacao) {
            if (individuo.getAptidao() < melhor.getAptidao()) {
                melhor = individuo;
            }
        }
        return melhor;
    }

    private List<Individuo> pmx(Individuo pai1, Individuo pai2) {
        int tamanho = pai1.getCromossomo().length;
        int[] p1 = pai1.getCromossomo();
        int[] p2 = pai2.getCromossomo();

        int esquerda = aleatorio.nextInt(tamanho);
        int direita = aleatorio.nextInt(tamanho);
        while (direita == esquerda) direita = aleatorio.nextInt(tamanho);
        if (esquerda > direita) { int t = esquerda; esquerda = direita; direita = t; }

        // c1 herda segmento de p2, c2 herda segmento de p1
        int[] c1 = Arrays.copyOf(p1, tamanho);
        int[] c2 = Arrays.copyOf(p2, tamanho);
        for (int i = esquerda; i <= direita; i++) {
            c1[i] = p2[i];
            c2[i] = p1[i];
        }

        Set<Integer> seg1 = genesDoSegmento(p2, esquerda, direita);
        Set<Integer> seg2 = genesDoSegmento(p1, esquerda, direita);

        for (int i = 0; i < tamanho; i++) {
            if (i >= esquerda && i <= direita) continue;
            c1[i] = resolverGene(p1[i], p1, p2, esquerda, direita, seg1);
            c2[i] = resolverGene(p2[i], p2, p1, esquerda, direita, seg2);
        }

        Individuo filho1 = new Individuo(tamanho);
        Individuo filho2 = new Individuo(tamanho);
        filho1.setCromossomo(c1);
        filho2.setCromossomo(c2);
        return List.of(filho1, filho2);
    }

    private Set<Integer> genesDoSegmento(int[] pai, int esquerda, int direita) {
        Set<Integer> genes = new HashSet<>();
        for (int i = esquerda; i <= direita; i++) {
            if (pai[i] != -1) genes.add(pai[i]);
        }
        return genes;
    }

    // Segue a cadeia de mapeamento PMX até encontrar um gene sem conflito com o segmento
    private int resolverGene(int gene, int[] de, int[] para, int esquerda, int direita, Set<Integer> genesSegmento) {
        int atual = gene;
        while (atual != -1 && genesSegmento.contains(atual)) {
            int pos = indiceNoSegmento(para, atual, esquerda, direita);
            if (pos == -1) break;
            atual = de[pos];
        }
        return atual;
    }

    private int indiceNoSegmento(int[] arr, int valor, int esquerda, int direita) {
        for (int i = esquerda; i <= direita; i++) {
            if (arr[i] == valor) return i;
        }
        return -1;
    }

    public List<Individuo> getPopulacao() {
        return populacao;
    }

    public void setPopulacao(List<Individuo> populacao) {
        this.populacao = populacao;
    }

    public double getAptidaoTotal() {
        return aptidaoTotal;
    }

    public void setAptidaoTotal(double aptidaoTotal) {
        this.aptidaoTotal = aptidaoTotal;
    }
}
