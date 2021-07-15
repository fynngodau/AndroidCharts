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

    int barWidth;

    final Paint textPaint;
    final Paint bgPaint;
    final Paint fgPaint;
    final Paint linePaint;
    final Paint dashedLinePaint;

    final boolean labelInSingleBarView;

    public SingleBarContext(Context context) {

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


        labelInSingleBarView = true;

    }

    protected SingleBarContext(Context context, int minBarWidth, int textMargin, int topMargin, Paint textPaint,
                               Paint bgPaint, Paint fgPaint, Paint linePaint, Paint dashedLinePaint,
                               boolean labelInSingleBarView) {
        this.minBarWidth = minBarWidth;
        this.textMargin = textMargin;
        this.topMargin = topMargin;
        this.textPaint = textPaint;
        this.bgPaint = bgPaint;
        this.fgPaint = fgPaint;
        this.linePaint = linePaint;
        this.dashedLinePaint = dashedLinePaint;
        this.labelInSingleBarView = labelInSingleBarView;
    }



}
