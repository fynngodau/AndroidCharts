package im.dacer.androidcharts.bar;

/**
 * Value with multiple differently-colored parts. Drawn bottom-to-top.
 */
public class MultiValue extends Value {

    private final float[] percentages;
    private final Integer[] colors;

    /**
     * @param colors Pass <code>null</code> to use default color (theme's accent color)
     * @param values Must be of same <code>length</code> as <code>colors</code>
     */

    public MultiValue(int[] values, Integer[] colors) {
        this(values, colors, null);
    }

    /**
     * @param colors      Pass <code>null</code> to use default color (theme's accent color)
     * @param percentages Must be of same <code>length</code> as <code>colors</code>
     */
    public MultiValue(float[] percentages, int total, Integer[] colors) {
        this(percentages, total, colors, null);
    }

    /**
     * @param colors      Pass <code>null</code> to use default color (theme's accent color)
     * @param percentages Must be of same <code>length</code> as <code>colors</code>
     */
    public MultiValue(float[] percentages, int total, Integer[] colors, String label) {
        super(total, label);

        this.percentages = percentages;
        this.colors = colors;
    }

    /**
     * @param colors Pass <code>null</code> to use default color (theme's accent color)
     * @param values Must be of same <code>length</code> as <code>colors</code>
     */
    public MultiValue(int[] values, Integer[] colors, String label) {
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

    Integer[] getColors() {
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
