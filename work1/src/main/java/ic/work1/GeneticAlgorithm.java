package ic.work1;

import java.util.*;

public class GeneticAlgorithm {

    private static final char[] GENES = {'S', 'E', 'N', 'D', 'M', 'O', 'R', 'Y'};
    private static final int CHROMOSOME_SIZE = 8;
    private static final int POPULATION_SIZE = 100;
    private static final int NUMBER_GENERATIONS = 10;
    private static final double MUTATION_RATE = 0.1;
    private static final double CROSSOVER_RATE = 0.6;

    private final Random random = new Random();

    public Set<String> generateInitialPopulation() {
        Set<String> population = new HashSet<>();

        while (population.size() < POPULATION_SIZE) {
            population.add(new String(generateChromosome()));
        }

        return population;
    }

    private char[] generateChromosome() {
        char[] chromosome = new char[CHROMOSOME_SIZE];

        for (int i = 0; i < CHROMOSOME_SIZE; i++) {
            chromosome[i] = GENES[random.nextInt(GENES.length)];
        }

        return chromosome;
    }
}