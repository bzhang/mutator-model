/**
 * @author Bingjun Zhang
 */

import java.util.ArrayList;

public class Population {

    private ArrayList<Individual> individuals;
    private LociPattern lociPattern;

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
        // Create the next generation
        lociPattern = parent.lociPattern;
        individuals = new ArrayList<Individual>();
        int counter = 0;
        ArrayList<Object> mutationProperties = new ArrayList<Object>();
        String mutMapFileOutput;
        double[] totals = initTotals(parent.getFitnessArray());

        while (getSize() < parent.getSize()) {
            int previousSize = getSize();
            IndividualPair parentPair = parent.getRandomIndividualPair(totals);
            IndividualPair offspringPair = parentPair.reproduce();
            offspringPair.mutate(currentGeneration, mutationProperties);
            addIndividualPair(offspringPair, parent.getSize());
            if (getSize() - previousSize == 0) {
                counter++;
                System.err.println("Parent -> offspring loop no." + counter);
                if (counter == ModelParameters.getInt("LOOP_LIMIT")) {
                    System.err.println("Parent -> offspring loop reach the limit!");
                    System.exit(0);
                }
            }
        }

        mutMapFileOutput = Util.outputMutMap(mutationProperties);
        Util.writeFile(mutMapFilename, mutMapFileOutput);
    }

    public double[] getFitnessArray() {
        double[] fitnessArray = new double[getSize()];
        for (int i = 0; i < getSize(); i++) {
            fitnessArray[i] = getIndividual(i).getFitness();
        }
        return fitnessArray;
    }

    public GroupReturn getFitnessPropertiesArray() {
        int popSize = getSize();
        double[] fitnessArray = new double[popSize];
        double[] meanDeleFitnessEffectArray = new double[popSize];
        double[] meanBeneFitnessEffectArray = new double[popSize];
        int[] nDeleMutArray = new int[popSize];
        int[] nBeneMutArray = new int[popSize];
        for (int i = 0; i < popSize; i++) {
            GroupReturn fitnessProperties = getIndividual(i).getFitnessProperties();
            fitnessArray[i] = fitnessProperties.getFitness();
            meanDeleFitnessEffectArray[i] = fitnessProperties.getMeanDeleFitnessEffect();
            meanBeneFitnessEffectArray[i] = fitnessProperties.getMeanBeneFitnessEffect();
            nDeleMutArray[i] = fitnessProperties.getNDeleteriousMutations();
            nBeneMutArray[i] = fitnessProperties.getNBeneficialMutations();
        }
        return new GroupReturn(fitnessArray, meanDeleFitnessEffectArray, meanBeneFitnessEffectArray, nDeleMutArray, nBeneMutArray);
    }

    public double[] getMutatorStrengthArray() {
        double[] mutatorStrengthArray = new double[getSize()];
        int i = 0;
        while (i < getSize()) {
            mutatorStrengthArray[i] = getIndividual(i).getMutatorStrength();
            i++;
        }
        return mutatorStrengthArray;
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

    private IndividualPair getRandomIndividualPair(double[] totals) {
        WeightedRandomGenerator wrg = new WeightedRandomGenerator();
        int indexA = 0;
        int indexB = 0;
        while (indexA == indexB || indexA == totals.length || indexB == totals.length) {
            indexA = wrg.nextInt(totals);
            indexB = wrg.nextInt(totals);
        }
        Individual individualA = getIndividual(indexA);
        Individual individualB = getIndividual(indexB);

        return new IndividualPair(individualA, individualB);
    }

    private double[] initTotals(double[] weights) {
        double[] totals = new double[weights.length];
        float runningTotal = 0;
        int i = 0;
        for (double weight : weights) {
            runningTotal += weight;
            totals[i++] = runningTotal;
        }
        return totals;
    }

    private double getRandomMutatorStrength() {
        double strength = 1;
        // Generate mutator locus, strength ranging from [2, FOUNDER_MUTATOR_STRENGTH_MAX]
        if (Rand.getFloat() < ModelParameters.getFloat("MUTATOR_RATIO")) {
            strength = Rand.getInt(ModelParameters.getInt("FOUNDER_MUTATOR_STRENGTH_MAX") - 1) + 2;
        }
        return strength;
    }

    private float getRandomRecombinationStrength() {
        float strength = 0;
        // Generate recombination locus (sexual), strength ranging from (0.0, 1.0]
        if (Rand.getFloat() < ModelParameters.getFloat("RECOMBINATION_RATIO")) {
            strength = Rand.getFloat();
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
