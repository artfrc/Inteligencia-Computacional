package ufu.ci.ga.model;

import java.util.Arrays;

public class Individual {

    private int[] chromosome;
    private double fitness;
    private double score;

    public Individual(int size) {
        this.chromosome = new int[size];
        Arrays.fill(this.chromosome, -1);
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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
