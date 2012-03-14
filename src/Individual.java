import cern.jet.random.Poisson;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Bingjun
 * Created by bingjun at 5/16/11 9:42 AM
*/

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

    public void mutate(int currentGeneration, Map mutFitnessMap, ArrayList mutationProperties) {
        lethalMutate();
        if (isAlive()) {
            deleteriousMutate(currentGeneration, mutFitnessMap, mutationProperties);
            beneficialMutate(currentGeneration, mutFitnessMap, mutationProperties);
            mutatorMutate(currentGeneration);
        }
        if (getFitness(mutFitnessMap) <= 0) {
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

    private void deleteriousMutate(int currentGeneration, Map mutFitnessMap, ArrayList mutationProperties) {
        double mutationRate = ModelParameters.getDouble("BASE_DELETERIOUS_MUTATION_RATE") * getMutatorStrength();
        Poisson poisson = new Poisson(mutationRate, Rand.getEngine());
        int poissonObs = poisson.nextInt();

        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            long mutationID = System.nanoTime();
            LocusPosition locusPosition = getRandomFitnessLocus();
            FitnessLocus fitnessLocus = (FitnessLocus) locusPosition.getFitnessLocus();
            fitnessLocus.addFitnessEffect(mutationID);
            mutationProperties.add(mutationID);
            mutationProperties.add(ModelParameters.getFloat("DEFAULT_DELETERIOUS_EFFECT"));
            mutationProperties.add(getMutatorStrength());
            mutationProperties.add(currentGeneration);
            mutationProperties.add(locusPosition.getPosition());
            mutFitnessMap.put(mutationID, ModelParameters.getFloat("DEFAULT_DELETERIOUS_EFFECT"));
        }
    }

    private void beneficialMutate(int currentGeneration, Map mutFitnessMap, ArrayList mutationProperties) {
        double mutationRate = ModelParameters.getDouble("BASE_BENEFICIAL_MUTATION_RATE") * getMutatorStrength();
        Poisson poisson = new Poisson(mutationRate, Rand.getEngine());
        int poissonObs = poisson.nextInt();
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            long mutationID = System.nanoTime();
            LocusPosition locusPosition = getRandomFitnessLocus();
            FitnessLocus fitnessLocus = (FitnessLocus) locusPosition.getFitnessLocus();
            fitnessLocus.addFitnessEffect(mutationID);
            mutationProperties.add(mutationID);
            mutationProperties.add(ModelParameters.getFloat("DEFAULT_BENEFICIAL_EFFECT"));
            mutationProperties.add(getMutatorStrength());
            mutationProperties.add(currentGeneration);
            mutationProperties.add(locusPosition.getPosition());
            mutFitnessMap.put(mutationID, ModelParameters.getFloat("DEFAULT_BENEFICIAL_EFFECT"));
        }
    }

    private void mutatorMutate(int currentGeneration) {
        int startingEvolvingGeneration = ModelParameters.getInt("START_EVOLVING_GENERATION");
        double mutationRate = ModelParameters.getDouble("INITIAL_MUTATOR_MUTATION_RATE");

//      startingEvolvingGeneration ranges [1,21];
//      1 means evolving mutators from beginning; 21 means fixed mu all the time.
        if (currentGeneration >= startingEvolvingGeneration) {
            mutationRate = ModelParameters.getDouble("EVOLVING_MUTATOR_MUTATION_RATE") * getMutatorStrength();
        }

        Poisson poisson = new Poisson(mutationRate, Rand.getEngine());
        int poissonObs = poisson.nextInt();
        for (int nMutation = 0; nMutation < poissonObs; nMutation++) {
            MutatorLocus locus = getRandomMutatorLocus();
            if (Rand.getDouble() < ModelParameters.getDouble("PROBABILITY_TO_MUTATOR")) {
                locus.increaseStrength();
//            } else if (locus.getStrength() > ModelParameters.MUTATOR_MUTATION_EFFECT) {
                    // to ensure the strength is positive
            } else {
                locus.decreaseStrength();
            }
        }


    }

    // TODO: modify getRandomXXLocus to remove redundant codes; extract new methods
    private MutatorLocus getRandomMutatorLocus() {
        int position = lociPattern.getRandomMutatorPosition();
        return (MutatorLocus) getLocus(position);
    }

    private LocusPosition getRandomFitnessLocus() {
        int position = lociPattern.getRandomFitnessPosition();
        return new LocusPosition((FitnessLocus) getLocus(position), position);
    }

    public void setFitnessLocus(int position) {
        FitnessLocus fitnessLocus = new FitnessLocus();
        loci[position] = fitnessLocus;
    }

//    public void setFitnessLocus(int position) {
//        setFitnessLocus(position, ModelParameters.getFloat("BASE_FITNESS_EFFECT"));
//    }

    public void setMutatorLocus(int position, int strength) {
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

    public float getFitness() {
        float fitness = 1;
        for (int i = 0; i < getGenomeSize(); i++) {
            if (lociPattern.getLocusType(i) == LociPattern.LocusType.Fitness) {
                FitnessLocus locus = (FitnessLocus)getLocus(i);
                fitness *= locus.getFitnessEffect();
            }
        }
        return fitness;
    }

    public int getMutatorStrength() {
        // TODO: multiple all mutator strength values
        int mutatorLocusPosition = lociPattern.getMutatorLociPositions()[0];
        return ((MutatorLocus) getLocus(mutatorLocusPosition)).getStrength(); // refactor
    }

    public float getRecombinationStrength() {
        // TODO: multiple all recombination strength values
        int recombinationLocusPosition = lociPattern.getRecombinationLociPositions()[0];
        return ((RecombinationLocus) getLocus(recombinationLocusPosition)).getStrength(); // refactor
    }

    public int getNDeleMut(Map mutFitnessMap) {
        int nDeleMut = 0;
        float fitnessEffect;

        for (int i = 0; i < getGenomeSize(); i++) {
            if (lociPattern.getLocusType(i) == LociPattern.LocusType.Fitness) {
                FitnessLocus locus = (FitnessLocus) getLocus(i);
                ArrayList<Long> mutationIDs = locus.getFitnessEffectsArray();
                for (long mutationID : mutationIDs) {
                    fitnessEffect = (Float) mutFitnessMap.get(mutationID);
                    if (fitnessEffect < 1) {
                        nDeleMut++;
                    }
                }
            }
        }
        return nDeleMut;
    }

    public int getNBeneMut(Map mutFitnessMap) {
        int nBeneMut = 0;
        float fitnessEffect;

        for (int i = 0; i < getGenomeSize(); i++) {
            if (lociPattern.getLocusType(i) == LociPattern.LocusType.Fitness) {
                FitnessLocus locus = (FitnessLocus) getLocus(i);
                ArrayList<Long> mutationIDs = locus.getFitnessEffectsArray();
                for (long mutationID : mutationIDs) {
                    fitnessEffect = (Float) mutFitnessMap.get(mutationID);
                    if (fitnessEffect > 1) {
                        nBeneMut++;
                    }
                }
            }
        }
        return nBeneMut;
    }
}
