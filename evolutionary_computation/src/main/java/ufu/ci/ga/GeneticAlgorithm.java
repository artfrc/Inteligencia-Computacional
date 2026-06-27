package ufu.ci.ga;

import ufu.ci.ga.model.Individual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GeneticAlgorithm {

    private static final int CHROMOSOME_SIZE = 10;
    private static final int FILLED_POSITIONS = 8;
    private static final int GENERATIONS = 50;
    private static final char[] GENES_OPTIONS = {'S', 'E', 'N', 'D', 'M', 'O', 'R', 'Y'};
    private static final int POPULATION_SIZE = 100;
    private static final int MUTATION_RATE = 20;
    private static final int ELITE_COUNT = (int) (POPULATION_SIZE * 0.2);

    private final Random random = new Random();
    private double totalFitness = 0.;

    private List<Individual> population;
    private static final Map<String, Integer> GENE_INDEX_MAP = buildGeneIndexMap();

    private static Map<String, Integer> buildGeneIndexMap() {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < GENES_OPTIONS.length; i++) {
            map.put(String.valueOf(GENES_OPTIONS[i]), i);
        }
        return Collections.unmodifiableMap(map);
    }

    public GeneticAlgorithm() {
        this.population = new ArrayList<>();
    }

    public void run() {
        generateInitialPopulation();
        fitness(population);
        // System.out.println("Population generated with " + population.size() + " individuals.");
        for (Individual ind : population) {
            System.out.println(ind);
        }
        int generation = 0;
        while (generation < GENERATIONS) {
            Individual best = findBestIndividual();
            System.out.println("Geração " + generation + " | Melhor fitness: " + best.getFitness());
            if (best.getFitness() == 0.) {
                break;
            }

            // seleciona pais e gera filhos
            List<Individual> newPopulation = new ArrayList<>();
            int crossoverCount = 0;
            while (newPopulation.size() < POPULATION_SIZE - ELITE_COUNT) {
                Individual parent1 = rouletteSelect();
                Individual parent2;
                do {
                    parent2 = rouletteSelect();
                } while (parent2.equals(parent1));

                System.out.println("  [Cruzamento " + (++crossoverCount) + "]");
                System.out.println("    Pai 1: " + parent1);
                System.out.println("    Pai 2: " + parent2);

                List<Individual> children = pmx(parent1, parent2);
                Individual firstChild = children.get(0);
                Individual sndChild = children.get(1);

                if (!((random.nextInt(100) + 1) > MUTATION_RATE)) {
                    mutation(firstChild);
                    mutation(sndChild);
                    System.out.println("    >> mutacao aplicada");
                }
                fitness(children);

                System.out.println("    Filho 1: " + firstChild);
                System.out.println("    Filho 2: " + sndChild);

                newPopulation.add(firstChild);
                if (newPopulation.size() < POPULATION_SIZE - ELITE_COUNT) {
                    newPopulation.add(sndChild);
                }
            }

            // nova populacao: elites + filhos gerados
            population.sort(Comparator.comparingDouble(Individual::getFitness));
            newPopulation.addAll(population.subList(0, ELITE_COUNT));
            population = newPopulation;

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
        return individual;
    }

    private List<Individual> fitness(List<Individual> individualList) {
        for (Individual ind : individualList) {
            int[] chromosome = ind.getChromosome();

            // Monta mapa inverso: índice da letra -> dígito atribuído
            // chromosome[dígito] = índice_da_letra  (ou -1 se posição vazia)
            int[] digitForLetter = new int[GENES_OPTIONS.length];
            Arrays.fill(digitForLetter, -1);
            for (int digit = 0; digit < CHROMOSOME_SIZE; digit++) {
                int letterIdx = chromosome[digit];
                if (letterIdx != -1) {
                    digitForLetter[letterIdx] = digit;
                }
            }

            int s = digitForLetter[GENE_INDEX_MAP.get("S")];
            int e = digitForLetter[GENE_INDEX_MAP.get("E")];
            int n = digitForLetter[GENE_INDEX_MAP.get("N")];
            int d = digitForLetter[GENE_INDEX_MAP.get("D")];
            int m = digitForLetter[GENE_INDEX_MAP.get("M")];
            int o = digitForLetter[GENE_INDEX_MAP.get("O")];
            int r = digitForLetter[GENE_INDEX_MAP.get("R")];
            int y = digitForLetter[GENE_INDEX_MAP.get("Y")];

            long send  =  1000L*s + 100*e + 10*n + d;
            long more  =  1000L*m + 100*o + 10*r + e;
            long money = 10000L*m + 1000*o + 100*n + 10*e + y;

            double fitnessValue = Math.abs(send + more - money);

            ind.setFitness(fitnessValue);
            ind.setScore(1.0 / fitnessValue);
        }
        return individualList;
    }

    private Individual rouletteSelect() {
        double totalWeight = 0;
        for (Individual ind : population) {
            totalWeight += ind.getScore();
        }

        double r = random.nextDouble() * totalWeight;
        double accumulated = 0;
        for (Individual ind : population) {
            accumulated += ind.getScore();
            if (accumulated >= r) {
                return ind;
            }
        }
        return population.get(population.size() - 1);
    }

    private Individual findBestIndividual() {
        Individual best = population.get(0);
        for (Individual individual : population) {
            if (individual.getFitness() < best.getFitness()) {
                best = individual;
            }
        }
        return best;
    }

    private List<Individual> pmx(Individual parent1, Individual parent2) {
        int len = parent1.getChromosome().length;
        int[] p1 = parent1.getChromosome();
        int[] p2 = parent2.getChromosome();

        int left = random.nextInt(len);
        int right = random.nextInt(len);
        while (right == left) right = random.nextInt(len);
        if (left > right) { int t = left; left = right; right = t; }

        // c1 herda segmento de p2, c2 herda segmento de p1
        int[] c1 = Arrays.copyOf(p1, len);
        int[] c2 = Arrays.copyOf(p2, len);
        for (int i = left; i <= right; i++) {
            c1[i] = p2[i];
            c2[i] = p1[i];
        }

        Set<Integer> seg1 = segmentGenes(p2, left, right);
        Set<Integer> seg2 = segmentGenes(p1, left, right);

        for (int i = 0; i < len; i++) {
            if (i >= left && i <= right) continue;
            c1[i] = resolveGene(p1[i], p1, p2, left, right, seg1);
            c2[i] = resolveGene(p2[i], p2, p1, left, right, seg2);
        }

        Individual child1 = new Individual(len);
        Individual child2 = new Individual(len);
        child1.setChromosome(c1);
        child2.setChromosome(c2);
        return List.of(child1, child2);
    }

    private Set<Integer> segmentGenes(int[] parent, int left, int right) {
        Set<Integer> genes = new HashSet<>();
        for (int i = left; i <= right; i++) {
            if (parent[i] != -1) genes.add(parent[i]);
        }
        return genes;
    }

    // Segue a cadeia de mapeamento PMX até encontrar um gene sem conflito com o segmento
    private int resolveGene(int gene, int[] from, int[] to, int left, int right, Set<Integer> segmentGenes) {
        int current = gene;
        while (current != -1 && segmentGenes.contains(current)) {
            int pos = indexInSegment(to, current, left, right);
            if (pos == -1) break;
            current = from[pos];
        }
        return current;
    }

    private int indexInSegment(int[] arr, int value, int left, int right) {
        for (int i = left; i <= right; i++) {
            if (arr[i] == value) return i;
        }
        return -1;
    }

    public List<Individual> getPopulation() {
        return population;
    }

    public void setPopulation(List<Individual> population) {
        this.population = population;
    }

    public double getTotalFitness() {
        return totalFitness;
    }

    public void setTotalFitness(double totalFitness) {
        this.totalFitness = totalFitness;
    }


}
