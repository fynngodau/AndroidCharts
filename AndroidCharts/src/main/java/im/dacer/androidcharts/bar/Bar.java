package im.dacer.androidcharts.bar;

/**
 * Represents a bar in the bar view
 */
class Bar {

    /**
     * Value that this bar should currently display
     */
    private Value value;

    /**
     * Percentage of value
     */
    private float percentage;

    /**
     * Percentage is only displayed after the animation is
     * complete. This value is the actually displayed percentage.
     */
    private float displayPercentage = 1f;

    void setValue(Value value, int max) {
        this.value = value;
        percentage = value.getPercentage(max);
    }

    float getDisplayPercentage() {
        return displayPercentage;
    }

    /**
     * @return <code>true</code> is another animation frame is
     * required, <code>false</code> if this was the last step
     */
    boolean animationStep() {

        // Gradually increase / decrease bar view

        if (displayPercentage + 0.02f < percentage) {
            displayPercentage += 0.02f;
            return true;
        } else if (displayPercentage - 0.02f > percentage) {
            displayPercentage -= 0.02f;
            return true;
        }

        // If remaining difference for this bar is smaller than one frame, set to final value
        if (Math.abs(percentage - displayPercentage) < 0.02f) {
            displayPercentage = percentage;
            return false;
        }

        return false;
    }

    public Value getValue() {
        return value;
    }
}
