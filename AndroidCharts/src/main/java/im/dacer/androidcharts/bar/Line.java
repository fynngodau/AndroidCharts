package im.dacer.androidcharts.bar;

/**
 * Represents a horizontal background line in the bar view for scale
 */
public class Line {

    private final int value;
    private final String label;

    private float percentage;

    /**
     * Construct horizontal background line at this value
     */
    public Line(int value) {
        this(value, null);
    }

    /**
     * Construct horizontal background line at this value
     * with this label
     *
     * @param label Text to be shown below line
     */
    public Line(int value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * Sets percentage to <code>value / max</code>
     *
     * @param max Topmost value in graph
     */
    void setPercentageByMax(int max) {
        percentage = (float) value / (float) max;
    }

    float getPercentage() {
        return percentage;
    }

    String getLabel() {
        return label;
    }
}
