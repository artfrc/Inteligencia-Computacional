package ufu.ci.ga.model;

import java.util.Arrays;

public class Individual {

    private int[] chromosome;
    private double fitness;

    public Individual(int size) {
        this.chromosome = new int[size];
        this.fitness = 0.0;
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(int[] chromosome) {
        this.chromosome = chromosome;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Individual)) return false;
        return Arrays.equals(chromosome, ((Individual) o).chromosome);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(chromosome);
    }

    @Override
    public String toString() {
        return "Individual{chromosome=" + Arrays.toString(chromosome) + ", fitness=" + fitness + '}';
    }
}
