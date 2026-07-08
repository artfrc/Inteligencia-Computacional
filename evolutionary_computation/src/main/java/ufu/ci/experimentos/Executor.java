package ufu.ci.experimentos;

import ufu.ci.ga.AlgoritmoGenetico;
import ufu.ci.ga.Configuracao;
import ufu.ci.ga.Problema;

import java.util.Random;

public class Executor {

    public static class ResultadoAgregado {
        public final String nome;
        public final int execucoes;
        public final int convergencias;
        public final double tempoMedioMs;
        public final double mediaGeracoes;

        public ResultadoAgregado(String nome, int execucoes, int convergencias,
                                 double tempoMedioMs, double mediaGeracoes) {
            this.nome = nome;
            this.execucoes = execucoes;
            this.convergencias = convergencias;
            this.tempoMedioMs = tempoMedioMs;
            this.mediaGeracoes = mediaGeracoes;
        }

        public double percentualConvergencia() {
            return 100.0 * convergencias / execucoes;
        }
    }

    public static ResultadoAgregado rodar(Problema problema, Configuracao config, int execucoes) {
        Random aleatorio = new Random();
        long tempoTotalNano = 0;
        int convergencias = 0;
        long totalGeracoes = 0;

        for (int i = 0; i < execucoes; i++) {
            AlgoritmoGenetico ag = new AlgoritmoGenetico(problema, config, aleatorio);
            long inicio = System.nanoTime();
            AlgoritmoGenetico.Resultado resultado = ag.executar();
            tempoTotalNano += System.nanoTime() - inicio;

            if (resultado.convergiu) {
                convergencias++;
            }
            totalGeracoes += resultado.geracoes;
        }

        double tempoMedioMs = (tempoTotalNano / 1_000_000.0) / execucoes;
        double mediaGeracoes = (double) totalGeracoes / execucoes;
        return new ResultadoAgregado(config.nome, execucoes, convergencias, tempoMedioMs, mediaGeracoes);
    }
}
