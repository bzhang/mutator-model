/**
 * @author Bingjun Zhang
 */

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.ArrayList;

public class Individual implements Cloneable{

    private Locus[] loci;
    private LociPattern lociPattern;
    private boolean alive;
    private double mutatorStrength = 1.0d;
    private double fitness;
    private double transformedFitness;

    public Individual(LociPattern pattern) {
        lociPattern = pattern;
        loci = new Locus[lociPattern.getGenomeSize()];
        alive = true;
        fitness = 1.0d;
        transformedFitness = 1.0d;
    }

    public Individual(Individual individual) {
        this(individual.getLociPattern());
        this.fitness = individual.getNonTransformedFitness();
        this.transformedFitness = individual.getTransformedFitness();
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
        if (ModelParameters.getDouble("BASE_LETHAL_MUTATION_RATE") != 0d) {
            lethalMutate();
        }

        if (isAlive()) {
            double parentFitnessZScore = (transformedFitness - parentFitnessMean) / parentFitnessSD;
            deleteriousMutate(currentGeneration, mutationProperties, parentFitnessZScore, corFitnessMutatorStrength);
            beneficialMutate(currentGeneration, mutationProperties, parentFitnessZScore, corFitnessMutatorStrength);
            if (currentGeneration >= ModelParameters.getDouble("START_EVOLVING_GENERATION")) {
                mutatorMutate(currentGeneration);
                antimutatorMutate(currentGeneration);
            }
            if (currentGeneration >= ModelParameters.getInt("START_EVOLVE_RECOMBINATION_RATE")) {
                recombinationMutate(currentGeneration);
                antiRecombinationMutate(currentGeneration);
            }
        }


        if (fitness > 1.0) {
            transformedFitness = transformFitness(fitness);
        } else {
            transformedFitness = fitness;
        }
//        System.out.println("Fitness = " + fitness);
//        System.out.println("transformedFitness = " + transformedFitness);
        if (

                fitness <= 0) {
            die();
        }
    }

    public void mutate(int currentGeneration) {
        if (ModelParameters.getDouble("BASE_LETHAL_MUTATION_RATE") != 0d) {
            lethalMutate();
        }

        if (isAlive()) {
            deleteriousMutate(currentGeneration);
            beneficialMutate(currentGeneration);
            if (currentGeneration >= ModelParameters.getDouble("START_EVOLVING_GENERATION")) {
                mutatorMutate(currentGeneration);
                antimutatorMutate(currentGeneration);
            }
            if (currentGeneration >= ModelParameters.getInt("START_EVOLVE_RECOMBINATION_RATE")) {
                recombinationMutate(currentGeneration);
                antiRecombinationMutate(currentGeneration);
            }
        }

        if (fitness > Math.exp(1)) {
            transformedFitness = transformFitness(fitness);
        } else {
            transformedFitness = fitness;
        }

        if (fitness <= 0) {
            die();
        }
    }

    private double transformFitness(double fitness) {
        return Math.exp(Math.pow(Math.log(fitness), ModelParameters.getFloat("EPISTASIS")));
    }

    private void lethalMutate() {
        System.out.println("lethal mutate");
        double mutationRate = ModelParameters.getDouble("BASE_LETHAL_MUTATION_RATE") * mutatorStrength;
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
        double mutationRate = ModelParameters.getDouble("BASE_DELETERIOUS_MUTATION_RATE") * mutatorStrength;
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            double u = Rand.getFloat();
            double fitnessEffect = 1 - ((-ModelParameters.getFloat("DEFAULT_DELETERIOUS_EFFECT")) * Math.log(1 - u));
            while (fitnessEffect < 0) {
                System.out.println("Deleterious fitnessEffect re-sampled.");
                u = Rand.getFloat();
                fitnessEffect = 1 - ((-ModelParameters.getFloat("DEFAULT_DELETERIOUS_EFFECT")) * Math.log(1 - u));
            }
            updateMutationInformation(currentGeneration, mutationProperties, fitnessEffect, parentFitnessZScore, corFitnessMutatorStrength);
//            if (fitness != 1.0) {
//                System.out.println(fitness);
//                System.out.println(fitnessEffect);
//            }
            fitness *= fitnessEffect;
//            System.out.println(fitness);
        }
    }

    private void deleteriousMutate(int currentGeneration) {
        double mutationRate = ModelParameters.getDouble("BASE_DELETERIOUS_MUTATION_RATE") * mutatorStrength;
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            double u = Rand.getFloat();
            double fitnessEffect = 1 - ((-ModelParameters.getFloat("DEFAULT_DELETERIOUS_EFFECT")) * Math.log(1 - u));
            while (fitnessEffect < 0) {
                System.out.println("Deleterious fitnessEffect re-sampled.");
                u = Rand.getFloat();
                fitnessEffect = 1 - ((-ModelParameters.getFloat("DEFAULT_DELETERIOUS_EFFECT")) * Math.log(1 - u));
            }
            updateMutationInformation(currentGeneration, fitnessEffect);
            fitness *= fitnessEffect;
        }
    }

    private void beneficialMutate(int currentGeneration, ArrayList mutationProperties, double parentFitnessZScore, double corFitnessMutatorStrength) {
        double mutationRate = ModelParameters.getDouble("BASE_BENEFICIAL_MUTATION_RATE") * mutatorStrength;
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            double u = Rand.getFloat();
            double fitnessEffect = 1 + ((-ModelParameters.getFloat("DEFAULT_BENEFICIAL_EFFECT")) * Math.log(1 - u));
            while (fitnessEffect > 2) {
                System.out.println("Beneficial fitnessEffect re-sampled.");
                u = Rand.getFloat();
                fitnessEffect = 1 + ((-ModelParameters.getFloat("DEFAULT_BENEFICIAL_EFFECT")) * Math.log(1 - u));
            }
            updateMutationInformation(currentGeneration, mutationProperties, fitnessEffect, parentFitnessZScore, corFitnessMutatorStrength);
//            if (fitness != 1.0) {
//                System.out.println(fitness);
//                System.out.println(fitnessEffect);
//            }
            fitness *= fitnessEffect;
//            System.out.println(fitness);

        }
    }

    private void beneficialMutate(int currentGeneration) {
        double mutationRate = ModelParameters.getDouble("BASE_BENEFICIAL_MUTATION_RATE") * mutatorStrength;
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            double u = Rand.getFloat();
            double fitnessEffect = 1 + ((-ModelParameters.getFloat("DEFAULT_BENEFICIAL_EFFECT")) * Math.log(1 - u));
            while (fitnessEffect > 2) {
                System.out.println("Beneficial fitnessEffect re-sampled.");
                u = Rand.getFloat();
                fitnessEffect = 1 + ((-ModelParameters.getFloat("DEFAULT_BENEFICIAL_EFFECT")) * Math.log(1 - u));
            }
            updateMutationInformation(currentGeneration, fitnessEffect);
            fitness *= fitnessEffect;
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
        mutationProperties.add(mutatorStrength);
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
//        double mutationRate = ModelParameters.getDouble("INITIAL_MUTATOR_MUTATION_RATE");
        double mutationRate = ModelParameters.getDouble("EVOLVING_MUTATOR_MUTATION_RATE") * mutatorStrength;
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            MutatorLocus locus = getRandomMutatorLocus();
            locus.increaseStrength();
        }

    }

    private void antimutatorMutate(int currentGeneration) {
//        int startingEvolvingGeneration = ModelParameters.getInt("START_EVOLVING_GENERATION");
//        double mutationRate = ModelParameters.getDouble("INITIAL_ANTIMUTATOR_MUTATION_RATE");
        double mutationRate = ModelParameters.getDouble("EVOLVING_ANTIMUTATOR_MUTATION_RATE") * mutatorStrength;
//        Poisson poisson = new Poisson(mutationRate, Rand.getEngine());
//        int poissonObs = poisson.nextInt();
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            MutatorLocus locus = getRandomMutatorLocus();
            locus.decreaseStrength();
        }
    }

    private void recombinationMutate(int currentGeneration) {
        double mutationRate = ModelParameters.getDouble("EVOLVING_RECOMBINATION_MUTATION_RATE") * mutatorStrength;
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            RecombinationLocus locus = getRandomRecombinationLocus();
            locus.increaseStrength();
        }
    }

    private void antiRecombinationMutate(int currentGeneration) {
        double mutationRate = ModelParameters.getDouble("EVOLVING_ANTIRECOMBINATION_MUTATION_RATE") * mutatorStrength;
        int poissonObs = Util.getPoisson(mutationRate);
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            RecombinationLocus locus = getRandomRecombinationLocus();
            locus.decreaseStrength();
        }
    }

    private RecombinationLocus getRandomRecombinationLocus() {
        int position = lociPattern.getFirstRecombinationPosition();
        return (RecombinationLocus) getLocus(position);
    }

    // TODO: modify getRandomXXLocus to remove redundant codes; extract new methods
    private MutatorLocus getRandomMutatorLocus() {
        int position = lociPattern.getFirstMutatorPosition();
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

//    public double getTransformedFitness() {
//        double fitness = 1;
//        for (int i = 0; i < getGenomeSize(); i++) {
//            if (lociPattern.getLocusType(i) == LociPattern.LocusType.Fitness) {
//                FitnessLocus locus = (FitnessLocus)getLocus(i);
//                fitness *= locus.getFitnessEffect();
//            }
//        }
//        return fitness;
//    }

    public double getTransformedFitness() {
        return transformedFitness;
    }

    public GroupReturn getFitnessProperties() {
//        double fitness = getTransformedFitness();
        double meanDeleFitnessEffect = 0;
        double meanBeneFitnessEffect = 0;
        int nDeleteriousMutations = 0;
        int nBeneficialMutations = 0;
        for (int i = 0; i < getGenomeSize(); i++) {
            if (lociPattern.getLocusType(i) == LociPattern.LocusType.Fitness) {
                FitnessLocus locus = (FitnessLocus)getLocus(i);
//                fitness *= locus.getFitnessEffect();
                meanDeleFitnessEffect += locus.getDeleFitnessEffectSum();
                meanBeneFitnessEffect += locus.getBeneFitnessEffectSum();
                nDeleteriousMutations += locus.getNDeleteriousMutations();
                nBeneficialMutations += locus.getNBeneficialMutations();
            }
        }
        meanDeleFitnessEffect /= nDeleteriousMutations;
        meanBeneFitnessEffect /= nBeneficialMutations;

        return new GroupReturn(meanDeleFitnessEffect, meanBeneFitnessEffect, nDeleteriousMutations, nBeneficialMutations);
    }

    public void calculateMutatorStrength() {
        // TODO: multiple all mutator strength values
        // mutatorStrength is the product of all current mutator strength on all mutator loci
//        int mutatorLocusPosition = lociPattern.getMutatorLociPositions()[0];
        for (int i : lociPattern.getMutatorLociPositions()) {
//            System.out.println("each mutator strength " + i + " = " + ((MutatorLocus) getLocus(i)).getStrength());
            mutatorStrength *= ((MutatorLocus) getLocus(i)).getStrength();
        }
//        System.out.println("product of mutator strength" + mutatorStrength);
//        mutatorStrength = ((MutatorLocus) getLocus(mutatorLocusPosition)).getStrength(); // refactor
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

    public double getMutatorStrength() {
        return mutatorStrength;
    }

    public void setRecombinationStrength(float i) {
        // TODO: multiple all recombination strength values
        int recombinationLocusPosition = lociPattern.getRecombinationLociPositions()[0];
        ((RecombinationLocus) getLocus(recombinationLocusPosition)).setStrength(i);
    }

    public double getNonTransformedFitness() {
        return fitness;
    }
}
