package ufu.ci;

import ufu.ci.experimentos.Etapa1;
import ufu.ci.experimentos.Etapa2;
import ufu.ci.ga.Configuracao;

public class Main {

    public static void main(String[] args) {
        Configuracao melhorEtapa1 = Etapa1.executar();
        Etapa2.executar(melhorEtapa1);
    }
}
