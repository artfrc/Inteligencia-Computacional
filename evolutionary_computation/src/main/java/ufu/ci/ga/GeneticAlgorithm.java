package ufu.ci.ga;

import ufu.ci.ga.model.Individual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GeneticAlgorithm {

    private static final int CHROMOSOME_SIZE = 10;
    private static final int FILLED_POSITIONS = 8;
    private static final int GENERATIONS = 50;
    private static final char[] GENES_OPTIONS = {'S', 'E', 'N', 'D', 'M', 'O', 'R', 'Y'};
    private static final int POPULATION_SIZE = 100;

    private final Random random = new Random();

    private List<Individual> population;

    public GeneticAlgorithm() {
        this.population = new ArrayList<>();
    }

    public void run() {
        generateInitialPopulation();
        // TODO: avaliar populacao
        System.out.println("Population generated with " + population.size() + " individuals.");
        int generation = 0;
        while(generation < GENERATIONS || findBestIndividual().getFitness() != 0.) {


            generation++;
        }
    }

    private void generateInitialPopulation() {
        Set<Individual> seen = new HashSet<>();
        while (seen.size() < POPULATION_SIZE) {
            seen.add(generateIndividual());
        }
        population.addAll(seen);
    }

    private Individual generateIndividual() {
        int[] chromosome = new int[CHROMOSOME_SIZE];
        Arrays.fill(chromosome, -1);

        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < CHROMOSOME_SIZE; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);

        List<Integer> genes = new ArrayList<>();
        for (int i = 0; i < GENES_OPTIONS.length; i++) {
            genes.add(i);
        }
        Collections.shuffle(genes);

        for (int j = 0; j < FILLED_POSITIONS; j++) {
            chromosome[positions.get(j)] = genes.get(j);
        }

        Individual individual = new Individual(CHROMOSOME_SIZE);
        individual.setChromosome(chromosome);
        System.out.println(individual.toString());
        return individual;
    }

    public Individual findBestIndividual() {
        Individual best = population.get(0);
        for (Individual individual : population) {
            if (individual.getFitness() < best.getFitness()) {
                best = individual;
            }
        }
        return best;
    }

    public List<Individual> getPopulation() {
        return population;
    }
}
