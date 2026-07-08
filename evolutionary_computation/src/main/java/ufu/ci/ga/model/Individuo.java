package ufu.ci.ga.model;

import java.util.Arrays;

public class Individuo {

    private int[] cromossomo;
    private double aptidao;

    public Individuo(int[] cromossomo) {
        this.cromossomo = cromossomo;
        this.aptidao = Double.MAX_VALUE;
    }

    public int[] getCromossomo() {
        return cromossomo;
    }

    public double getAptidao() {
        return aptidao;
    }

    public void setAptidao(double aptidao) {
        this.aptidao = aptidao;
    }

    public Individuo copia() {
        Individuo novo = new Individuo(Arrays.copyOf(cromossomo, cromossomo.length));
        novo.aptidao = this.aptidao;
        return novo;
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
