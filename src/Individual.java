/**
 * @author Bingjun
 * Created by bingjun at 5/16/11 9:42 AM
*/

public class Individual {

    private Locus[] loci;
    private LociPattern lociPattern;

    public Individual(LociPattern pattern) {
        this.lociPattern = pattern;
        loci = new Locus[lociPattern.getGenomeSize()];
    }

    public void setFitnessLocus(int position, float fitnessEffect) {
        FitnessLocus fitnessLocus = new FitnessLocus(fitnessEffect);
        loci[position] = fitnessLocus;
    }

    public void setFitnessLocus(int position) {
        setFitnessLocus(position, 0);
    }

    public void setMutatorLocus(int position, int strength) {
        MutatorLocus mutatorLocus = new MutatorLocus(strength);
        loci[position] = mutatorLocus;
    }

    public void setRecombinationLocus(int position, float strength) {
        RecombinationLocus recombinationLocus = new RecombinationLocus(strength);
        loci[position] = recombinationLocus;
    }

    public float getFitness() {
        return 1.5f;
    }

    public int getGenomeSize() {
        return loci.length;
    }

    public float getRecombinationStrength() {
        int recombinationLocusPosition = lociPattern.getRecombinationLociPositions()[0];
        return ((RecombinationLocus)loci[recombinationLocusPosition]).getStrength(); // refactor
    }

}
