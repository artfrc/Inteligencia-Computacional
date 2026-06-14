package ic.work1;

import java.util.Set;

public class Main {

    private static final int MAX_ITERATIONS = 10000;
    private static final int EXPERIMENTS = 10000;

    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        int totalIterations = 0;
        int successfulExperiments = 0;

        for (int exp = 1; exp <= EXPERIMENTS; exp++) {
            for (int iteration = 1; iteration <= MAX_ITERATIONS; iteration++) {
                Set<String> population = ga.generateInitialPopulation();
                int bestFitness = Integer.MAX_VALUE;

                for (String chromosome : population) {
                    bestFitness = Math.min(bestFitness, ga.fitness(chromosome.toCharArray()));
                }

                if (bestFitness == 0) {
                    System.out.println("Experimento " + exp + " | Solução encontrada na iteração " + iteration);
                    totalIterations += iteration;
                    successfulExperiments++;
                    break;
                }
            }
        }

        System.out.println("\n=== Resultado ===");
        System.out.println("Experimentos com solução: " + successfulExperiments + "/" + EXPERIMENTS);
        System.out.println("Média de iterações: " + (totalIterations / (double) successfulExperiments));
    }
}