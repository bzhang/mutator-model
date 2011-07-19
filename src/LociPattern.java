/**
 * Created by bingjun at 7/15/11 12:35 AM
 */

import java.util.Random;

public class LociPattern {

    public enum LocusType {
        Fitness, Mutator, Recombination
    }

    private int genomeSize;
    private LocusType[] pattern;
    private int[] recombinationLociPositions;

    private Random random = new Random(System.nanoTime());

    public LociPattern(int nFitness, int nMutator, int nRecombination) {

        genomeSize = nFitness + nMutator + nRecombination;
        pattern = new LocusType[genomeSize];
        recombinationLociPositions = new int[nRecombination];

        for (int i = 0; i < genomeSize; i++) {
            pattern[i] = LocusType.Fitness;
        }

        for (int i = 0; i < nMutator; i++) {
            int position = getRandomLocation();
            while (pattern[position] != LocusType.Fitness) {
                position = getRandomLocation();
            }
            pattern[position] = LocusType.Mutator;
        }

        for (int i = 0; i < nRecombination; i++) {
            int position = getRandomLocation();
            while (pattern[position] != LocusType.Fitness) {
                position = getRandomLocation();
            }
            pattern[position] = LocusType.Recombination;
            recombinationLociPositions[i] = position;
        }
    }

    public int getGenomeSize() {
        return genomeSize;
    }

    public LocusType getLocusType(int location) {
        return pattern[location];
    }

    public int[] getRecombinationLociPositions() {
        return recombinationLociPositions;
    }

    private int getRandomLocation() {
        return random.nextInt(genomeSize);
    }
}
