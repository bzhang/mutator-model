import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bingjun Zhang
 */


public class MetaPopulation {
//    private ArrayList<ArrayList<Individual>> individuals;
    private ArrayList<IndividualInSpace> individuals;
    private LociPattern lociPattern;
    private int popSize = ModelParameters.getInt("POPULATION_SIZE");

    public MetaPopulation() {
        // Create the founder population
        lociPattern = new LociPattern(ModelParameters.getInt("N_FITNESS_LOCI"),
                ModelParameters.getInt("N_MUTATOR_LOCI"), ModelParameters.getInt("N_RECOMBINATION_LOCI"));
        individuals = new ArrayList<IndividualInSpace>();
        int radius = 0;
        initMetaPopulation:
        while (true) {
            for (int y = -radius; y <= radius; y++) {
                individuals.add(new IndividualInSpace(createIndividual(), -radius, y));
                if (individuals.size() >= popSize) break initMetaPopulation;
                individuals.add(new IndividualInSpace(createIndividual(), radius, y));
                if (individuals.size() >= popSize) break initMetaPopulation;
            }
            for (int x = -radius + 1; x < radius; x++) {
                individuals.add(new IndividualInSpace(createIndividual(), -x, -radius));
                if (individuals.size() >= popSize) break initMetaPopulation;
                individuals.add(new IndividualInSpace(createIndividual(), x, radius));
                if (individuals.size() >= popSize) break initMetaPopulation;
            }
            radius++;
        }
    }

    private Individual createIndividual() {
        Individual individual = new Individual(lociPattern);
        for (int location = 0; location < ModelParameters.getGenomeSize(); location++) {
            if (lociPattern.getLocusType(location) == LociPattern.LocusType.Fitness) {
                individual.setFitnessLocus(location);
            } else if (lociPattern.getLocusType(location) == LociPattern.LocusType.Mutator) {
                individual.setMutatorLocus(location, getRandomMutatorStrength());
            } else {
                individual.setRecombinationLocus(location, getRandomRecombinationStrength());
            }
        }
        return individual;
    }

    public MetaPopulation(MetaPopulation metaParent, int currentGeneration) {
        double[] parentFitnessArray = metaParent.getFitnessArray();
        double[] totals = Util.initTotals(parentFitnessArray);
//        List<List<Integer>> directions = Util.getDirections();
        lociPattern = metaParent.lociPattern;
        individuals = new ArrayList<IndividualInSpace>();
//        double[][] parentFitnessMatrix = metaParent.getFitnessMatrix();
//        double[] totals = initTotals(parentFitnessMatrix);
        if (totals[totals.length - 1] < 1e-10) {
            System.out.println("Population is extinct at generation " + currentGeneration + "!");
            System.exit(0);
        }

        if (ModelParameters.getBoolean("INVASION_EXPERIMENT")) {
            while (getSize() < popSize) {
                IndividualInSpace parentIndividualInSpace = getRandomIndividual(totals);
                Individual parentIndividual = parentIndividualInSpace.getIndividual();
                float parentX = parentIndividualInSpace.getX();
                float parentY = parentIndividualInSpace.getY();
                if (currentGeneration <= ModelParameters.getInt("START_CREATING_ASEXUALS")) {
                    // sexually reproduce
                    IndividualInSpace mateIndividualInSpace = getMateIndividual(parentX, parentY);
                    float mateX = mateIndividualInSpace.getX();
                    float mateY = mateIndividualInSpace.getY();
                    IndividualPair parentPair = new IndividualPair(parentIndividual, mateIndividualInSpace.getIndividual());
                    IndividualPair offspringPair = parentPair.reproduce();
                    disperse(offspringPair);
                } else {
                    if (parentIndividual.getRecombinationStrength() > 0) {
                        // convert to asexuals
                        if (Rand.getFloat() < ModelParameters.getFloat("PROBABILITY_TO_ASEXUAL")) {
                            parentIndividual.setRecombinationStrength(0);
                            // asexually reproduction
                            for (int i = 0; i < 4; i++) {
                                Individual offspring = new Individual(parentIndividual);
                                disperse(offspring);
                            }
                        } else {
                            // sexually reproduce
                            Individual mateIndividual = getMateIndividual(directions, parentRow, parentColumn);
                            IndividualPair parentPair = new IndividualPair(parentIndividual, mateIndividual);
                            IndividualPair offspringPair = parentPair.reproduce();
                            disperse(offspringPair);
//                    for (int neighborID = 0; neighborID < 4; neighborID++) {
//                        mateIndividual = getMateIndividual(neighborID);
//                        if (mateIndividual.getRecombinationStrength() > 0) {
//                            break;
//                        }
//                    }
                        }
                    } else {
                        // asexually reproduce
                        for (int i = 0; i < 4; i++) {
                            Individual offspring = new Individual(parentIndividual);
                            disperse(offspring);
                        }
                    }
                }
            }

        } else {
            while (getSize() < popSize) {
                GroupReturn parentIndividualAndIndex = getRandomIndividual(totals);
                Individual parentIndividual = parentIndividualAndIndex.getIndividual();
                int parentRow = parentIndividualAndIndex.getRow();
                int parentColumn = parentIndividualAndIndex.getColumn();
            }

        }
    }

    private void disperse(IndividualPair offspringPair) {

    }

    private IndividualInSpace getMateIndividual(float x, float y) {
        float matingDistance = ModelParameters.getFloat("MATING_DISTANCE");
//        float newX = x + Rand.getFloat() * matingDistance * 2 - matingDistance;
//        float newY = y + Rand.getFloat() * matingDistance * 2 - matingDistance;
        for (int i = 0; i < individuals.size(); i++) {
            IndividualInSpace individualInSpace = individuals.get(i);
            float newX = individualInSpace.getX();
            float newY = individualInSpace.getY();
            if (newX >= (x - matingDistance) && newX <= (x + matingDistance)) {
                if (newY >= (y - matingDistance) && newY <= (y + matingDistance)) {
                    return individualInSpace;
                }
            }
        }
        return null;
    }

    private int getSize() {
        return individuals.size();
    }

    private double[] getFitnessArray() {
        double[] fitnessArray = new double[getSize()];
        for (int i = 0; i < getSize(); i++) {
            fitnessArray[i] = getIndividualInSpace(i).getIndividual().getFitness();
        }
        return fitnessArray;
    }

    private IndividualInSpace getIndividualInSpace(int i) {
        return individuals.get(i);
    }

    private IndividualInSpace getRandomIndividual(double[] totals) {
        int index = 0;
        while (index == totals.length) {
            index = WeightedRandomGenerator.nextInt(totals);
        }
        return getIndividualInSpace(index) ;
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
