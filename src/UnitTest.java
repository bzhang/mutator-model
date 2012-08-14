import java.util.Arrays;

/**
 * @author Bingjun Zhang
 */
public class UnitTest {
    public static void main(String[] args) {
//        String propertiesFileName = args.length > 0 ? args[0] : "MutatorModel.properties";
//        ModelParameters.setPropertiesFileName(propertiesFileName);

//        float u = Rand.getFloat();
//        double u2 = Rand.getDouble();
//        System.out.println(u + "," + u2);

//        testWeightedRandomGenerator();
//        testPoisson();
//        testMutatorEffect();
//        testFitnessEffect();
        testMersenneTwister();


    }

    private static void testMersenneTwister() {
        int rep = 10;
        for (int i = 0; i < rep; i++) {
            double u = MersenneTwisterRand.getDouble();
            System.out.println(u);
        }
    }

    private static void testWeightedRandomGenerator() {
        double[] totals = {1, 3, 5, 6, 10, 13, 30};
        int index = Arrays.binarySearch(totals, 33);
        index = (index >= 0) ? index : - index - 1;
        System.out.println(index);

    }

    private static void testFitnessEffect() {
        int rep = 10000;
        double[] exponentialObs = new double[rep];
        for (int i = 0; i < rep; i++) {
            double u = Rand.getDouble();
            exponentialObs[i] = (-0.03 * Math.log(1 - u));
        }
        System.out.println(Arrays.toString(exponentialObs));
    }

    private static void testMutatorEffect() {
        int rep = 10000;
        double[] exponentialObs = new double[rep];
        for (int i = 0; i < rep; i++) {
            exponentialObs[i] = Math.pow(Rand.getDouble(), 0.03);
        }
        Util.writeFile("test.txt", Arrays.toString(exponentialObs));
    }

    private static void testPoisson() {
        int rep = 10000;
        int[] poissonObs = new int[rep];
        for (int i = 0; i < rep; i++) {
            poissonObs[i] = Util.getPoisson(0.05);
        }
        Util.writeFile("test.txt", Arrays.toString(poissonObs));
    }

}
