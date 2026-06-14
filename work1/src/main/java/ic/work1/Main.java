package ic.work1;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm();

        List<Individual> population = ga.generateInitialPopulation();
        Individual best = ga.findBestIndividual(population);

        System.out.println("Melhor cromossomo: " + best.chromosome());
        System.out.println("Fitness: " + best.fitness());

        while (best.fitness() != 0) {
            //TODO: selecionar os pais

            //TODO: fazer cruzamento

            //TODO: mutação dos novos indivíduos

            //TODO: avaliar filhos gerados

            //TODO: gerar nova população

        }

        System.out.println("Solução encontrada!");
    }
}
