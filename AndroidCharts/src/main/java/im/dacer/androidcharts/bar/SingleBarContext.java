package im.dacer.androidcharts.bar;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import im.dacer.androidcharts.CommonPaint;
import im.dacer.androidcharts.MyUtils;

/**
 * Value store for all {@link SingleBarView}s
 */
public class SingleBarContext {

    private final Context context;

    /**
     * Minimum bar width (not used by subclasses with own implementation of
     * {@link #updateValueLabelMeasurements(Value[])})
     */
    final int minBarWidth;

    /**
     * Space that should be left empty between two bars, as well as before the first and
     * after the last bar
     */
    int barSideMargin;

    /**
     * Margin around text
     */
    final int textMargin;

    final int topMargin;
    int lineLabelWidth;

    int valueLabelHeight;
    int valueLabelDescent;

    int barWidth;

    final Paint textPaint;
    final Paint bgPaint;
    final Paint fgPaint;
    final Paint linePaint;
    final Paint dashedLinePaint;



    public SingleBarContext(Context context) {
        this.context = context;


        minBarWidth = MyUtils.dip2px(context, 22);
        barSideMargin = MyUtils.dip2px(context, 22);
        textMargin = MyUtils.dip2px(context, 8);

        topMargin = MyUtils.dip2px(context, 5);
        barWidth = MyUtils.dip2px(context, 22);

        bgPaint = CommonPaint.getBackgroundPaint();
        fgPaint = CommonPaint.getForegroundPaint(context);
        linePaint = CommonPaint.getForegroundLinePaint(context);
        dashedLinePaint = CommonPaint.getDashedForegroundLinePaint(context);
        textPaint = CommonPaint.getTextPaint(context);

    }

    /**
     * Update {@link #barWidth}, {@link #valueLabelDescent} and {@link #valueLabelHeight}
     * using labels from the provided values
     */
    protected void updateValueLabelMeasurements(Value[] values) {

        Rect r = new Rect();
        valueLabelDescent = valueLabelHeight = 0;
        barWidth = minBarWidth;
        for (Value s : values) {
            if (s.getLabel() != null) {
                textPaint.getTextBounds(s.getLabel(), 0, s.getLabel().length(), r);
                if (valueLabelHeight < r.height()) {
                    valueLabelHeight = r.height();
                }
                if (barWidth < r.width()) {
                    barWidth = r.width();
                }
                if (valueLabelDescent < (Math.abs(r.bottom))) {
                    valueLabelDescent = Math.abs(r.bottom);
                }
            }
        }
    }



}
