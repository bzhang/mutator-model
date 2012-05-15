import java.util.Arrays;

/**
 * @author Bingjun Zhang
 */
public class UnitTest {
    public static void main(String[] args) {
        int rep = 100000;
        int[] poissonObs = new int[rep];
        for (int i = 0; i < rep; i++) {
            poissonObs[i] = Util.getPoisson(1);
        }
        System.out.println(Arrays.toString(poissonObs));
    }

}
