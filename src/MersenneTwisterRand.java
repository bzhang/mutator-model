/**

 */

import org.apache.commons.math.random.MersenneTwister;

public class MersenneTwisterRand {
    private static MersenneTwister mt = new MersenneTwister();

    public static int getInt(int i) {
        return mt.nextInt(i);
    }

    public static float getFloat() {
        return mt.nextFloat();
    }

    public static double getDouble() {
        return mt.nextDouble();
    }


}
