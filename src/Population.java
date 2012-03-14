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

    public Population(Population parent, int currentGeneration, String mutMapFilename) {
//        Long start = System.currentTimeMillis();

        // Create the next generation
        lociPattern = parent.lociPattern;
        individuals = new ArrayList<Individual>();
        int counter = 0;
        ArrayList<Object> mutationProperties = new ArrayList<Object>();
        String mutMapFileOutput;

        while (getSize() < parent.getSize()) {
            int previousSize = getSize();
            IndividualPair parentPair = parent.getRandomIndividualPair();
            IndividualPair offspringPair = parentPair.reproduce();
            offspringPair.mutate(currentGeneration, mutationProperties);
            addIndividualPair(offspringPair, parent.getSize());
            if (getSize() - previousSize == 0) {
                counter++;
                if (counter == ModelParameters.getInt("LOOP_LIMIT")) {
                    System.err.println("Parent -> offspring loop reach the limit!");
                    System.exit(0);
                }
            }
        }

        mutMapFileOutput = Util.outputMutMap(mutationProperties);
        Util.writeFile(mutMapFilename, mutMapFileOutput);
    }

    public float[] getFitnessArray() {
        float[] fitnessArray = new float[getSize()];

        Long timeB4IteratingAllIndividuals = System.currentTimeMillis();
        for (int i = 0; i < getSize(); i++) {
            fitnessArray[i] = getIndividual(i).getFitness();
        }
        int reminderIteraringAllIndividuals = (int) ((System.currentTimeMillis() - timeB4IteratingAllIndividuals) % (24L * 3600 * 1000));
        Float secondsElapsedIteratingAllIndividuals = (float) reminderIteraringAllIndividuals / 1000;
        System.out.println("Seconds elapsed for get fitness of all individuals = " + secondsElapsedIteratingAllIndividuals);

        return fitnessArray;
    }

    public int[] getMutatorStrengthArray() {
        int[] mutatorStrengthArray = new int[getSize()];
        for (int i = 0; i < getSize(); i++) {
            mutatorStrengthArray[i] = getIndividual(i).getMutatorStrength();
        }
        return mutatorStrengthArray;
    }

    public int[] getNDeleMutArray() {
        int[] nDeleMutArray = new int[getSize()];
        for (int i = 0; i < getSize(); i++) {
            nDeleMutArray[i] = getIndividual(i).getNDeleMut();
        }
        return nDeleMutArray;
    }

    public int[] getNBeneMutArray() {
        int[] nBeneMutArray = new int[getSize()];
        for (int i = 0; i < getSize(); i++) {
            nBeneMutArray[i] = getIndividual(i).getNBeneMut();
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

    private IndividualPair getRandomIndividualPair() {
//        Long timeB4GetFitnessArray = System.currentTimeMillis();

        float[] randomWeights = getFitnessArray();

//        Long timeAfterGetFitnessArray = System.currentTimeMillis();
//        int reminderGetFitnessArray = (int) ((timeAfterGetFitnessArray - timeB4GetFitnessArray) % (24L * 3600 * 1000));
//        Float secondsElapsedGetFitnessArray = (float) reminderGetFitnessArray / 1000;
//        System.out.println("Seconds elapsed for getting fitness array = " + secondsElapsedGetFitnessArray);


        WeightedRandomGenerator wrg = new WeightedRandomGenerator(randomWeights);
        int indexA = wrg.nextInt();
        int indexB = wrg.nextInt();
        while (indexA == indexB) {
            indexB = wrg.nextInt();
        }
        Individual individualA = getIndividual(indexA);
        Individual individualB = getIndividual(indexB);

//        Long timeAfterGeneratingRanIndividuals = System.currentTimeMillis();
//        int reminderGeneratingRanIndividuals = (int) ((timeAfterGeneratingRanIndividuals - timeAfterGeneratingRanIndividuals) % (24L * 3600 * 1000));
//        Float secondsElapsedGeneratingRanIndividuals = (float) reminderGeneratingRanIndividuals / 1000;
//        System.out.println("Seconds elapsed for randomly getting parents = " + secondsElapsedGeneratingRanIndividuals);

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
