package ufu.ci.experimentos;

import ufu.ci.ga.Configuracao;
import ufu.ci.ga.Problema;

import java.util.ArrayList;
import java.util.List;

// 2a etapa: a partir da melhor configuracao da 1a etapa, refina o AG variando
// parametros. Sao pelo menos 10 variacoes e nenhuma pode passar de 50% de
// acrescimo no tempo medio.
public class Etapa2 {

    private static final int EXECUCOES = 1000;

    public static Configuracao executar(Configuracao melhorEtapa1) {
        Problema problema = Problemas.SEND_MORE_MONEY;

        System.out.println("=== 2a ETAPA: refinamento sobre " + melhorEtapa1.nome
                + " (" + EXECUCOES + " execucoes cada) ===");
        System.out.println();

        Configuracao base = melhorEtapa1.copia();
        base.nome = "BASE (melhor etapa 1)";
        Executor.ResultadoAgregado resultadoBase = Executor.rodar(problema, base, EXECUCOES);
        double tempoLimite = resultadoBase.tempoMedioMs * 1.5;

        System.out.printf("%-34s | %-12s | %-16s | %-8s%n",
                "Variacao", "Convergencia", "Tempo medio (ms)", "Dentro 50%");
        System.out.println("-".repeat(82));
        imprimir(resultadoBase, tempoLimite);

        List<Configuracao> variacoes = montarVariacoes(melhorEtapa1);

        Configuracao melhorConfig = base;
        Executor.ResultadoAgregado melhorResultado = resultadoBase;

        for (Configuracao config : variacoes) {
            Executor.ResultadoAgregado r = Executor.rodar(problema, config, EXECUCOES);
            imprimir(r, tempoLimite);

            boolean dentroDoLimite = r.tempoMedioMs <= tempoLimite;
            if (dentroDoLimite && ehMelhor(r, melhorResultado)) {
                melhorResultado = r;
                melhorConfig = config;
            }
        }

        System.out.println();
        System.out.println(">> Melhor configuracao da 2a etapa: " + melhorResultado.nome
                + " (" + String.format("%.1f%%", melhorResultado.percentualConvergencia())
                + ", " + String.format("%.3f ms", melhorResultado.tempoMedioMs) + ")");
        System.out.println(">> Base (etapa 1): "
                + String.format("%.1f%%", resultadoBase.percentualConvergencia())
                + ", " + String.format("%.3f ms", resultadoBase.tempoMedioMs));
        System.out.println();
        return melhorConfig;
    }

    private static void imprimir(Executor.ResultadoAgregado r, double tempoLimite) {
        String dentro = r.tempoMedioMs <= tempoLimite ? "sim" : "NAO";
        System.out.printf("%-34s | %10.1f%% | %16.3f | %-8s%n",
                r.nome, r.percentualConvergencia(), r.tempoMedioMs, dentro);
    }

    private static boolean ehMelhor(Executor.ResultadoAgregado candidato, Executor.ResultadoAgregado atual) {
        if (candidato.convergencias != atual.convergencias) {
            return candidato.convergencias > atual.convergencias;
        }
        return candidato.tempoMedioMs < atual.tempoMedioMs;
    }

    // As variacoes priorizam mudancas de custo de tempo desprezivel (taxa de
    // mutacao, tour e taxa de crossover), porque aumentar populacao ou geracoes
    // tende a estourar o limite de 50%. Algumas variacoes mais caras entram so
    // para comparacao.
    private static List<Configuracao> montarVariacoes(Configuracao base) {
        List<Configuracao> lista = new ArrayList<>();

        Configuracao v01 = base.copia();
        v01.nome = "V01 mutacao 30%";
        v01.taxaMutacao = 0.30;
        lista.add(v01);

        Configuracao v02 = base.copia();
        v02.nome = "V02 mutacao 40%";
        v02.taxaMutacao = 0.40;
        lista.add(v02);

        Configuracao v03 = base.copia();
        v03.nome = "V03 mutacao 50%";
        v03.taxaMutacao = 0.50;
        lista.add(v03);

        Configuracao v04 = base.copia();
        v04.nome = "V04 tour 2";
        v04.tamanhoTorneio = 2;
        lista.add(v04);

        Configuracao v05 = base.copia();
        v05.nome = "V05 tour 4";
        v05.tamanhoTorneio = 4;
        lista.add(v05);

        Configuracao v06 = base.copia();
        v06.nome = "V06 tour 5";
        v06.tamanhoTorneio = 5;
        lista.add(v06);

        Configuracao v07 = base.copia();
        v07.nome = "V07 crossover 90%";
        v07.taxaCrossover = 0.90;
        lista.add(v07);

        Configuracao v08 = base.copia();
        v08.nome = "V08 elitismo 10%";
        v08.taxaElitismo = 0.10;
        lista.add(v08);

        Configuracao v09 = base.copia();
        v09.nome = "V09 mutacao 40% + tour 4";
        v09.taxaMutacao = 0.40;
        v09.tamanhoTorneio = 4;
        lista.add(v09);

        Configuracao v10 = base.copia();
        v10.nome = "V10 mutacao 40% + tour 4 + crossover 90%";
        v10.taxaMutacao = 0.40;
        v10.tamanhoTorneio = 4;
        v10.taxaCrossover = 0.90;
        lista.add(v10);

        Configuracao v11 = base.copia();
        v11.nome = "V11 populacao 120 + mutacao 40%";
        v11.tamanhoPopulacao = 120;
        v11.taxaMutacao = 0.40;
        lista.add(v11);

        Configuracao v12 = base.copia();
        v12.nome = "V12 geracoes 70 + mutacao 40%";
        v12.numeroGeracoes = 70;
        v12.taxaMutacao = 0.40;
        lista.add(v12);

        return lista;
    }
}
