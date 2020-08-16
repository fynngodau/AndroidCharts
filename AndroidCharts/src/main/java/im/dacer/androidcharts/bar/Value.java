package im.dacer.androidcharts.bar;

public class Value {

    private final int value;

    public Value(int value) {
        this.value = value;
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
}
