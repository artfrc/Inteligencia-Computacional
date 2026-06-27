package ufu.ci;

import ufu.ci.ga.Problema;

public class Main {

    public static void main(String[] args) {
        Problema problema = new Problema("SEND", "MORE", "MONEY");
        System.out.println("Problema: " + problema);
        System.out.println("Letras distintas: " + problema.getNumeroLetras());
    }
}
