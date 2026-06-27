package ufu.ci;

import ufu.ci.ga.AlgoritmoGenetico;
import ufu.ci.ga.Configuracao;
import ufu.ci.ga.Problema;

public class Main {

    public static void main(String[] args) {
        Problema problema = new Problema("SEND", "MORE", "MONEY");
        AlgoritmoGenetico ag = new AlgoritmoGenetico(problema, new Configuracao());
        AlgoritmoGenetico.Resultado resultado = ag.executar();

        System.out.println("Convergiu: " + resultado.convergiu + " em " + resultado.geracoes + " geracoes");
        if (resultado.convergiu) {
            System.out.println(problema.descricaoSolucao(resultado.melhor.getCromossomo()));
        }
    }
}
