import com.google.common.primitives.Doubles;
import org.jfree.data.xy.DefaultXYZDataset;

import java.util.ArrayList;


/**
 * @author Bingjun Zhang
 */


public class MetaPopulation {
    //    private ArrayList<ArrayList<Individual>> individuals;
    private ArrayList<IndividualInSpace> individuals;
    private LociPattern lociPattern;
    private int popSize = ModelParameters.getInt("POPULATION_SIZE");
    private float matingDistance = ModelParameters.getFloat("MATING_DISTANCE");
    public DefaultXYZDataset xyzDataset = new DefaultXYZDataset();
    private ArrayList<Double> xValues = new ArrayList<Double>();
    private ArrayList<Double> yValues = new ArrayList<Double>();
    private ArrayList<Double> zValues = new ArrayList<Double>();

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
                addToXYZArrays(-radius, y, 1);
                if (individuals.size() >= popSize) break initMetaPopulation;
                individuals.add(new IndividualInSpace(createIndividual(), radius, y));
                addToXYZArrays(radius, y, 1);
                if (individuals.size() >= popSize) break initMetaPopulation;
            }
            for (int x = -radius + 1; x < radius; x++) {
                individuals.add(new IndividualInSpace(createIndividual(), -x, -radius));
                addToXYZArrays(-x, radius, 1);
                if (individuals.size() >= popSize) break initMetaPopulation;
                individuals.add(new IndividualInSpace(createIndividual(), x, radius));
                addToXYZArrays(x, radius, 1);
                if (individuals.size() >= popSize) break initMetaPopulation;
            }
            radius++;
        }
        addToXYZDataset(xValues, yValues, zValues);
    }

    public MetaPopulation(MetaPopulation metaParent, int currentGeneration) {
        double[][] parentDistanceMatrix = metaParent.getProbabilityMatrix();
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
                GroupReturn parentIndividualInSpaceAndIndex = metaParent.getRandomIndividual(totals);
                IndividualInSpace parentIndividualInSpace = parentIndividualInSpaceAndIndex.getIndividualInSpace();
                int parentIndex = parentIndividualInSpaceAndIndex.getIndex();
                Individual parentIndividual = parentIndividualInSpace.getIndividual();
                float parentX = parentIndividualInSpace.getX();
                float parentY = parentIndividualInSpace.getY();
                if (currentGeneration <= ModelParameters.getInt("START_CREATING_ASEXUALS")) {
                    // sexually reproduce
                    IndividualInSpace mateIndividualInSpace = metaParent.getMateIndividual(metaParent, parentDistanceMatrix[parentIndex]);
                    float mateX = mateIndividualInSpace.getX();
                    float mateY = mateIndividualInSpace.getY();
                    IndividualPair parentPair = new IndividualPair(parentIndividual, mateIndividualInSpace.getIndividual());
                    IndividualPair offspringPair = parentPair.reproduce();
                    float offspringX = (parentX + mateX) / 2;
                    float offspringY = (parentY + mateY) / 2;
                    offspringPair.mutate(currentGeneration);
                    disperseOffspringPair(offspringPair, offspringX, offspringY);
                } else {
                    if (parentIndividual.getRecombinationStrength() > 0) {
                        // convert to asexuals
                        if (Rand.getFloat() < ModelParameters.getFloat("PROBABILITY_TO_ASEXUAL")) {
                            parentIndividual.setRecombinationStrength(0);
                            // asexually reproduction
                            for (int i = 0; i < 4; i++) {
                                Individual offspring = new Individual(parentIndividual);
                                offspring.mutate(currentGeneration);
                                disperse(offspring, parentX, parentY);
                            }
                        } else {
                            // sexually reproduce
                            IndividualInSpace mateIndividualInSpace = getMateIndividual(metaParent, parentDistanceMatrix[parentIndex]);
                            float mateX = mateIndividualInSpace.getX();
                            float mateY = mateIndividualInSpace.getY();
                            IndividualPair parentPair = new IndividualPair(parentIndividual, mateIndividualInSpace.getIndividual());
                            IndividualPair offspringPair = parentPair.reproduce();
                            float offspringX = (parentX + mateX) / 2;
                            float offspringY = (parentY + mateY) / 2;
                            offspringPair.mutate(currentGeneration);
                            disperseOffspringPair(offspringPair, offspringX, offspringY);
                        }
                    } else {
                        // asexually reproduce
                        for (int i = 0; i < 4; i++) {
                            Individual offspring = new Individual(parentIndividual);
                            offspring.mutate(currentGeneration);
                            disperse(offspring, parentX, parentY);
                        }
                    }
                }
            }

        } else {
            while (getSize() < popSize) {
                GroupReturn parentIndividualInSpaceAndIndex = metaParent.getRandomIndividual(totals);
                IndividualInSpace parentIndividualInSpace = parentIndividualInSpaceAndIndex.getIndividualInSpace();
                int parentIndex = parentIndividualInSpaceAndIndex.getIndex();
                Individual parentIndividual = parentIndividualInSpace.getIndividual();
                float parentX = parentIndividualInSpace.getX();
                float parentY = parentIndividualInSpace.getY();
                if (parentIndividual.getRecombinationStrength() > 0) {
                    // sexually reproduce
                    IndividualInSpace mateIndividualInSpace = getMateIndividual(metaParent, parentDistanceMatrix[parentIndex]);
                    float mateX = mateIndividualInSpace.getX();
                    float mateY = mateIndividualInSpace.getY();
                    IndividualPair parentPair = new IndividualPair(parentIndividual, mateIndividualInSpace.getIndividual());
                    IndividualPair offspringPair = parentPair.reproduce();
                    float offspringX = (parentX + mateX) / 2;
                    float offspringY = (parentY + mateY) / 2;
                    offspringPair.mutate(currentGeneration);
                    disperseOffspringPair(offspringPair, offspringX, offspringY);
                } else {
                    // asexually reproduce
                    for (int i = 0; i < 4; i++) {
                        Individual offspring = new Individual(parentIndividual);
                        offspring.mutate(currentGeneration);
                        disperse(offspring, parentX, parentY);
                    }
                }
            }
        }
        addToXYZDataset(xValues, yValues, zValues);
    }

    private void addToXYZDataset(ArrayList<Double> xValues, ArrayList<Double> yValues, ArrayList<Double> zValues) {
        double[][] data = new double[3][];
        data[0] = Doubles.toArray(xValues);
        data[1] = Doubles.toArray(yValues);
        data[2] = Doubles.toArray(zValues);
        xyzDataset.addSeries("xyzData", data);
    }

    private double[][] getProbabilityMatrix() {
        double[][] probabilityMatrix = new double[individuals.size()][individuals.size()];
        int triangleSide = 0;
        double distance;
        double p;
        double IndividualJFitness;
        for (int i = 0; i < individuals.size(); i++) {
            IndividualInSpace individualInSpaceI = getIndividualInSpace(i);
            float xi = individualInSpaceI.getX();
            float yi = individualInSpaceI.getY();
            for (int j = triangleSide; j < individuals.size(); j++) {
                IndividualInSpace individualInSpaceJ = getIndividualInSpace(j);
                IndividualJFitness = individualInSpaceJ.getIndividual().getFitness();
                float xj = individualInSpaceJ.getX();
                float yj = individualInSpaceJ.getY();
                distance = Math.sqrt(Math.pow((xj - xi), 2) + Math.pow((yj - yi), 2));
                if (distance == 0) {
                    p = 0;
                } else {
                    p = 1 / matingDistance * Math.exp(-1 * (1 / matingDistance) * distance) * IndividualJFitness;
                }
                probabilityMatrix[i][j] = p;
                probabilityMatrix[j][i] = p;
            }
            triangleSide++;
        }
        return probabilityMatrix;
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

    private void disperseOffspringPair(IndividualPair offspringPair, float x, float y) {
        disperse(offspringPair.getIndividualA(), x, y);
        disperse(offspringPair.getIndividualB(), x, y);
    }

    private void disperse(Individual offspring, float x, float y) {
        float disperseDistance = ModelParameters.getFloat("DISPERSE_DISTANCE");
        float newX = x + Rand.getFloat() * disperseDistance * 2 - disperseDistance;
        float newY = y + Rand.getFloat() * disperseDistance * 2 - disperseDistance;
        individuals.add(new IndividualInSpace(offspring, newX, newY));
        addToXYZArrays(newX, newY, offspring.getMutatorStrength());
    }

    private IndividualInSpace getMateIndividual(MetaPopulation metaParent, double[] parentProbabilityRow) {
        double[] totals = Util.initTotals(parentProbabilityRow);
        return metaParent.getRandomIndividual(totals).getIndividualInSpace();
    }

    private void addToXYZArrays(double x, double y, double z) {
        xValues.add(x);
        yValues.add(y);
        zValues.add(z);
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

    private GroupReturn getRandomIndividual(double[] totals) {
        int index = 0;
        while (index == totals.length) {
            index = WeightedRandomGenerator.nextInt(totals);
        }
        return new GroupReturn(getIndividualInSpace(index), index);
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

    public GroupReturn getFitnessPropertiesArray() {
        int popSize = getSize();
        double[] fitnessArray = new double[popSize];
        double[] meanDeleFitnessEffectArray = new double[popSize];
        double[] meanBeneFitnessEffectArray = new double[popSize];
        int[] nDeleMutArray = new int[popSize];
        int[] nBeneMutArray = new int[popSize];
        float x = 0;
        float y = 0;
        for (int i = 0; i < popSize; i++) {
            IndividualInSpace individualInSpace = getIndividualInSpace(i);
            GroupReturn fitnessProperties = individualInSpace.getIndividual().getFitnessProperties();
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
            mutatorStrengthArray[i] = getIndividualInSpace(i).getIndividual().getMutatorStrength();
            i++;
        }
        return mutatorStrengthArray;
    }

}
