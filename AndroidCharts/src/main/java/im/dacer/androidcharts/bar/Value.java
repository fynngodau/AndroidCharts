package im.dacer.androidcharts.bar;

public class Value {

    private final int value;
    private final String label;

    public Value(int value) {
        this(value, null);
    }

    /**
     * @param value Value to display in bar
     * @param label Text to display below this value
     */
    public Value(int value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * Top-to-bottom percentage (0% is at the top)
     *
     * @param max Maximum value to which to calculate relative percentage
     * @return Usually a float in the range [0;1]
     */
    float getPercentage(int max) {
        return 1 - (float) value / (float) max;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
