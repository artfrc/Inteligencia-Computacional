package ufu.ci.experimentos;

import ufu.ci.ga.Configuracao;
import ufu.ci.ga.Configuracao.FuncaoAptidao;
import ufu.ci.ga.Configuracao.MetodoCrossover;
import ufu.ci.ga.Configuracao.MetodoReinsercao;
import ufu.ci.ga.Configuracao.MetodoSelecao;
import ufu.ci.ga.Problema;

import java.util.ArrayList;
import java.util.List;

// 1a etapa: SEND + MORE = MONEY com as especificacoes fixas do enunciado,
// variando taxa de mutacao, selecao, crossover e reinsercao (16 combinacoes).
public class Etapa1 {

    private static final int EXECUCOES = 1000;

    public static Configuracao executar() {
        Problema problema = Problemas.SEND_MORE_MONEY;
        List<Configuracao> configuracoes = montarCombinacoes();

        System.out.println("=== 1a ETAPA: SEND + MORE = MONEY (16 combinacoes, " + EXECUCOES + " execucoes cada) ===");
        System.out.println();
        System.out.printf("%-28s | %-12s | %-16s%n", "Configuracao", "Convergencia", "Tempo medio (ms)");
        System.out.println("-".repeat(64));

        Configuracao melhorConfig = null;
        Executor.ResultadoAgregado melhorResultado = null;

        for (Configuracao config : configuracoes) {
            Executor.ResultadoAgregado r = Executor.rodar(problema, config, EXECUCOES);
            System.out.printf("%-28s | %10.1f%% | %16.3f%n", r.nome, r.percentualConvergencia(), r.tempoMedioMs);

            if (ehMelhor(r, melhorResultado)) {
                melhorResultado = r;
                melhorConfig = config;
            }
        }

        System.out.println();
        System.out.println(">> Melhor configuracao da 1a etapa: " + melhorResultado.nome
                + " (" + String.format("%.1f%%", melhorResultado.percentualConvergencia())
                + ", " + String.format("%.3f ms", melhorResultado.tempoMedioMs) + ")");
        System.out.println();
        return melhorConfig;
    }

    // Escolhe a maior convergencia; em empate, o menor tempo medio.
    private static boolean ehMelhor(Executor.ResultadoAgregado candidato, Executor.ResultadoAgregado atual) {
        if (atual == null) {
            return true;
        }
        if (candidato.convergencias != atual.convergencias) {
            return candidato.convergencias > atual.convergencias;
        }
        return candidato.tempoMedioMs < atual.tempoMedioMs;
    }

    private static List<Configuracao>  montarCombinacoes() {

        double[] taxasMutacao = {0.10, 0.20};
        MetodoSelecao[] selecoes = {MetodoSelecao.TORNEIO, MetodoSelecao.ROLETA};
        MetodoCrossover[] crossovers = {MetodoCrossover.CICLICO, MetodoCrossover.PMX};
        MetodoReinsercao[] reinsercoes = {MetodoReinsercao.ORDENADA, MetodoReinsercao.ELITISMO_PURO};

        List<Configuracao> lista = new ArrayList<>();
        for (double taxaMutacao : taxasMutacao) {
            for (MetodoSelecao selecao : selecoes) {
                for (MetodoCrossover crossover : crossovers) {
                    for (MetodoReinsercao reinsercao : reinsercoes) {
                        Configuracao config = new Configuracao();
                        config.tamanhoPopulacao = 100;
                        config.numeroGeracoes = 50;
                        config.taxaMutacao = taxaMutacao;
                        config.taxaElitismo = 0.20;
                        config.tamanhoTorneio = 3;
                        config.selecao = selecao;
                        config.crossover = crossover;
                        config.reinsercao = reinsercao;
                        config.funcaoAptidao = FuncaoAptidao.ERRO_GLOBAL;
                        // R2 (elitismo puro de 20%) usa crossover de 80%; os demais usam 60%.
                        config.taxaCrossover = reinsercao == MetodoReinsercao.ELITISMO_PURO ? 0.80 : 0.60;
                        config.nome = nomear(taxaMutacao, selecao, crossover, reinsercao);
                        lista.add(config);
                    }
                }
            }
        }
        return lista;
    }

    private static String nomear(double taxaMutacao, MetodoSelecao selecao, MetodoCrossover crossover, MetodoReinsercao reinsercao) {
        String tm = taxaMutacao == 0.10 ? "Mutacao 10% (TM1)" : "Mutacao 20% (TM2)";
        String s = selecao == MetodoSelecao.TORNEIO ? "Torneio com tour de 3 (S1)" : "Roleta (S2)";
        String c = crossover == MetodoCrossover.CICLICO ? "Cíclico (C1)" : " PMX(C2)";
        String r = reinsercao == MetodoReinsercao.ORDENADA ? "Ordenada (R1)" : "reinserção pura com 20% de elitismo (R2)";
        return tm + "-" + s + "-" + c + "-" + r;
    }
}
