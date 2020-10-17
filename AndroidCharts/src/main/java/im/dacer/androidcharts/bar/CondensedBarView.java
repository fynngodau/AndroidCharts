package im.dacer.androidcharts.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import im.dacer.androidcharts.MyUtils;

public class CondensedBarView extends BarView {

    private final int TEXT_LEFT_MARGIN;

    private LabelIndicatorMode labelIndicatorMode = LabelIndicatorMode.BELOW_CHART;

    public CondensedBarView(Context context) {
        this(context, null);
    }

    public CondensedBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        BAR_SIDE_MARGIN = 0;
        TEXT_LEFT_MARGIN =
                barWidth = MyUtils.dip2px(context, 4);
        textPaint.setStrokeWidth(MyUtils.dip2px(context, 1));
    }

    public void setBarWidth(int dp) {
        barWidth = MyUtils.dip2px(getContext(), dp);

        postInvalidate();
    }

    public void setLabelIndicatorMode(LabelIndicatorMode labelIndicatorMode) {
        this.labelIndicatorMode = labelIndicatorMode;
    }

    @Override
    protected void drawBars(Canvas canvas) {
        textPaint.setTextAlign(Paint.Align.LEFT);

        // Bar background
        rect.set(lineLabelWidth, topMargin,
                lineLabelWidth + (BAR_SIDE_MARGIN + barWidth) * bars.length,
                getHeight() - valueLabelHeight - 2 * TEXT_MARGIN);
        canvas.drawRect(rect, bgPaint);

        for (int i = 0; i < bars.length; i++) {

            // Bar foreground
            rect.set(lineLabelWidth + BAR_SIDE_MARGIN * (i + 1) + barWidth * i, topMargin + (int) ((getHeight()
                            - topMargin
                            - valueLabelHeight
                            - 2 * TEXT_MARGIN) * (1f - bars[i].getDisplayPercentage())),
                    lineLabelWidth + (BAR_SIDE_MARGIN + barWidth) * (i + 1),
                    getHeight() - valueLabelHeight - 2 * TEXT_MARGIN);
            canvas.drawRect(rect, fgPaint);

            // Draw bar label if present
            String label = bars[i].getValue().getLabel();
            if (label != null) {

                // Use provided typeface
                textPaint.setTypeface(bars[i].getValue().getLabelTypeface());

                // Draw text
                canvas.drawText(label, lineLabelWidth + BAR_SIDE_MARGIN * (i + 1) + barWidth * i + TEXT_LEFT_MARGIN,
                        getHeight() - valueLabelDescent - TEXT_MARGIN, textPaint);

                if (labelIndicatorMode != LabelIndicatorMode.HIDDEN) {

                    int start = labelIndicatorMode == LabelIndicatorMode.BELOW_CHART?
                            getHeight() - valueLabelHeight - 2 * TEXT_MARGIN : topMargin;

                    // Draw indicator left to text
                    canvas.drawLine(lineLabelWidth + BAR_SIDE_MARGIN * (i + 1) + barWidth * i,
                            start,
                            lineLabelWidth + BAR_SIDE_MARGIN * (i + 1) + barWidth * i,
                            getHeight(),
                            linePaint
                    );
                }
            }
        }
    }

    /**
     * Compared to superclass, this method does not set the bar width according to the width
     * of the label
     * @param values
     */
    @Override
    protected void updateValueLabelMeasurements(Value[] values) {

        Rect r = new Rect();
        valueLabelDescent = 0;

        for (Value s : values) {
            if (s.getLabel() != null) {
                textPaint.getTextBounds(s.getLabel(), 0, s.getLabel().length(), r);
                if (valueLabelHeight < r.height()) {
                    valueLabelHeight = r.height();
                }
                if (valueLabelDescent < (Math.abs(r.bottom))) {
                    valueLabelDescent = Math.abs(r.bottom);
                }
            }
        }
    }

    public enum LabelIndicatorMode {
        /**
         * Do not draw label indicators
         */
        HIDDEN,
        /**
         * Draw label indicators below the chart
         */
        BELOW_CHART,
        /**
         * Draw label indicators below as well as through the chart
         */
        IN_CHART
    }
}
