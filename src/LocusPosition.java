/*
 @author Bingjun Zhang
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
