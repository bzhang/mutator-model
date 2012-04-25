

/**
 * @author Bingjun Zhang
 */
public class UnitTest {
    public static void main(String[] args) {
        int a = 1;
        float r = Rand.getFloat();
        a *= r;
        System.out.println("a = " + a + "\n" + "a * r = " + a * r);
    }

}
