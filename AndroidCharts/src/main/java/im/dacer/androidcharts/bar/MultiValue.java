package im.dacer.androidcharts.bar;

/**
 * Value with multiple differently-colored parts. Drawn bottom-to-top.
 */
public class MultiValue extends Value {

    private final float[] percentages;
    private final int[] colors;

    public MultiValue(int[] values, int[] colors) {
        this(values, colors, null);
    }

    public MultiValue(float[] percentages, int total, int[] colors) {
        this(percentages, total, colors, null);
    }

    public MultiValue(float[] percentages, int total, int[] colors, String label) {
        super(total, label);

        this.percentages = percentages;
        this.colors = colors;
    }

    public MultiValue(int[] values, int[] colors, String label) {
        super(sum(values), label);

        if (colors.length != values.length) {
            throw new IllegalArgumentException(
                    "Amount of colors (" + colors.length + ") does not match amount of values (" + values.length + ")!"
            );
        }

        percentages = new float[values.length];

        for (int i = 0; i < values.length; i++) {
            percentages[i] = values[i] / (float) getValue();
        }

        this.colors = colors;
    }

    float[] getValuePercentages() {
        return percentages;
    }

    int[] getColors() {
        return colors;
    }

    private static int sum(int[] array) {
        int sum = 0;
        for (int v : array) {
            sum += v;
        }
        return sum;
    }
}
