package ufu.ci;

import ufu.ci.experimentos.Etapa1;
import ufu.ci.experimentos.Etapa2;
import ufu.ci.experimentos.Etapa3;
import ufu.ci.ga.Configuracao;

// Roda as tres etapas de experimentos em sequencia. A melhor configuracao de
// cada etapa e usada como ponto de partida da etapa seguinte.
public class Main {

    public static void main(String[] args) {
        long inicio = System.nanoTime();

        Configuracao melhorEtapa1 = Etapa1.executar();
        Configuracao melhorEtapa2 = Etapa2.executar(melhorEtapa1);
        Etapa3.executar(melhorEtapa2);

        double minutos = (System.nanoTime() - inicio) / 1_000_000_000.0 / 60.0;
        System.out.printf("Tempo total dos experimentos: %.2f min%n", minutos);
    }
}
