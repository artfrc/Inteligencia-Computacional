package ufu.ci.ga;

// Guarda os parametros e a escolha de operadores de uma execucao do AG.
// Cada combinacao de campos e uma "variacao" avaliada no trabalho.
public class Configuracao {

    public enum MetodoSelecao { TORNEIO, ROLETA }
    public enum MetodoCrossover { CICLICO, PMX }
    public enum MetodoReinsercao { ORDENADA, ELITISMO_PURO }
    public enum FuncaoAptidao { ERRO_GLOBAL, ERRO_POSICIONAL }

    public String nome = "configuracao";

    public int tamanhoPopulacao = 100;
    public int numeroGeracoes = 50;
    public double taxaCrossover = 0.60;
    public double taxaMutacao = 0.10;
    public double taxaElitismo = 0.20;
    public int tamanhoTorneio = 3;

    public MetodoSelecao selecao = MetodoSelecao.TORNEIO;
    public MetodoCrossover crossover = MetodoCrossover.PMX;
    public MetodoReinsercao reinsercao = MetodoReinsercao.ORDENADA;
    public FuncaoAptidao funcaoAptidao = FuncaoAptidao.ERRO_GLOBAL;

    public Configuracao copia() {
        Configuracao c = new Configuracao();
        c.nome = nome;
        c.tamanhoPopulacao = tamanhoPopulacao;
        c.numeroGeracoes = numeroGeracoes;
        c.taxaCrossover = taxaCrossover;
        c.taxaMutacao = taxaMutacao;
        c.taxaElitismo = taxaElitismo;
        c.tamanhoTorneio = tamanhoTorneio;
        c.selecao = selecao;
        c.crossover = crossover;
        c.reinsercao = reinsercao;
        c.funcaoAptidao = funcaoAptidao;
        return c;
    }
}
