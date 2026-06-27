package ufu.ci.ga;

import ufu.ci.ga.model.Individuo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// Algoritmo Genetico para problemas de criptoaritmetica. O comportamento e
// definido por uma Configuracao, entao uma mesma classe roda todas as variacoes.
public class AlgoritmoGenetico {

    public static class Resultado {
        public final boolean convergiu;
        public final int geracoes;
        public final Individuo melhor;

        public Resultado(boolean convergiu, int geracoes, Individuo melhor) {
            this.convergiu = convergiu;
            this.geracoes = geracoes;
            this.melhor = melhor;
        }
    }

    private final Problema problema;
    private final Configuracao config;
    private final Random aleatorio;

    public AlgoritmoGenetico(Problema problema, Configuracao config) {
        this(problema, config, new Random());
    }

    public AlgoritmoGenetico(Problema problema, Configuracao config, Random aleatorio) {
        this.problema = problema;
        this.config = config;
        this.aleatorio = aleatorio;
    }

    public Resultado executar() {
        List<Individuo> populacao = gerarPopulacaoInicial();
        avaliar(populacao);

        Individuo melhor = melhorDe(populacao);
        int geracao = 0;

        while (geracao < config.numeroGeracoes && !ehSolucao(melhor)) {
            populacao = proximaGeracao(populacao);
            avaliar(populacao);

            Individuo melhorDaGeracao = melhorDe(populacao);
            if (melhorDaGeracao.getAptidao() < melhor.getAptidao()) {
                melhor = melhorDaGeracao;
            }
            geracao++;
        }

        return new Resultado(ehSolucao(melhor), geracao, melhor);
    }

    private List<Individuo> gerarPopulacaoInicial() {
        Set<Individuo> distintos = new HashSet<>();
        while (distintos.size() < config.tamanhoPopulacao) {
            distintos.add(new Individuo(permutacaoAleatoria()));
        }
        return new ArrayList<>(distintos);
    }

    // Embaralha um vetor 0..9 (Fisher-Yates).
    private int[] permutacaoAleatoria() {
        int[] vetor = new int[Problema.TOTAL_DIGITOS];
        for (int i = 0; i < vetor.length; i++) {
            vetor[i] = i;
        }
        for (int i = vetor.length - 1; i > 0; i--) {
            int j = aleatorio.nextInt(i + 1);
            int tmp = vetor[i];
            vetor[i] = vetor[j];
            vetor[j] = tmp;
        }
        return vetor;
    }

    private List<Individuo> proximaGeracao(List<Individuo> populacao) {
        boolean comElitismo = config.reinsercao == Configuracao.MetodoReinsercao.ELITISMO_PURO;
        int tamanho = config.tamanhoPopulacao;
        int elite = comElitismo ? (int) Math.round(tamanho * config.taxaElitismo) : 0;

        int filhosNecessarios = comElitismo ? (tamanho - elite) : tamanho;
        List<Individuo> filhos = gerarFilhos(populacao, filhosNecessarios);

        if (comElitismo) {
            // reinsercao pura com elitismo: elite dos pais + filhos
            List<Individuo> ordenados = new ArrayList<>(populacao);
            ordenados.sort(Comparator.comparingDouble(Individuo::getAptidao));
            List<Individuo> nova = new ArrayList<>(tamanho);
            nova.addAll(ordenados.subList(0, elite));
            nova.addAll(filhos);
            return nova;
        }

        // reinsercao ordenada: melhores entre pais e filhos
        List<Individuo> combinados = new ArrayList<>(populacao.size() + filhos.size());
        combinados.addAll(populacao);
        combinados.addAll(filhos);
        combinados.sort(Comparator.comparingDouble(Individuo::getAptidao));
        return new ArrayList<>(combinados.subList(0, tamanho));
    }

    private List<Individuo> gerarFilhos(List<Individuo> populacao, int quantidade) {
        List<Individuo> filhos = new ArrayList<>(quantidade);
        while (filhos.size() < quantidade) {
            Individuo pai1 = selecionar(populacao);
            Individuo pai2 = selecionar(populacao);

            int[] filho1;
            int[] filho2;
            if (aleatorio.nextDouble() < config.taxaCrossover) {
                int[][] resultado = cruzar(pai1.getCromossomo(), pai2.getCromossomo());
                filho1 = resultado[0];
                filho2 = resultado[1];
            } else {
                filho1 = pai1.getCromossomo().clone();
                filho2 = pai2.getCromossomo().clone();
            }

            aplicarMutacao(filho1);
            aplicarMutacao(filho2);

            filhos.add(new Individuo(filho1));
            if (filhos.size() < quantidade) {
                filhos.add(new Individuo(filho2));
            }
        }
        return filhos;
    }

    private void avaliar(List<Individuo> populacao) {
        for (Individuo individuo : populacao) {
            int[] digitoPorLetra = problema.decodificar(individuo.getCromossomo());
            long erro = config.funcaoAptidao == Configuracao.FuncaoAptidao.ERRO_POSICIONAL
                    ? problema.erroPosicional(digitoPorLetra)
                    : problema.erroGlobal(digitoPorLetra);
            individuo.setAptidao(erro);
        }
    }

    private boolean ehSolucao(Individuo individuo) {
        return problema.ehSolucaoValida(problema.decodificar(individuo.getCromossomo()));
    }

    private Individuo melhorDe(List<Individuo> populacao) {
        Individuo melhor = populacao.get(0);
        for (Individuo individuo : populacao) {
            if (individuo.getAptidao() < melhor.getAptidao()) {
                melhor = individuo;
            }
        }
        return melhor;
    }

    private Individuo selecionar(List<Individuo> populacao) {
        if (config.selecao == Configuracao.MetodoSelecao.ROLETA) {
            return selecaoRoleta(populacao);
        }
        return selecaoTorneio(populacao);
    }

    private Individuo selecaoTorneio(List<Individuo> populacao) {
        Individuo vencedor = populacao.get(aleatorio.nextInt(populacao.size()));
        for (int i = 1; i < config.tamanhoTorneio; i++) {
            Individuo candidato = populacao.get(aleatorio.nextInt(populacao.size()));
            if (candidato.getAptidao() < vencedor.getAptidao()) {
                vencedor = candidato;
            }
        }
        return vencedor;
    }

    private Individuo selecaoRoleta(List<Individuo> populacao) {
        // Como o problema e de minimizacao, o erro vira um peso 1/(1+erro),
        // que e maior quanto menor for a aptidao.
        double pesoTotal = 0;
        for (Individuo individuo : populacao) {
            pesoTotal += 1.0 / (1.0 + individuo.getAptidao());
        }

        double sorteio = aleatorio.nextDouble() * pesoTotal;
        double acumulado = 0;
        for (Individuo individuo : populacao) {
            acumulado += 1.0 / (1.0 + individuo.getAptidao());
            if (acumulado >= sorteio) {
                return individuo;
            }
        }
        return populacao.get(populacao.size() - 1);
    }

    private int[][] cruzar(int[] pai1, int[] pai2) {
        if (config.crossover == Configuracao.MetodoCrossover.CICLICO) {
            return crossoverCiclico(pai1, pai2);
        }
        return crossoverPmx(pai1, pai2);
    }

    // Crossover ciclico: percorre os ciclos formados pelas posicoes dos pais e,
    // a cada ciclo, alterna de qual pai os filhos herdam os genes.
    private int[][] crossoverCiclico(int[] pai1, int[] pai2) {
        int n = pai1.length;
        int[] filho1 = new int[n];
        int[] filho2 = new int[n];
        boolean[] visitado = new boolean[n];

        boolean usarPai1 = true;
        for (int inicio = 0; inicio < n; inicio++) {
            if (visitado[inicio]) {
                continue;
            }
            int posicao = inicio;
            do {
                visitado[posicao] = true;
                if (usarPai1) {
                    filho1[posicao] = pai1[posicao];
                    filho2[posicao] = pai2[posicao];
                } else {
                    filho1[posicao] = pai2[posicao];
                    filho2[posicao] = pai1[posicao];
                }
                posicao = posicaoDoValor(pai1, pai2[posicao]);
            } while (posicao != inicio);
            usarPai1 = !usarPai1;
        }
        return new int[][]{filho1, filho2};
    }

    // PMX: cada filho herda um segmento de um pai e completa o resto com o outro,
    // resolvendo as repeticoes pelo mapeamento do segmento.
    private int[][] crossoverPmx(int[] pai1, int[] pai2) {
        int n = pai1.length;
        int corte1 = aleatorio.nextInt(n);
        int corte2 = aleatorio.nextInt(n);
        if (corte1 > corte2) {
            int tmp = corte1;
            corte1 = corte2;
            corte2 = tmp;
        }
        int[] filho1 = construirFilhoPmx(pai1, pai2, corte1, corte2);
        int[] filho2 = construirFilhoPmx(pai2, pai1, corte1, corte2);
        return new int[][]{filho1, filho2};
    }

    private int[] construirFilhoPmx(int[] doadorSegmento, int[] outro, int corte1, int corte2) {
        int[] filho = outro.clone();
        for (int i = corte1; i <= corte2; i++) {
            filho[i] = doadorSegmento[i];
        }
        for (int i = corte1; i <= corte2; i++) {
            int valorDeslocado = outro[i];
            if (!contemNoSegmento(doadorSegmento, corte1, corte2, valorDeslocado)) {
                int posicao = i;
                do {
                    int mapeado = doadorSegmento[posicao];
                    posicao = posicaoDoValor(outro, mapeado);
                } while (posicao >= corte1 && posicao <= corte2);
                filho[posicao] = valorDeslocado;
            }
        }
        return filho;
    }

    private boolean contemNoSegmento(int[] vetor, int corte1, int corte2, int valor) {
        for (int i = corte1; i <= corte2; i++) {
            if (vetor[i] == valor) {
                return true;
            }
        }
        return false;
    }

    private int posicaoDoValor(int[] vetor, int valor) {
        for (int i = 0; i < vetor.length; i++) {
            if (vetor[i] == valor) {
                return i;
            }
        }
        return -1;
    }

    // Mutacao por troca de duas posicoes, aplicada conforme a taxa.
    private void aplicarMutacao(int[] cromossomo) {
        if (aleatorio.nextDouble() >= config.taxaMutacao) {
            return;
        }
        int pos1 = aleatorio.nextInt(cromossomo.length);
        int pos2 = aleatorio.nextInt(cromossomo.length);
        while (pos2 == pos1) {
            pos2 = aleatorio.nextInt(cromossomo.length);
        }
        int tmp = cromossomo[pos1];
        cromossomo[pos1] = cromossomo[pos2];
        cromossomo[pos2] = tmp;
    }
}
