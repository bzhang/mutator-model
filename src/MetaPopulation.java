/**
 * @author Bingjun Zhang
 */


public class MetaPopulation {
//    private ArrayList<ArrayList<Individual>> individuals;
    private Individual[][] individuals;
    private LociPattern lociPattern;
    private int popSize = ModelParameters.getInt("POPULATION_SIZE");
    private int side = (int) Math.sqrt(popSize);

    public MetaPopulation() {
        // Create the founder population
        lociPattern = new LociPattern(ModelParameters.getInt("N_FITNESS_LOCI"),
                ModelParameters.getInt("N_MUTATOR_LOCI"), ModelParameters.getInt("N_RECOMBINATION_LOCI"));
        individuals = new Individual[side][side];

        for (int row = 0; row < side; row++) {
            for (int column = 0; column < side; column++) {
                Individual individual = new Individual(lociPattern);
                individuals[row][column] = individual;
                for (int location = 0; location < ModelParameters.getGenomeSize(); location++) {
                    if (lociPattern.getLocusType(location) == LociPattern.LocusType.Fitness) {
                        individual.setFitnessLocus(location);
                    } else if (lociPattern.getLocusType(location) == LociPattern.LocusType.Mutator) {
                        individual.setMutatorLocus(location, getRandomMutatorStrength());
                    } else {
                        individual.setRecombinationLocus(location, getRandomRecombinationStrength());
                    }
                }
            }
        }
    }

    public MetaPopulation(MetaPopulation metaParent, int currentGeneration) {
        lociPattern = metaParent.lociPattern;
        individuals = new Individual[side][side];
        double[][] parentFitnessMatrix = metaParent.getFitnessMatrix();
        double[] totals = initTotals(parentFitnessMatrix);
        if (totals[totals.length - 1] < 1e-10) {
            System.out.println("Population is extinct at generation " + currentGeneration + "!");
            System.exit(0);
        }

        //TODO: evolve until mutation-selection equilibrium, 500 generations
        //TODO: create asexual individuals after that

        while (getSize() < popSize) {
            Individual parentIndividual = getRandomIndividual();
            if (parentIndividual.getRecombinationStrength() > 0) {
                Individual mateIndividual;
                for (int neighborID = 0; neighborID < 4; neighborID++) {
                    mateIndividual = getMateIndividual(neighborID);
                    if (mateIndividual.getRecombinationStrength() > 0) {
                        break;
                    }
                }
                // sexually reproduce
            } else {
                // asexually reproduce
            }
        }
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
        // Generate recombination locus (sexual)
        // Sexual to asexual ratio in founder population = RECOMBINATION_RATIO
        // Initial Recombination strength = RECOMBINATION_RATE
        if (ModelParameters.getFloat("RECOMBINATION_RATIO") == 0) {
            return strength;
        } else if (ModelParameters.getFloat("RECOMBINATION_RATIO") == 1) {
            strength = ModelParameters.getFloat("RECOMBINATION_RATE");
        } else if (Rand.getFloat() < ModelParameters.getFloat("RECOMBINATION_RATIO")) {
//            strength = Rand.getFloat();
            strength = ModelParameters.getFloat("RECOMBINATION_RATE");
        }
        return strength;
    }
}
