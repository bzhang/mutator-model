import java.util.Random;

/**
 * Created by Bingjun at 7/20/11 11:21 PM
 */

public class Rand {

    private static Random random = new Random(System.nanoTime());

    public static int getInt(int i) {
        return random.nextInt(i);
    }

    public static float getFloat() {
        return random.nextFloat();
    }

    public static double getDouble() {
        return random.nextDouble();
    }

}
