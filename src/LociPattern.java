/**
 * Created by bingjun at 7/15/11 12:35 AM
 */

import java.util.Random;

public class LociPattern {

    private int length;
    private Random random = new Random(System.nanoTime());

    public enum LocusType {
        Fitness, Mutator, Recombination
    }

    private LocusType[] pattern;

    public LociPattern(int nFitness, int nMutator, int nRecombination) {

        length = nFitness + nMutator + nRecombination;
        pattern = new LocusType[length];

        for (int i = 0; i < length; i++) {
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
        }
    }

    private int getRandomLocation() {
        return random.nextInt(length);
    }

    public LocusType getLocusType(int location) {
        return pattern[location];
    }
}
