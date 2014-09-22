import java.util.ArrayList;

/**
 * @author Bingjun Zhang
 */

public class IndividualPair {

    private Individual individualA, individualB;

    public IndividualPair(Individual individualA, Individual individualB) {
        this.individualA = individualA;
        this.individualB = individualB;
//        this.individualA.calculateMutatorStrength();
//        this.individualB.calculateMutatorStrength();
    }

    public Individual getIndividualA() {
//        individualA.calculateMutatorStrength();
        return individualA;
    }

    public Individual getIndividualB() {
//        individualB.calculateMutatorStrength();
        return individualB;
    }

    public void mutate(int currentGeneration, ArrayList mutationProperties, double parentFitnessMean, double parentFitnessSD, double corFitnessMutatorStrength) {
        getIndividualA().calculateMutatorStrength();
        getIndividualB().calculateMutatorStrength();
        getIndividualA().mutate(currentGeneration, mutationProperties, parentFitnessMean, parentFitnessSD, corFitnessMutatorStrength);
        getIndividualB().mutate(currentGeneration, mutationProperties, parentFitnessMean, parentFitnessSD, corFitnessMutatorStrength);
    }

    public void mutate(int currentGeneration) {
        getIndividualA().calculateMutatorStrength();
        getIndividualB().calculateMutatorStrength();
//        System.out.println("before mutate = " + getIndividualA().getMutatorStrength());
//        System.out.println("before mutate = " + getIndividualB().getMutatorStrength());
        getIndividualA().mutate(currentGeneration);
        getIndividualB().mutate(currentGeneration);
//        getIndividualA().calculateMutatorStrength();
//        getIndividualB().calculateMutatorStrength();
//        System.out.println("after mutate = " + getIndividualA().getMutatorStrength());
//        System.out.println("after mutate = " + getIndividualB().getMutatorStrength());
    }

    public IndividualPair reproduce() {

        Individual offspringA = new Individual(getIndividualA());
        Individual offspringB = new Individual(getIndividualB());
        IndividualPair offspringPair = new IndividualPair(offspringA, offspringB);
//        System.out.println("offspringA fitness = " + offspringA.getTransformedFitness());
//        System.out.println("offspringB fitness = " + offspringB.getTransformedFitness());

        if (ModelParameters.getFloat("RECOMBINATION_RATIO") != 0.0) {
            float recombinationStrengthA = offspringA.getRecombinationStrength();
            float recombinationStrengthB = offspringB.getRecombinationStrength();
            float recombinationProbability = (recombinationStrengthA + recombinationStrengthB) / 2;

            if (Rand.getFloat() < recombinationProbability) {
                offspringPair.recombine();
                ModelParameters.addNRecombination();
            }
        }

        return offspringPair;
    }

    private void recombine() {
        int genomeSize = getIndividualA().getGenomeSize();
        int position = Rand.getInt(genomeSize);

        for (int i = position; i < genomeSize; i++) {
            Locus locusA = getIndividualA().getLocus(i);
            Locus locusB = getIndividualB().getLocus(i);
            getIndividualA().setLocus(i, locusB);
            getIndividualB().setLocus(i, locusA);
        }
        getIndividualA();
        getIndividualB();
        getIndividualB();
    }
}
