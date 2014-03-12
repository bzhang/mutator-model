//import com.google.common.primitives.Doubles;
//import org.jfree.data.xy.DefaultXYZDataset;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * @author Bingjun Zhang
// */
//
//
//public class MetaPopulation {
//    //    private ArrayList<ArrayList<Individual>> individuals;
//    private ArrayList<IndividualInSpace> individuals;
//    private Individual[][] individualsDiscrete;
//    private LociPattern lociPattern;
//    private int popSize = ModelParameters.getInt("POPULATION_SIZE");
//    private float matingDistance = ModelParameters.getFloat("MATING_DISTANCE");
//    public DefaultXYZDataset xyzDataset = new DefaultXYZDataset();
//    private ArrayList<Double> xValues = new ArrayList<Double>();
//    private ArrayList<Double> yValues = new ArrayList<Double>();
//    private ArrayList<Double> zValues = new ArrayList<Double>();
//    private int spaceRange = ModelParameters.getInt("SPACE_RANGE");
//
//    public MetaPopulation() {
//        if (ModelParameters.getBoolean("META_POPULATION_DISCRETE")) {
//            // Create the founder population
//            lociPattern = new LociPattern(ModelParameters.getInt("N_FITNESS_LOCI"),
//                    ModelParameters.getInt("N_MUTATOR_LOCI"), ModelParameters.getInt("N_RECOMBINATION_LOCI"));
//
//            individualsDiscrete = new Individual[spaceRange][spaceRange];
//            for (int i = 0; i < spaceRange; i++) {
//                for (int j = 0; i < spaceRange; j++) {
//                    individualsDiscrete[i][j] = createIndividual();
//                    addToXYZArrays(i, j, 1);
//                }
//            }
//            addToXYZDataset(xValues, yValues, zValues);
//        } else {
//            // Create the founder population
//            lociPattern = new LociPattern(ModelParameters.getInt("N_FITNESS_LOCI"),
//                    ModelParameters.getInt("N_MUTATOR_LOCI"), ModelParameters.getInt("N_RECOMBINATION_LOCI"));
//            individuals = new ArrayList<IndividualInSpace>();
//            individuals.add(new IndividualInSpace(createIndividual(), 0, 0));
//            addToXYZArrays(0, 0, 1);
//            int radius = 1;
//            initMetaPopulation:
//            while (true) {
//                for (int y = -radius; y <= radius; y++) {
//                    individuals.add(new IndividualInSpace(createIndividual(), -radius, y));
//                    addToXYZArrays(-radius, y, 1);
//                    if (individuals.size() >= popSize) break initMetaPopulation;
//                    individuals.add(new IndividualInSpace(createIndividual(), radius, y));
//                    addToXYZArrays(radius, y, 1);
//                    if (individuals.size() >= popSize) break initMetaPopulation;
//                }
//                for (int x = -radius + 1; x < radius; x++) {
//                    individuals.add(new IndividualInSpace(createIndividual(), x, -radius));
//                    addToXYZArrays(x, -radius, 1);
//                    if (individuals.size() >= popSize) break initMetaPopulation;
//                    individuals.add(new IndividualInSpace(createIndividual(), x, radius));
//                    addToXYZArrays(x, radius, 1);
//                    if (individuals.size() >= popSize) break initMetaPopulation;
//                }
//                radius++;
//            }
//            addToXYZDataset(xValues, yValues, zValues);
//        }
//    }
//
//    public MetaPopulation(MetaPopulation metaParent, int currentGeneration) {
//        if (ModelParameters.getBoolean("META_POPULATION_DISCRETE")) {
//            List<List<Integer>> directions = Util.getDirections();
//            double[] parentFitnessArray = metaParent.getFitnessArrayDiscrete();
//            double[] totals = Util.initTotals(parentFitnessArray);
//            lociPattern = metaParent.lociPattern;
//            individuals = new ArrayList<IndividualInSpace>();
//            if (totals[totals.length - 1] < 1e-10) {
//                System.out.println("Population is extinct at generation " + currentGeneration + "!");
//                System.exit(0);
//            }
//            while (getSize() < popSize) {
//                GroupReturn parentIndividualAndIndex = metaParent.getRandomIndividualDiscrete(totals);
//                Individual parentIndividual = parentIndividualAndIndex.getIndividual();
//                int parentX = parentIndividualAndIndex.getX();
//                int parentY = parentIndividualAndIndex.getY();
//                reproduceDiscrete(getMateIndividualDiscrete(parentX, parentY, directions), currentGeneration, parentIndividual, parentX, parentY);
//            }
//        } else {
//            double[][] parentDistanceMatrix = metaParent.getProbabilityMatrix();
//            double[] parentFitnessArray = metaParent.getFitnessArray();
//            double[] totals = Util.initTotals(parentFitnessArray);
//            lociPattern = metaParent.lociPattern;
//            individuals = new ArrayList<IndividualInSpace>();
//            if (totals[totals.length - 1] < 1e-10) {
//                System.out.println("Population is extinct at generation " + currentGeneration + "!");
//                System.exit(0);
//            }
//
//            if (ModelParameters.getBoolean("INVASION_EXPERIMENT")) {
//                while (getSize() < popSize) {
//                    GroupReturn parentIndividualInSpaceAndIndex = metaParent.getRandomIndividual(totals);
//                    IndividualInSpace parentIndividualInSpace = parentIndividualInSpaceAndIndex.getIndividualInSpace();
//                    int parentIndex = parentIndividualInSpaceAndIndex.getIndex();
//                    Individual parentIndividual = parentIndividualInSpace.getIndividual();
//                    float parentX = parentIndividualInSpace.getX();
//                    float parentY = parentIndividualInSpace.getY();
//                    if (currentGeneration > ModelParameters.getInt("START_CREATING_ASEXUALS")) {
//                        if (parentIndividual.getRecombinationStrength() > 0) {
//                            // convert to asexuals
//                            if (Rand.getFloat() < ModelParameters.getFloat("PROBABILITY_TO_ASEXUAL")) {
//                                parentIndividual.setRecombinationStrength(0);
//                            }
//                        }
//                    }
//                    reproduce(getMateIndividual(metaParent, parentDistanceMatrix[parentIndex]), currentGeneration, parentIndividual, parentX, parentY);
//                }
//
//            } else {
//                while (getSize() < popSize) {
//                    GroupReturn parentIndividualInSpaceAndIndex = metaParent.getRandomIndividual(totals);
//                    IndividualInSpace parentIndividualInSpace = parentIndividualInSpaceAndIndex.getIndividualInSpace();
//                    int parentIndex = parentIndividualInSpaceAndIndex.getIndex();
//                    Individual parentIndividual = parentIndividualInSpace.getIndividual();
//                    float parentX = parentIndividualInSpace.getX();
//                    float parentY = parentIndividualInSpace.getY();
//                    reproduce(getMateIndividual(metaParent, parentDistanceMatrix[parentIndex]), currentGeneration, parentIndividual, parentX, parentY);
//                }
//            }
//            addToXYZDataset(xValues, yValues, zValues);
//        }
//    }
//
//    private double[] getFitnessArrayDiscrete() {
//        double[] fitnessArray = new double[spaceRange * spaceRange];
//        for (int i = 0; i < individualsDiscrete.length; i++) {
//            for (int j = 0; j < individualsDiscrete[0].length; j++) {
//                fitnessArray[i * spaceRange + j] = individualsDiscrete[i][j].getFitness();
//            }
//        }
//        for (int i = 0; i < popSize; i++) {
//            fitnessArray[i] = getIndividualInSpace(i).getIndividual().getFitness();
//        }
//        return fitnessArray;
//    }
//
//    private GroupReturn getRandomIndividualDiscrete(double[] totals) {
//        int index = WeightedRandomGenerator.nextInt(totals);
//        while (index == totals.length) {
//            index = WeightedRandomGenerator.nextInt(totals);
//        }
//        return new GroupReturn(getIndividualDiscete(x, y), x, y);
//    }
//
//    private IndividualInSpace getMateIndividualDiscrete(int parentX, int parentY, List<List<Integer>> directions) {
//
//    }
//
//    private void reproduceDiscrete(IndividualInSpace mateIndividualInSpace, int currentGeneration, Individual parentIndividual, float parentX, float parentY) {
//
//    }
//
//    private void reproduce(IndividualInSpace mateIndividualInSpace, int currentGeneration, Individual parentIndividual, float parentX, float parentY) {
//        float mateX = mateIndividualInSpace.getX();
//        float mateY = mateIndividualInSpace.getY();
//        IndividualPair parentPair = new IndividualPair(parentIndividual, mateIndividualInSpace.getIndividual());
//        IndividualPair offspringPair = parentPair.reproduce();
//        float offspringX = (parentX + mateX) / 2;
//        float offspringY = (parentY + mateY) / 2;
//        offspringPair.mutate(currentGeneration);
//        disperseOffspringPair(offspringPair, offspringX, offspringY);
//    }
//
//    private void addToXYZDataset(ArrayList<Double> xValues, ArrayList<Double> yValues, ArrayList<Double> zValues) {
//        double[][] data = new double[3][];
//        data[0] = Doubles.toArray(xValues);
//        data[1] = Doubles.toArray(yValues);
//        data[2] = Doubles.toArray(zValues);
//        xyzDataset.addSeries("xyzData", data);
//    }
//
//    private double[][] getProbabilityMatrix() {
//        double[][] probabilityMatrix = new double[individuals.size()][individuals.size()];
//        int triangleSide = 0;
//        double distance;
//        double p;
//        double IndividualJFitness;
//        for (int i = 0; i < individuals.size(); i++) {
//            IndividualInSpace individualInSpaceI = getIndividualInSpace(i);
//            float xi = individualInSpaceI.getX();
//            float yi = individualInSpaceI.getY();
//            for (int j = triangleSide; j < individuals.size(); j++) {
//                IndividualInSpace individualInSpaceJ = getIndividualInSpace(j);
//                IndividualJFitness = individualInSpaceJ.getIndividual().getFitness();
//                float xj = individualInSpaceJ.getX();
//                float yj = individualInSpaceJ.getY();
//                distance = Math.sqrt(Math.pow((xj - xi), 2) + Math.pow((yj - yi), 2));
//                if (distance == 0) {
//                    p = 0;
//                } else {
//                    p = 1 / matingDistance * Math.exp(-1 * (1 / matingDistance) * distance) * IndividualJFitness;
//                }
//                probabilityMatrix[i][j] = p;
//                probabilityMatrix[j][i] = p;
//            }
//            triangleSide++;
//        }
//        return probabilityMatrix;
//    }
//
//    private Individual createIndividual() {
//        Individual individual = new Individual(lociPattern);
//        for (int location = 0; location < ModelParameters.getGenomeSize(); location++) {
//            if (lociPattern.getLocusType(location) == LociPattern.LocusType.Fitness) {
//                individual.setFitnessLocus(location);
//            } else if (lociPattern.getLocusType(location) == LociPattern.LocusType.Mutator) {
//                individual.setMutatorLocus(location, getRandomMutatorStrength());
//            } else {
//                individual.setRecombinationLocus(location, getRandomRecombinationStrength());
//            }
//        }
//        return individual;
//    }
//
//    private void disperseOffspringPair(IndividualPair offspringPair, float x, float y) {
//        disperse(offspringPair.getIndividualA(), x, y);
//        disperse(offspringPair.getIndividualB(), x, y);
//    }
//
//    private void disperse(Individual offspring, float x, float y) {
//        float disperseDistance = ModelParameters.getFloat("DISPERSE_DISTANCE");
//        double angle = Math.toRadians(Math.random() * 360);
//        double distance = -disperseDistance * Math.log(Rand.getDouble());
//        float newX = (float) (x + distance * Math.cos(angle));
//        float newY = (float) (y + distance * Math.sin(angle));
//        individuals.add(new IndividualInSpace(offspring, newX, newY));
//        addToXYZArrays(newX, newY, offspring.getMutatorStrength());
//    }
//
//    private IndividualInSpace getMateIndividual(MetaPopulation metaParent, double[] parentProbabilityRow) {
//        double[] totals = Util.initTotals(parentProbabilityRow);
//        return metaParent.getRandomIndividual(totals).getIndividualInSpace();
//    }
//
//    private void addToXYZArrays(double x, double y, double z) {
//        xValues.add(x);
//        yValues.add(y);
//        zValues.add(z);
//    }
//
//    private int getSize() {
//        return individuals.size();
//    }
//
//    private int getSizeDiscrete() {
////        return individuals.size();
//    }
//
//    private double[] getFitnessArray() {
//        double[] fitnessArray = new double[getSize()];
//        for (int i = 0; i < getSize(); i++) {
//            fitnessArray[i] = getIndividualInSpace(i).getIndividual().getFitness();
//        }
//        return fitnessArray;
//    }
//
//    private IndividualInSpace getIndividualInSpace(int i) {
//        return individuals.get(i);
//    }
//
//    private GroupReturn getRandomIndividual(double[] totals) {
//        int index = WeightedRandomGenerator.nextInt(totals);
//        while (index == totals.length) {
//            index = WeightedRandomGenerator.nextInt(totals);
//        }
//        return new GroupReturn(getIndividualInSpace(index), index);
//    }
//
//    private double getRandomMutatorStrength() {
//        double strength = 1;
//        // Generate mutator locus, strength ranging from [2, FOUNDER_MUTATOR_STRENGTH_MAX]
//        if (Rand.getFloat() < ModelParameters.getFloat("MUTATOR_RATIO")) {
//            strength = Rand.getInt(ModelParameters.getInt("FOUNDER_MUTATOR_STRENGTH_MAX") - 1) + 2;
//        }
//        return strength;
//    }
//
//    private float getRandomRecombinationStrength() {
//        float strength = 0;
//        // Generate recombination locus (sexual)
//        // Sexual to asexual ratio in founder population = RECOMBINATION_RATIO
//        // Initial Recombination strength = RECOMBINATION_RATE
//        if (ModelParameters.getFloat("RECOMBINATION_RATIO") == 0) {
//            return strength;
//        } else if (ModelParameters.getFloat("RECOMBINATION_RATIO") == 1) {
//            strength = ModelParameters.getFloat("RECOMBINATION_RATE");
//        } else if (Rand.getFloat() < ModelParameters.getFloat("RECOMBINATION_RATIO")) {
////            strength = Rand.getFloat();
//            strength = ModelParameters.getFloat("RECOMBINATION_RATE");
//        }
//        return strength;
//    }
//
//    public GroupReturn getFitnessPropertiesArray() {
//        int popSize = getSize();
//        double[] fitnessArray = new double[popSize];
//        double[] meanDeleFitnessEffectArray = new double[popSize];
//        double[] meanBeneFitnessEffectArray = new double[popSize];
//        int[] nDeleMutArray = new int[popSize];
//        int[] nBeneMutArray = new int[popSize];
//        float x = 0;
//        float y = 0;
//        for (int i = 0; i < popSize; i++) {
//            IndividualInSpace individualInSpace = getIndividualInSpace(i);
//            GroupReturn fitnessProperties = individualInSpace.getIndividual().getFitnessProperties();
//            fitnessArray[i] = fitnessProperties.getFitness();
//            meanDeleFitnessEffectArray[i] = fitnessProperties.getMeanDeleFitnessEffect();
//            meanBeneFitnessEffectArray[i] = fitnessProperties.getMeanBeneFitnessEffect();
//            nDeleMutArray[i] = fitnessProperties.getNDeleteriousMutations();
//            nBeneMutArray[i] = fitnessProperties.getNBeneficialMutations();
//        }
//        return new GroupReturn(fitnessArray, meanDeleFitnessEffectArray, meanBeneFitnessEffectArray, nDeleMutArray, nBeneMutArray);
//    }
//
//    public double[] getMutatorStrengthArray() {
//        double[] mutatorStrengthArray = new double[getSize()];
//        int i = 0;
//        while (i < getSize()) {
//            mutatorStrengthArray[i] = getIndividualInSpace(i).getIndividual().getMutatorStrength();
//            i++;
//        }
//        return mutatorStrengthArray;
//    }
//
//}
