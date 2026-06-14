package ic.work1;

import java.util.*;

public class GeneticAlgorithm {

    private static final char[] GENES = {'S', 'E', 'N', 'D', 'M', 'O', 'R', 'Y'};
    private static final int CHROMOSOME_SIZE = 10;
    private static final int SHUFFLE_ARRAY_SZ = 8;
    private static final int POPULATION_SIZE = 100;
    private static final int NUMBER_GENERATIONS = 10;
    private static final double MUTATION_RATE = 0.1;
    private static final double CROSSOVER_RATE = 0.6;

    private final Random random = new Random();

    public Set<String> generateInitialPopulation() {
        Set<String> population = new HashSet<>();

        while (population.size() < POPULATION_SIZE) {
            List<Integer> geneOrder = generateShuffleArray(SHUFFLE_ARRAY_SZ);
            population.add(new String(generateChromosome(geneOrder)));
        }

        return population;
    }

    private char[] generateChromosome(List<Integer> geneOrder) {
        char[] chromosome = new char[CHROMOSOME_SIZE];

        Arrays.fill(chromosome, '-');

        for (int i = 0; i < SHUFFLE_ARRAY_SZ; i++) {
            int idx = geneOrder.getLast();
            chromosome[idx] = GENES[random.nextInt(GENES.length)];
            geneOrder.removeLast();
        }


        return chromosome;
    }

    // Gerar uma ordem aleatória para os genes do cromossomo ser gerado
    private List<Integer> generateShuffleArray(int size) {
        List<Integer> shuffleArray = new ArrayList<>();
        for (int i = 0; i < CHROMOSOME_SIZE; i++) { // 0 até 9
            shuffleArray.add(i);
        }
        Collections.shuffle(shuffleArray);

        return shuffleArray.subList(0, size); // pega só 8
    }

    public int fitness(char[] chromosome) {
        int S=-1, E=-1, N=-1, D=-1, M=-1, O=-1, R=-1, Y=-1;

        for (int digit = 0; digit < chromosome.length; digit++) {
            switch (chromosome[digit]) {
                case 'S' -> S = digit;
                case 'E' -> E = digit;
                case 'N' -> N = digit;
                case 'D' -> D = digit;
                case 'M' -> M = digit;
                case 'O' -> O = digit;
                case 'R' -> R = digit;
                case 'Y' -> Y = digit;
            }
        }

        int SEND  =             S*1000 + E*100 + N*10 + D;
        int MORE  =             M*1000 + O*100 + R*10 + E;
        int MONEY = M*10000 + O*1000  + N*100 + E*10 + Y;

        return Math.abs((SEND + MORE) - MONEY); // 0 = solução perfeita
    }
}