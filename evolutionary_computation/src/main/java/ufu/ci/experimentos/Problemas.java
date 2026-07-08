package ufu.ci.experimentos;

import ufu.ci.ga.Problema;

public class Problemas {

    public static final Problema SEND_MORE_MONEY = new Problema("SEND", "MORE", "MONEY");
    public static final Problema EAT_THAT_APPLE = new Problema("EAT", "THAT", "APPLE");
    public static final Problema CROSS_ROADS_DANGER = new Problema("CROSS", "ROADS", "DANGER");
    public static final Problema COCA_COLA_OASIS = new Problema("COCA", "COLA", "OASIS");
    public static final Problema DONALD_GERALD_ROBERT = new Problema("DONALD", "GERALD", "ROBERT");

    public static final Problema[] TODOS = {
            SEND_MORE_MONEY,
            EAT_THAT_APPLE,
            CROSS_ROADS_DANGER,
            COCA_COLA_OASIS,
            DONALD_GERALD_ROBERT
    };

    private Problemas() {
    }
}
