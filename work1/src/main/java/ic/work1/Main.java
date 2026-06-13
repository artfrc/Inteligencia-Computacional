package ic.work1;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        Set<String> population = ga.generateInitialPopulation();

        System.out.println("Population size: " + population.size());
        int i = 1;
        for (String chromosome : population) {
            System.out.println("Chromosome " + i++ + ": " + chromosome);
        }
    }
}