/**
 * @author Bingjun
 * Created by bingjun at 5/16/11 9:42 AM
*/

public class Individual {

    private Locus[] loci;
    private LociPattern lociPattern;
    private boolean alive;

    public Individual(LociPattern pattern) {
        this.lociPattern = pattern;
        loci = new Locus[lociPattern.getGenomeSize()];
        alive = true;
    }

    public void mutate() {
        lethalMutate();
        if (isAlive()) {
            deleteriousMutate();
            beneficialMutate();
        }
    }

    private void lethalMutate() {
        double mutationRate = ModelParameters.baseLethalMutationRate * getMutatorStrength();
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

    private void deleteriousMutate() {
        double mutationRate = ModelParameters.baseDeleteriousMutationRate * getMutatorStrength();
        if (Rand.getDouble() < mutationRate) {
            FitnessLocus locus = getRandomFitnessLocus();
            locus.addFitnessEffect(ModelParameters.defaultDeleteriousEffect);
        }
    }

    private void beneficialMutate() {
        double mutationRate = ModelParameters.baseBeneficialMutationRate * getMutatorStrength();
        if (Rand.getDouble() < mutationRate) {
            FitnessLocus locus = getRandomFitnessLocus();
            locus.addFitnessEffect(ModelParameters.defaultBeneficialEffect);
        }
    }

    private FitnessLocus getRandomFitnessLocus() {
        int position = lociPattern.getRandomFitnessPosition();
        return (FitnessLocus)getLocus(position);
    }

    public void setFitnessLocus(int position, float fitnessEffect) {
        FitnessLocus fitnessLocus = new FitnessLocus(fitnessEffect);
        loci[position] = fitnessLocus;
    }

    public void setFitnessLocus(int position) {
        setFitnessLocus(position, ModelParameters.baseFitnessEffect);
    }

    public void setMutatorLocus(int position, int strength) {
        MutatorLocus mutatorLocus = new MutatorLocus(strength);
        setLocus(position, mutatorLocus);
    }

    public void setRecombinationLocus(int position, float strength) {
        RecombinationLocus recombinationLocus = new RecombinationLocus(strength);
        setLocus(position, recombinationLocus);
    }

    public Locus getLocus(int position) {
        return loci[position];
    }

    public void setLocus(int position, Locus locus) {
        loci[position] = locus;
    }

    public float getFitness() {
        return 1.5f;
    }

    public int getGenomeSize() {
        return loci.length;
    }

    public float getMutatorStrength() {
        // TODO: multiple all mutator strength values
        int mutatorLocusPosition = lociPattern.getMutatorLociPositions()[0];
        return ((MutatorLocus)getLocus(mutatorLocusPosition)).getStrength(); // refactor
    }

    public float getRecombinationStrength() {
        // TODO: multiple all recombination strength values
        int recombinationLocusPosition = lociPattern.getRecombinationLociPositions()[0];
        return ((RecombinationLocus)getLocus(recombinationLocusPosition)).getStrength(); // refactor
    }

}
