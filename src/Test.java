import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bingjun at 8/25/11 5:08 PM
 */
public class Test {
    public static void main(String[] args) {
        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        ArrayList<String> mutationProperties = new ArrayList<String>();
        mutationProperties.add("2");
        mutationProperties.add("3.5");
        map.put("1", mutationProperties);
        System.out.println();
        System.out.println("Elements of Map");
        System.out.println(map);
    }
}
