package im.dacer.androidcharts.clockpie;

import android.graphics.Color;

/**
 * Created by Dacer on 11/14/13.
 */
public class ClockPieSegment {

    private final float start;
    private float end;

    private Integer color = null;

    public ClockPieSegment(int startHour, int startMin, int endHour, int endMin) {
        this(startHour, startMin, 0, endHour, endMin, 0);
    }

    public ClockPieSegment(int startHour, int startMin, int startSec, int endHour, int endMin,
                           int endSec) {
        /* From the docs:
         * "An angle of 0 degrees correspond to the geometric angle of 0 degrees (3 o'clock on a watch.)"
         * This is why we are adding 270f, to get back to 0 o'clock "on a watch".
         */
        start = 270f + startHour * 15 + startMin * 15 / 60f + startSec * 15 / 3600f;
        end = 270f + endHour * 15 + endMin * 15 / 60f + endSec * 15 / 3600f;
        while (end < start) {
            end += 360;
        }
    }

    float getSweep() {
        return end - start;
    }

    float getStart() {
        return start;
    }

    float getEnd() {
        return end;
    }

    public Integer getColor() {
        return color;
    }

    /**
     * Set color that this segment should be drawn in. Not supported for background segments
     */
    public void setColor(int color) {
        this.color = color;
    }
}
