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
    private int side = (int) Math.sqrt(popSize);

    public MetaPopulation() {
        // Create the founder population
        lociPattern = new LociPattern(ModelParameters.getInt("N_FITNESS_LOCI"),
                ModelParameters.getInt("N_MUTATOR_LOCI"), ModelParameters.getInt("N_RECOMBINATION_LOCI"));
        individuals = new ArrayList<IndividualInSpace>();
        int radius = 0;
        while (true) {
            for (int y = -radius; y <= radius; y++) {
                individuals.add(new IndividualInSpace(createIndividual(), -radius, y));
                if (individuals.size() >= popSize) break;
                individuals.add(new IndividualInSpace(createIndividual(), radius, y));
                if (individuals.size() >= popSize) break;
            }
            for (int x = -radius + 1; x < radius; x++) {
                individuals.add(new IndividualInSpace(createIndividual(), -x, -radius));
                if (individuals.size() >= popSize) break;
                individuals.add(new IndividualInSpace(createIndividual(), x, radius));
                if (individuals.size() >= popSize) break;
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
        List<List<Integer>> directions = Util.getDirections();
        lociPattern = metaParent.lociPattern;
        individuals = new ArrayList<List<List<Individual>>>();
//        double[][] parentFitnessMatrix = metaParent.getFitnessMatrix();
//        double[] totals = initTotals(parentFitnessMatrix);
        if (totals[totals.length - 1] < 1e-10) {
            System.out.println("Population is extinct at generation " + currentGeneration + "!");
            System.exit(0);
        }

        if (ModelParameters.getBoolean("INVASION_EXPERIMENT")) {
            while (getSize() < popSize) {
                GroupReturn parentIndividualAndIndex = getRandomIndividual(totals);
                Individual parentIndividual = parentIndividualAndIndex.getIndividual();
                int parentRow = parentIndividualAndIndex.getRow();
                int parentColumn = parentIndividualAndIndex.getColumn();
                if (currentGeneration <= ModelParameters.getInt("START_CREATING_ASEXUALS")) {
                    // sexually reproduce
                    Individual mateIndividual = getMateIndividual(directions, parentRow, parentColumn);
                    IndividualPair parentPair = new IndividualPair(parentIndividual, mateIndividual);
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

    private Individual getMateIndividual(List<List<Integer>> directions, int row, int column) {
        int i = Rand.getInt(directions.size());
        int newRow = row + directions.get(i).get(0);
        int newColumn = column + directions.get(i).get(1);
        int loopCount = 0;
        while (newRow < 0 || newRow > side - 1 || newColumn < 0 || newColumn > side - 1) {
            if (loopCount > (int) Math.pow(directions.size() + 1, 2)) {
                return null;
            }
            loopCount++;
            i = Rand.getInt(directions.size());
            newRow = row + directions.get(i).get(0);
            newColumn = column + directions.get(i).get(1);
        }
        return getIndividual(newRow, newColumn);
    }

    private int getSize() {
        int size = 0;
        for (List<List<Individual>> individualRow : individuals) {
            for (List<Individual> individualCell : individualRow) {
                size += individualCell.size();
            }
        }
        return size;
    }

    private double[] getFitnessArray() {
        double[] fitnessArray = new double[popSize];
        int i = 0;
        for (int row = 0; row < side; row++) {
            for (int column = 0; column < side; column++) {
                fitnessArray[i] = getIndividual(row, column).getFitness();
                i++;
            }
        }
        return fitnessArray;
    }

    //TODO: change to list of list of list
    private Individual getIndividual(int row, int column) {
        return individuals[row][column];
    }

    //TODO: change to get id of each random individual (row, column, IDInCell)
    private GroupReturn getRandomIndividual(double[] totals) {
        int index = 0;
        while (index == totals.length) {
            index = WeightedRandomGenerator.nextInt(totals);
        }
        int row = index / side;
        int column = index % side;
        return new GroupReturn(getIndividual(row, column), row, column) ;
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
