/**
 * @author Bingjun
 * Created by @author Bingjun at 6/24/11 1:55 PM
 */

public abstract class Locus implements Cloneable{

    public Object clone() throws CloneNotSupportedException {
        Object cloned = null;
        try {
            cloned = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloned;
    }

}
