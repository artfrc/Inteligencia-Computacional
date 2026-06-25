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
    private static final int MUTATION_RATE = 10;

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
        while(generation < GENERATIONS) {
            // TODO: selecionar Pcross para formarem pares de pais para o cruzamento
            // TODO: recombinar genes (cruzamento)
            if(!((random.nextInt(100) + 1) > MUTATION_RATE)) {
                mutation(firstChild);
                mutation(sndChild);
            }
            // TODO: avaliar filhos
            // TODO: selecionar os individuos sobreviventes (nova populacao)

            Individual best = findBestIndividual();
            if(best.getFitness() == 0.) {
                break;
            }
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

    private void mutation(Individual ind1) {
        int[] chromosome = ind1.getChromosome();

        int pos1 = random.nextInt(CHROMOSOME_SIZE);
        int pos2 = random.nextInt(CHROMOSOME_SIZE);
        while (pos2 == pos1) {
            pos2 = random.nextInt(CHROMOSOME_SIZE);
        }

        int temp = chromosome[pos1];
        chromosome[pos1] = chromosome[pos2];
        chromosome[pos2] = temp;

        ind1.setChromosome(chromosome);
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
