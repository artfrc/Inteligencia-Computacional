package ufu.ci.ga.operator;

import ufu.ci.ga.model.Individual;

public class Mutation {

    private final double mutationRate;

    public Mutation(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void mutate(Individual individual) {
        throw new UnsupportedOperationException("Mutation not implemented yet");
    }
}
