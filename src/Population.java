/**
 * @author Bingjun
 * 5/16/11 12:32 AM
 */

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Population {

    private ArrayList<Individual> individuals;
    private LociPattern lociPattern;
    private Random random = new Random(System.nanoTime());

    public Population(int nIndividuals) {
        // Create the founder population
        lociPattern = new LociPattern(ModelParameters.getInt("N_FITNESS_LOCI"),
                ModelParameters.getInt("N_MUTATOR_LOCI"), ModelParameters.getInt("N_RECOMBINATION_LOCI"));
        individuals = new ArrayList<Individual>();

        for (int i = 0; i < nIndividuals; i++) {
            Individual individual = new Individual(lociPattern);
            individuals.add(individual);

            for (int location = 0; location < ModelParameters.getGenomeSize(); location++) {
                if (lociPattern.getLocusType(location) == LociPattern.LocusType.Fitness) {
                    individual.setFitnessLocus(location);
                }
                else if (lociPattern.getLocusType(location) == LociPattern.LocusType.Mutator) {
                    individual.setMutatorLocus(location, getRandomMutatorStrength());
                }
                else {
                    individual.setRecombinationLocus(location, getRandomRecombinationStrength());
                }
            }
        }
    }

    public Population(Population parent, int currentGeneration, Map mutFitnessMap, String mutMapFilename) {
        Long start = System.currentTimeMillis();

        // Create the next generation
        lociPattern = parent.lociPattern;
        individuals = new ArrayList<Individual>();
        int counter = 0;
        ArrayList<Object> mutationProperties = new ArrayList<Object>();
        String mutMapFileOutput;

        while (getSize() < parent.getSize()) {
//            Long init = System.currentTimeMillis();
            int previousSize = getSize();
            IndividualPair parentPair = parent.getRandomIndividualPair(mutFitnessMap);

//            Long timeAfterRanIndividualPair = System.currentTimeMillis();
//            int reminderRandomIndividualPair = (int) ((timeAfterRanIndividualPair - init) % (24L * 3600 * 1000));
//        Float hoursElapsed = (float) reminder / (3600 * 1000);
//        System.out.println("Hours elapsed = " + hoursElapsed);
//            Float secondsElapsedRandomIndividualPair = (float) reminderRandomIndividualPair / 1000;
//            System.out.println("Seconds elapsed for getRandomIndividualPair = " + secondsElapsedRandomIndividualPair);

            IndividualPair offspringPair = parentPair.reproduce();

//            Long timeAfterReproduce = System.currentTimeMillis();
//            int reminderReproduce = (int) ((timeAfterReproduce - timeAfterRanIndividualPair) % (24L * 3600 * 1000));
////        Float hoursElapsed = (float) reminder / (3600 * 1000);
////        System.out.println("Hours elapsed = " + hoursElapsed);
//            Float secondsElapsedReproduce = (float) reminderReproduce / 1000;
//            System.out.println("Seconds elapsed for reproduce = " + secondsElapsedReproduce);

            offspringPair.mutate(currentGeneration, mutFitnessMap, mutationProperties);

//            Long timeAfterMutate = System.currentTimeMillis();
//            int reminderMutate = (int) ((timeAfterMutate - timeAfterReproduce) % (24L * 3600 * 1000));
////        Float hoursElapsed = (float) reminder / (3600 * 1000);
////        System.out.println("Hours elapsed = " + hoursElapsed);
//            Float secondsElapsedMutate = (float) reminderMutate / 1000;
//            System.out.println("Seconds elapsed for mutate = " + secondsElapsedMutate);


            addIndividualPair(offspringPair, parent.getSize());
            if (getSize() - previousSize == 0) {
                counter++;
                if (counter == ModelParameters.getInt("LOOP_LIMIT")) {
                    System.err.println("Parent -> offspring loop reach the limit!");
                    System.exit(0);
                }
            }
        }

//        Long timeAfterOneGen = System.currentTimeMillis();
//        int reminderOneGen = (int) ((timeAfterOneGen - start) % (24L * 3600 * 1000));
////        Float hoursElapsed = (float) reminder / (3600 * 1000);
////        System.out.println("Hours elapsed = " + hoursElapsed);
//        Float secondsElapsedOneGen = (float) reminderOneGen / 1000;
//        System.out.println("Seconds elapsed for creating a new pop for next generation = " + secondsElapsedOneGen);

        mutMapFileOutput = Util.outputMutMap(mutationProperties);
        Util.writeFile(mutMapFilename, mutMapFileOutput);

//        int reminderOutputMutMap = (int) ((System.currentTimeMillis() - timeAfterOneGen) % (24L * 3600 * 1000));
////        Float hoursElapsed = (float) reminder / (3600 * 1000);
////        System.out.println("Hours elapsed = " + hoursElapsed);
//        Float secondsElapsedOutputMutMap = (float) reminderOutputMutMap / 1000;
//        System.out.println("Seconds elapsed for outputMutMap = " + secondsElapsedOutputMutMap);
    }

    public float[] getFitnessArray(Map mutFitnessMap) {
        float[] fitnessArray = new float[getSize()];
        for (int i = 0; i < getSize(); i++) {
            fitnessArray[i] = getIndividual(i).getFitness(mutFitnessMap);
        }
        return fitnessArray;
    }

    public int[] getMutatorStrengthArray() {
        int[] mutatorStrengthArray = new int[getSize()];
        for (int i = 0; i < getSize(); i++) {
            mutatorStrengthArray[i] = getIndividual(i).getMutatorStrength();
        }
        return mutatorStrengthArray;
    }

    public int[] getNDeleMutArray(Map mutFitnessMap) {
        int[] nDeleMutArray = new int[getSize()];
        for (int i = 0; i < getSize(); i++) {
            nDeleMutArray[i] = getIndividual(i).getNDeleMut(mutFitnessMap);
        }
        return nDeleMutArray;
    }

    public int[] getNBeneMutArray(Map mutFitnessMap) {
        int[] nBeneMutArray = new int[getSize()];
        for (int i = 0; i < getSize(); i++) {
            nBeneMutArray[i] = getIndividual(i).getNBeneMut(mutFitnessMap);
        }
        return nBeneMutArray;
    }

    private void addIndividualPair(IndividualPair offspringPair, int parentSize) {
        addIndividual(offspringPair.getIndividualA(), parentSize);
        addIndividual(offspringPair.getIndividualB(), parentSize);
    }

    private void addIndividual(Individual individual, int parentSize) {
        if (getSize() < parentSize && individual.isAlive()) {
            individuals.add(individual);
        }
    }

    private IndividualPair getRandomIndividualPair(Map mutFitnessMap) {
        float[] randomWeights = getFitnessArray(mutFitnessMap);
        WeightedRandomGenerator wrg = new WeightedRandomGenerator(randomWeights);
        int indexA = wrg.nextInt();
        int indexB = wrg.nextInt();
        while (indexA == indexB) {
            indexB = wrg.nextInt();
        }
        Individual individualA = getIndividual(indexA);
        Individual individualB = getIndividual(indexB);
        return new IndividualPair(individualA, individualB);
    }

    private int getRandomMutatorStrength() {
        int strength = 1;
        // Generate mutator locus, strength ranging from [2, MUTATOR_STRENGTH_MAX]
        if (random.nextFloat() < ModelParameters.getFloat("MUTATOR_RATIO")) {
            strength = random.nextInt(ModelParameters.getInt("MUTATOR_STRENGTH_MAX") - 1) + 2;
        }
        return strength;
    }

    private float getRandomRecombinationStrength() {
        float strength = 0;
        // Generate recombination locus (sexual), strength ranging from (0.0, 1.0]
        if (random.nextFloat() < ModelParameters.getFloat("RECOMBINATION_RATIO")) {
            strength = random.nextFloat();
        }
        return strength;
    }

    public ArrayList<Individual> getIndividualArray() {
        return individuals;
    }

    public Individual getIndividual(int index) {
        return individuals.get(index);
    }

    public int getSize() {
        return individuals.size();
    }
}
