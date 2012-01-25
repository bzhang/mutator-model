/*
 @author bingjun at 1/25/12 Time: 1:48 PM
 */
public class LocusPosition {
    private Locus locus;
    private int position;

    public LocusPosition(Locus locus, int position) {
        this.locus = locus;
        this.position = position;
    }

    public Locus getLocus() {
        return locus;
    }

    public int getPosition() {
        return position;
    }
}
