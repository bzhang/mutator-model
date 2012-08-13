/**

 */

import org.apache.commons.math.random.MersenneTwister;

public class MersenneTwisterRand {
    public MersenneTwisterRand() {
        MersenneTwister mt = new MersenneTwister();
        int rep = 100;
        for (int i = 0; i < rep; i++){
            double randDouble = mt.nextDouble();
            System.out.println(randDouble);
        }
    }

}
