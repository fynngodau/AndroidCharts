package im.dacer.androidcharts;

/**
 * Created by Dacer on 11/14/13.
 */
public class ClockPieSegment {

    private final float start;
    private float end;

    public ClockPieSegment(int startHour, int startMin, int endHour, int endMin) {
        start = 270f + startHour * 15 + startMin * 15 / 60f;
        end = 270f + endHour * 15 + endMin * 15 / 60f;
        while (end < start) {
            end += 360;
        }
    }

    public ClockPieSegment(int startHour, int startMin, int startSec, int endHour, int endMin,
                           int endSec) {
        start = 270f + startHour * 15 + startMin * 15 / 60f + startSec * 15 / 3600f;
        end = 270f + endHour * 15 + endMin * 15 / 60f + endSec * 15 / 3600f;
        while (end < start) {
            end += 360;
        }
    }

    public float getSweep() {
        return end - start;
    }

    public float getStart() {
        return start;
    }

    public float getEnd() {
        return end;
    }
}
