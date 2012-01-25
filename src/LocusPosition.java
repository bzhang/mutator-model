/*
 @author bingjun at 1/25/12 Time: 1:48 PM
 */
public class LocusPosition {
    private FitnessLocus fitnessLocus;
    private int position;

    public LocusPosition(FitnessLocus fitnessLocus, int position) {
        this.fitnessLocus = fitnessLocus;
        this.position = position;
    }

    public Locus getFitnessLocus() {
        return fitnessLocus;
    }

    public int getPosition() {
        return position;
    }
}
