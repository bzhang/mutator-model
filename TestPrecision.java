public class TestPrecision {
	public static void main(String[] args){
		double machEps = calculateMachineEpsilonFloat();	
		System.out.println(machEps);		
	}

	private static double calculateMachineEpsilonFloat() {
        	double machEps = 1.0;
        	do
 	          	machEps /= 2.0;
        	while ((double) (1.0 + (machEps / 2.0)) != 1.0);
         return machEps;
    }
}