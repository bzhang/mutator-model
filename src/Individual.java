/**
 * @author Bingjun Zhang
 */

import java.util.ArrayList;

public class Individual implements Cloneable{

    private Locus[] loci;
    private LociPattern lociPattern;
    private boolean alive;

    public Individual(LociPattern pattern) {
        lociPattern = pattern;
        loci = new Locus[lociPattern.getGenomeSize()];
        alive = true;
    }

    public Individual(Individual individual) {
        this(individual.getLociPattern());
        for (int i = 0; i < individual.getGenomeSize(); i++) {
            Object clonedLocus = null;
            try {
                clonedLocus = individual.getLocus(i).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (getLociPattern().getLocusType(i) == LociPattern.LocusType.Fitness) {
                loci[i] = (FitnessLocus) clonedLocus;
            } else if (getLociPattern().getLocusType(i) == LociPattern.LocusType.Mutator) {
                loci[i] = (MutatorLocus) clonedLocus;
            } else if (getLociPattern().getLocusType(i) == LociPattern.LocusType.Recombination) {
                loci[i] = (RecombinationLocus) clonedLocus;
            }
        }
    }

    public void mutate(int currentGeneration, ArrayList mutationProperties, double parentFitnessMean, double parentFitnessSD, double corFitnessMutatorStrength) {
        if (ModelParameters.getDouble("BASE_LETHAL_MUTATION_RATE") != 0) {
            lethalMutate();
        }

        if (isAlive()) {
            double parentFitnessZScore = (getFitness() - parentFitnessMean) / parentFitnessSD;
            deleteriousMutate(currentGeneration, mutationProperties, parentFitnessZScore, corFitnessMutatorStrength);
            beneficialMutate(currentGeneration, mutationProperties, parentFitnessZScore, corFitnessMutatorStrength);
            mutatorMutate(currentGeneration);
            antimutatorMutate(currentGeneration);
        }

        if (getFitness() <= 0) {
            die();
        }
    }

    public void mutate(int currentGeneration) {
        lethalMutate();
        if (isAlive()) {
            deleteriousMutate(currentGeneration);
            beneficialMutate(currentGeneration);
            mutatorMutate(currentGeneration);
            antimutatorMutate(currentGeneration);
        }

        if (getFitness() <= 0) {
            die();
        }
    }

    private void lethalMutate() {
        double mutationRate = ModelParameters.getDouble("BASE_LETHAL_MUTATION_RATE") * getMutatorStrength();
        if (Rand.getDouble() < mutationRate) {
            die();
        }
    }

    public void die() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    private void deleteriousMutate(int currentGeneration, ArrayList mutationProperties, double parentFitnessZScore, double corFitnessMutatorStrength) {
        double mutationRate = ModelParameters.getDouble("BASE_DELETERIOUS_MUTATION_RATE") * getMutatorStrength();
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            double u = Rand.getFloat();
            double fitnessEffect = 1 - ((-ModelParameters.getFloat("DEFAULT_DELETERIOUS_EFFECT")) * Math.log(1 - u));
            updateMutationInformation(currentGeneration, mutationProperties, fitnessEffect, parentFitnessZScore, corFitnessMutatorStrength);
        }
    }

    private void deleteriousMutate(int currentGeneration) {
        double mutationRate = ModelParameters.getDouble("BASE_DELETERIOUS_MUTATION_RATE") * getMutatorStrength();
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            double u = Rand.getFloat();
            double fitnessEffect = 1 - ((-ModelParameters.getFloat("DEFAULT_DELETERIOUS_EFFECT")) * Math.log(1 - u));
            updateMutationInformation(currentGeneration, fitnessEffect);
        }
    }

    private void beneficialMutate(int currentGeneration, ArrayList mutationProperties, double parentFitnessZScore, double corFitnessMutatorStrength) {
        double mutationRate = ModelParameters.getDouble("BASE_BENEFICIAL_MUTATION_RATE") * getMutatorStrength();
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            double u = Rand.getFloat();
            double fitnessEffect = 1 + ((-ModelParameters.getFloat("DEFAULT_BENEFICIAL_EFFECT")) * Math.log(1 - u));
            updateMutationInformation(currentGeneration, mutationProperties, fitnessEffect, parentFitnessZScore, corFitnessMutatorStrength);
        }
    }

    private void beneficialMutate(int currentGeneration) {
        double mutationRate = ModelParameters.getDouble("BASE_BENEFICIAL_MUTATION_RATE") * getMutatorStrength();
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            double u = Rand.getFloat();
            double fitnessEffect = 1 + ((-ModelParameters.getFloat("DEFAULT_BENEFICIAL_EFFECT")) * Math.log(1 - u));
            updateMutationInformation(currentGeneration, fitnessEffect);
        }
    }

    private void updateMutationInformation(int currentGeneration, ArrayList mutationProperties, double fitnessEffect, double parentFitnessZScore, double corFitnessMutatorStrength) {
        long mutationID = ModelParameters.getMutationID();
        GroupReturn locusPosition = getRandomFitnessLocus();
        FitnessLocus fitnessLocus = (FitnessLocus) locusPosition.getFitnessLocus();
        fitnessLocus.addMutationID(mutationID);
        fitnessLocus.updateFitnessEffect(fitnessEffect);
        mutationProperties.add(mutationID);
        mutationProperties.add(fitnessEffect);
        mutationProperties.add(getMutatorStrength());
        mutationProperties.add(currentGeneration);
        mutationProperties.add(locusPosition.getPosition());
        mutationProperties.add(parentFitnessZScore);
        mutationProperties.add(corFitnessMutatorStrength);
    }

    private void updateMutationInformation(int currentGeneration, double fitnessEffect) {
        long mutationID = ModelParameters.getMutationID();
        GroupReturn locusPosition = getRandomFitnessLocus();
        FitnessLocus fitnessLocus = (FitnessLocus) locusPosition.getFitnessLocus();
        fitnessLocus.addMutationID(mutationID);
        fitnessLocus.updateFitnessEffect(fitnessEffect);
    }

    private void mutatorMutate(int currentGeneration) {
        double mutationRate = ModelParameters.getDouble("INITIAL_MUTATOR_MUTATION_RATE");
        if (currentGeneration >= ModelParameters.getInt("START_EVOLVING_GENERATION")) {
            mutationRate = ModelParameters.getDouble("EVOLVING_MUTATOR_MUTATION_RATE") * getMutatorStrength();
            int poissonObs = Util.getPoisson(mutationRate);
            for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
                MutatorLocus locus = getRandomMutatorLocus();
                locus.increaseStrength();
            }
        }
    }

    private void antimutatorMutate(int currentGeneration) {
        int startingEvolvingGeneration = ModelParameters.getInt("START_EVOLVING_GENERATION");
        double mutationRate = ModelParameters.getDouble("INITIAL_ANTIMUTATOR_MUTATION_RATE");

        if (currentGeneration >= startingEvolvingGeneration) {
            mutationRate = ModelParameters.getDouble("EVOLVING_ANTIMUTATOR_MUTATION_RATE") * getMutatorStrength();
//        Poisson poisson = new Poisson(mutationRate, Rand.getEngine());
//        int poissonObs = poisson.nextInt();
            int poissonObs = Util.getPoisson(mutationRate);
            for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
                MutatorLocus locus = getRandomMutatorLocus();
                locus.decreaseStrength();
            }
        }
    }


    // TODO: modify getRandomXXLocus to remove redundant codes; extract new methods
    private MutatorLocus getRandomMutatorLocus() {
        int position = lociPattern.getRandomMutatorPosition();
        return (MutatorLocus) getLocus(position);
    }

    private GroupReturn getRandomFitnessLocus() {
        int position = lociPattern.getRandomFitnessPosition();
        return new GroupReturn((FitnessLocus) getLocus(position), position);
    }

    public void setFitnessLocus(int position) {
        FitnessLocus fitnessLocus = new FitnessLocus();
        loci[position] = fitnessLocus;
    }

//    public void setFitnessLocus(int position) {

    public void setMutatorLocus(int position, double strength) {
        MutatorLocus mutatorLocus = new MutatorLocus(strength);
        setLocus(position, mutatorLocus);
    }

    public void setRecombinationLocus(int position, float strength) {
        RecombinationLocus recombinationLocus = new RecombinationLocus(strength);
        setLocus(position, recombinationLocus);
    }

    public LociPattern getLociPattern() {
        return lociPattern;
    }

    public Locus getLocus(int position) {
        return loci[position];
    }

    public void setLocus(int position, Locus locus) {
        loci[position] = locus;
    }

    public int getGenomeSize() {
        return loci.length;
    }

//        int startingEvolvingGeneration = ModelParameters.getInt("START_EVOLVING_GENERATION");

    public double getFitness() {
        double fitness = 1;
        for (int i = 0; i < getGenomeSize(); i++) {
            if (lociPattern.getLocusType(i) == LociPattern.LocusType.Fitness) {
                FitnessLocus locus = (FitnessLocus)getLocus(i);
                fitness *= locus.getFitnessEffect();
            }
        }
        return fitness;
    }

    public GroupReturn getFitnessProperties() {
        double fitness = 1;
        double meanDeleFitnessEffect = 0;
        double meanBeneFitnessEffect = 0;
        int nDeleteriousMutations = 0;
        int nBeneficialMutations = 0;
        for (int i = 0; i < getGenomeSize(); i++) {
            if (lociPattern.getLocusType(i) == LociPattern.LocusType.Fitness) {
                FitnessLocus locus = (FitnessLocus)getLocus(i);
                fitness *= locus.getFitnessEffect();
                meanDeleFitnessEffect += locus.getDeleFitnessEffectSum();
                meanBeneFitnessEffect += locus.getBeneFitnessEffectSum();
                nDeleteriousMutations += locus.getNDeleteriousMutations();
                nBeneficialMutations += locus.getNBeneficialMutations();
            }
        }
        meanDeleFitnessEffect /= nDeleteriousMutations;
        meanBeneFitnessEffect /= nBeneficialMutations;

        return new GroupReturn(fitness, meanDeleFitnessEffect, meanBeneFitnessEffect, nDeleteriousMutations, nBeneficialMutations);
    }

    public double getMutatorStrength() {
        // TODO: multiple all mutator strength values
        int mutatorLocusPosition = lociPattern.getMutatorLociPositions()[0];
        return ((MutatorLocus) getLocus(mutatorLocusPosition)).getStrength(); // refactor
    }

    public float getRecombinationStrength() {
        // TODO: multiple all recombination strength values
        int recombinationLocusPosition = lociPattern.getRecombinationLociPositions()[0];
        return ((RecombinationLocus) getLocus(recombinationLocusPosition)).getStrength(); // refactor
    }

    public double mutate(int nDeleMutation, int nBeneMutation) {
        double fitnessEffect = 1;
        for (int i = 0; i < nDeleMutation; i++) {
            double u = Rand.getFloat();
            fitnessEffect *= 1 - ((-ModelParameters.getFloat("DEFAULT_DELETERIOUS_EFFECT")) * Math.log(1 - u));
        }
        for (int j = 0; j < nBeneMutation; j++) {
            double u = Rand.getFloat();
            fitnessEffect *= 1 + ((-ModelParameters.getFloat("DEFAULT_BENEFICIAL_EFFECT")) * Math.log(1 - u));
        }
        return (fitnessEffect);
    }

    public void mutateMutationRate(int nMutatorMutation, int nAntiMutMutation) {
//        MutatorLocus locus = getRandomMutatorLocus();
        int position = lociPattern.getRandomMutatorPosition();
        MutatorLocus locus = (MutatorLocus) loci[position];
        for (int i = 0; i < nMutatorMutation; i++) {
            locus.increaseStrength();
        }
        for (int j = 0; j < nAntiMutMutation; j++) {
            locus.decreaseStrength();
        }
    }
}
