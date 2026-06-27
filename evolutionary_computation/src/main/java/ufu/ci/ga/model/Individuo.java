package ufu.ci.ga.model;

import java.util.Arrays;

// Uma solucao candidata. O cromossomo e um vetor de tamanho 10 (um por digito
// de 0 a 9): a posicao e o digito e o valor e o indice da letra associada a ele.
// Como sao 10 posicoes e 10 valores, e sempre uma permutacao de 0..9. Quando o
// problema tem menos de 10 letras, os indices que sobram nao aparecem em nenhuma
// palavra (letras "fantasma").
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
