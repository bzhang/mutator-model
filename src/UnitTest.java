import java.util.Arrays;

/**
 * @author Bingjun Zhang
 */
public class UnitTest {
    public static void main(String[] args) {
        String propertiesFileName = args.length > 0 ? args[0] : "MutatorModel.properties";
        ModelParameters.setPropertiesFileName(propertiesFileName);

//        testPoisson();
        testExponential();
    }

    private static void testExponential() {
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
