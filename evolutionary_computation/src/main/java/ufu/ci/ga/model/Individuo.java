package ufu.ci.ga.model;

import java.util.Arrays;

public class Individuo {

    private int[] cromossomo;
    private double aptidao;
    private double pontuacao;

    public Individuo(int tamanho) {
        this.cromossomo = new int[tamanho];
        Arrays.fill(this.cromossomo, -1);
        this.aptidao = 0.0;
    }

    public int[] getCromossomo() {
        return cromossomo;
    }

    public void setCromossomo(int[] cromossomo) {
        this.cromossomo = cromossomo;
    }

    public double getAptidao() {
        return aptidao;
    }

    public void setAptidao(double aptidao) {
        this.aptidao = aptidao;
    }

    public double getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(double pontuacao) {
        this.pontuacao = pontuacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Individuo)) return false;
        return Arrays.equals(cromossomo, ((Individuo) o).cromossomo);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cromossomo);
    }

    @Override
    public String toString() {
        return "Individuo{cromossomo=" + Arrays.toString(cromossomo) + ", aptidao=" + aptidao + '}';
    }
}
