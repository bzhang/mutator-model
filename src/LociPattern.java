/**
 * Created by bingjun at 7/15/11 12:35 AM
 */

import java.util.Random;

public class LociPattern {


    private int firstNeutralPosition;

    public enum LocusType {
        Fitness, Mutator, Recombination, Neutral;
    }
    private int genomeSize;
    private LocusType[] pattern;

    private int[] mutatorLociPositions, recombinationLociPositions, neutralLociPositions;
    public LociPattern(int nFitness, int nMutator, int nRecombination, int nNeutral) {

        genomeSize = nFitness + nMutator + nRecombination + nNeutral;
        pattern = new LocusType[genomeSize];
        mutatorLociPositions = new int[nMutator];
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
            mutatorLociPositions[i] = position;
        }

        for (int i = 0; i < nRecombination; i++) {
            int position = getRandomLocation();
            while (pattern[position] != LocusType.Fitness) {
                position = getRandomLocation();
            }
            pattern[position] = LocusType.Recombination;
            recombinationLociPositions[i] = position;
        }

        for (int i = 0; i < nNeutral; i++) {
            int position = getRandomLocation();
            while (pattern[position] != LocusType.Neutral) {
                position = getRandomLocation();
            }
            pattern[position] = LocusType.Neutral;
            neutralLociPositions[i] = position;
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

    public int[] getMutatorLociPositions() {
        return mutatorLociPositions;
    }

    //    private Random random = new Random(System.nanoTime());
    public int getFirstRecombinationPosition() {
        return recombinationLociPositions[0];
    }

    public int getFirstNeutralPosition() {
        return neutralLociPositions[0];
    }

    public int getFirstMutatorPosition() {
        return mutatorLociPositions[0];
    }

    public int getRandomMutatorPosition() {
        int position;
//        System.out.println(mutatorLociPositions.length);
        int index = Rand.getInt(mutatorLociPositions.length);
        position = mutatorLociPositions[index];
//        do {
//            position = getRandomLocation();
//        } while (getLocusType(position) != LocusType.Mutator);
        return position;
    }

    public int getRandomFitnessPosition() {
        int position;
        do {
            position = getRandomLocation();
        } while (getLocusType(position) != LocusType.Fitness);
        return position;
    }

    private int getRandomLocation() {
        return Rand.getInt(genomeSize);
    }

}
