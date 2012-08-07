import java.util.Arrays;

/**
 * @author Bingjun Zhang
 */
public class UnitTest {
    public static void main(String[] args) {
        String propertiesFileName = args.length > 0 ? args[0] : "MutatorModel.properties";
        ModelParameters.setPropertiesFileName(propertiesFileName);

//        float u = Rand.getFloat();
//        double u2 = Rand.getDouble();
//        System.out.println(u + "," + u2);

//        testPoisson();
//        testMutatorEffect();
        testFitnessEffect();
    }

    private static void testFitnessEffect() {
        int rep = 100000;
        double[] exponentialObs = new double[rep];
        for (int i = 0; i < rep; i++) {
            double u = Rand.getDouble();
            exponentialObs[i] = (-0.03 * Math.log(1 - u));
        }
        System.out.println(Arrays.toString(exponentialObs));
    }

    private static void testMutatorEffect() {
        int rep = 100000;
        double[] exponentialObs = new double[rep];
        for (int i = 0; i < rep; i++) {
            exponentialObs[i] = Math.pow(Rand.getDouble(), ModelParameters.getFloat("MUTATOR_MUTATION_EFFECT"));
        }
        System.out.println(Arrays.toString(exponentialObs));
    }

    private static void testPoisson() {
        int rep = 100000;
        int[] poissonObs = new int[rep];
        for (int i = 0; i < rep; i++) {
            poissonObs[i] = Util.getPoisson(1);
        }
        System.out.println(Arrays.toString(poissonObs));
    }

}
