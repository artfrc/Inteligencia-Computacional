package ufu.ci.experimentos;

import ufu.ci.ga.Configuracao;
import ufu.ci.ga.Configuracao.FuncaoAptidao;
import ufu.ci.ga.Problema;

import java.util.ArrayList;
import java.util.List;

// 3a etapa: avalia o AG de forma geral, pela media de desempenho nos cinco
// problemas. A partir da melhor versao da 2a etapa, testa variacoes em busca da
// versao mais generica, incluindo a aptidao por erro posicional comparada com o
// erro global. As variacoes respeitam o limite de 50% de acrescimo no tempo.
public class Etapa3 {

    private static final int EXECUCOES = 1000;

    // Desempenho medio de uma configuracao nos cinco problemas.
    private static class Media {
        final double convergencia;
        final double tempo;

        Media(double convergencia, double tempo) {
            this.convergencia = convergencia;
            this.tempo = tempo;
        }
    }

    public static void executar(Configuracao melhorEtapa2) {
        System.out.println("=== 3a ETAPA: desempenho medio nos 5 problemas ("
                + EXECUCOES + " execucoes por problema) ===");
        System.out.println();

        Configuracao base = melhorEtapa2.copia();
        base.nome = "BASE (melhor etapa 2, erro global)";

        List<Configuracao> variacoes = montarVariacoes(base);

        Media mediaBase = medirNosCincoProblemas(base);
        double tempoLimite = mediaBase.tempo * 1.5;

        System.out.printf("%-44s | %-16s | %-16s | %-8s%n",
                "Variacao", "Conv. media (%)", "Tempo medio (ms)", "Dentro 50%");
        System.out.println("-".repeat(92));
        imprimir(base.nome, mediaBase, tempoLimite);

        Configuracao melhorConfig = base;
        Media melhorMedia = mediaBase;

        for (Configuracao config : variacoes) {
            Media media = medirNosCincoProblemas(config);
            imprimir(config.nome, media, tempoLimite);

            boolean dentroDoLimite = media.tempo <= tempoLimite;
            if (dentroDoLimite && ehMelhor(media, melhorMedia)) {
                melhorMedia = media;
                melhorConfig = config;
            }
        }

        System.out.println();
        System.out.println(">> Melhor variacao generica: " + melhorConfig.nome
                + " (" + String.format("%.1f%%", melhorMedia.convergencia)
                + ", " + String.format("%.3f ms", melhorMedia.tempo) + ")");
        System.out.println(">> Base (erro global): "
                + String.format("%.1f%%", mediaBase.convergencia)
                + ", " + String.format("%.3f ms", mediaBase.tempo));
        System.out.println();
        imprimirDetalhePorProblema(melhorConfig);
    }

    private static List<Configuracao> montarVariacoes(Configuracao base) {
        List<Configuracao> lista = new ArrayList<>();

        Configuracao vg1 = base.copia();
        vg1.nome = "VG1 aptidao posicional";
        vg1.funcaoAptidao = FuncaoAptidao.ERRO_POSICIONAL;
        lista.add(vg1);

        Configuracao vg2 = base.copia();
        vg2.nome = "VG2 posicional + mutacao 40%";
        vg2.funcaoAptidao = FuncaoAptidao.ERRO_POSICIONAL;
        vg2.taxaMutacao = 0.40;
        lista.add(vg2);

        Configuracao vg3 = base.copia();
        vg3.nome = "VG3 posicional + tour 4";
        vg3.funcaoAptidao = FuncaoAptidao.ERRO_POSICIONAL;
        vg3.tamanhoTorneio = 4;
        lista.add(vg3);

        Configuracao vg4 = base.copia();
        vg4.nome = "VG4 posicional + crossover 90%";
        vg4.funcaoAptidao = FuncaoAptidao.ERRO_POSICIONAL;
        vg4.taxaCrossover = 0.90;
        lista.add(vg4);

        Configuracao vg5 = base.copia();
        vg5.nome = "VG5 posicional + mutacao 40% + tour 4";
        vg5.funcaoAptidao = FuncaoAptidao.ERRO_POSICIONAL;
        vg5.taxaMutacao = 0.40;
        vg5.tamanhoTorneio = 4;
        lista.add(vg5);

        return lista;
    }

    private static Media medirNosCincoProblemas(Configuracao config) {
        double somaConvergencia = 0;
        double somaTempo = 0;
        for (Problema problema : Problemas.TODOS) {
            Executor.ResultadoAgregado r = Executor.rodar(problema, config, EXECUCOES);
            somaConvergencia += r.percentualConvergencia();
            somaTempo += r.tempoMedioMs;
        }
        int n = Problemas.TODOS.length;
        return new Media(somaConvergencia / n, somaTempo / n);
    }

    private static boolean ehMelhor(Media candidato, Media atual) {
        if (candidato.convergencia != atual.convergencia) {
            return candidato.convergencia > atual.convergencia;
        }
        return candidato.tempo < atual.tempo;
    }

    private static void imprimir(String nome, Media media, double tempoLimite) {
        String dentro = media.tempo <= tempoLimite ? "sim" : "NAO";
        System.out.printf("%-44s | %14.1f%% | %16.3f | %-8s%n",
                nome, media.convergencia, media.tempo, dentro);
    }

    private static void imprimirDetalhePorProblema(Configuracao config) {
        System.out.println("Detalhe da melhor variacao (" + config.nome + ") por problema:");
        System.out.printf("%-26s | %-12s | %-16s%n", "Problema", "Convergencia", "Tempo medio (ms)");
        System.out.println("-".repeat(60));
        for (Problema problema : Problemas.TODOS) {
            Executor.ResultadoAgregado r = Executor.rodar(problema, config, EXECUCOES);
            System.out.printf("%-26s | %10.1f%% | %16.3f%n",
                    problema.toString(), r.percentualConvergencia(), r.tempoMedioMs);
        }
        System.out.println();
    }
}
