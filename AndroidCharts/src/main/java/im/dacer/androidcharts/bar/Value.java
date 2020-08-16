package im.dacer.androidcharts.bar;

import android.graphics.Typeface;

public class Value {

    private final int value;
    private final String label;
    private Typeface labelTypeface = Typeface.DEFAULT;

    /**
     * @param value Value to display in bar
     */
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
     * @param max Maximum value to which to calculate relative percentage
     * @return <code>value / max</code>, usually a float in the range [0;1]
     */
    float getPercentage(int max) {
        return (float) value / (float) max;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Set typeface to render label with
     * @param labelTypeface
     */
    public void setLabelTypeface(Typeface labelTypeface) {
        this.labelTypeface = labelTypeface;
    }

    Typeface getLabelTypeface() {
        return labelTypeface;
    }
}
