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

        int barBottomY = getHeight() - valueLabelHeight - 2 * TEXT_MARGIN;

        // Bar background
        rect.set(lineLabelWidth, topMargin,
                lineLabelWidth + (BAR_SIDE_MARGIN + barWidth) * bars.length,
                barBottomY);
        canvas.drawRect(rect, bgPaint);

        for (int i = 0; i < bars.length; i++) {

            int barStartX = lineLabelWidth + BAR_SIDE_MARGIN * (i + 1) + barWidth * i;

            // Bar foreground
            rect.set(barStartX, topMargin + (int) ((getHeight()
                            - topMargin
                            - valueLabelHeight
                            - 2 * TEXT_MARGIN) * (1f - bars[i].getDisplayPercentage())),
                    lineLabelWidth + (BAR_SIDE_MARGIN + barWidth) * (i + 1),
                    barBottomY);
            canvas.drawRect(rect, fgPaint);

            // Draw bar label if present
            String label = bars[i].getValue().getLabel();
            if (label != null) {

                // Use provided typeface
                textPaint.setTypeface(bars[i].getValue().getLabelTypeface());

                // Draw text
                canvas.drawText(label, barStartX + TEXT_LEFT_MARGIN,
                        getHeight() - valueLabelDescent - TEXT_MARGIN, textPaint);

                // Draw indicator left of text
                switch (labelIndicatorMode) {
                    case IN_CHART:
                        canvas.drawLine(barStartX,
                                barBottomY,
                                barStartX,
                                topMargin,
                                dashedLinePaint
                        );

                    case BELOW_CHART:
                        canvas.drawLine(barStartX,
                                barBottomY,
                                barStartX,
                                getHeight(),
                                linePaint
                        );
                        break;

                    case HIDDEN:
                }
            }
        }
    }

    /**
     * Compared to superclass, this method does not set the bar width according to the width
     * of the label
     */
    @Override
    protected void updateValueLabelMeasurements(Value[] values) {

        valueLabelDescent = 0;

        for (Value s : values) {
            if (s.getLabel() != null) {
                textPaint.getTextBounds(s.getLabel(), 0, s.getLabel().length(), rect);
                if (valueLabelHeight < rect.height()) {
                    valueLabelHeight = rect.height();
                }
                if (valueLabelDescent < (Math.abs(rect.bottom))) {
                    valueLabelDescent = Math.abs(rect.bottom);
                }
            }
        }
    }

    @Override
    protected int measurePreferredWidth() {

        // Find last value with label
        int lastBarWithValueIndex = -1;

        for (int i = 0; i < bars.length; i++) {
            Bar bar = bars[i];
            if (bar.getValue().getLabel() != null) {
                lastBarWithValueIndex = i;
            }
        }

        if (lastBarWithValueIndex >= 0) {


            // Draw text
            String label = bars[lastBarWithValueIndex].getValue().getLabel();
            textPaint.getTextBounds(label, 0, label.length(), rect);

            int textStartPosition = lineLabelWidth + BAR_SIDE_MARGIN * (lastBarWithValueIndex + 1)
                    + barWidth * lastBarWithValueIndex + TEXT_LEFT_MARGIN;

            // End position is start position plus text
            int textEndPosition = textStartPosition + rect.width();

            int chartWidth = super.measurePreferredWidth();

            // Test if adding margin would add a white space to the end of the graph that is smaller than margin
            if (chartWidth >= textEndPosition && chartWidth < textEndPosition + TEXT_LEFT_MARGIN) {
                return chartWidth;
            } else return Math.max(
                    chartWidth,
                    textEndPosition + TEXT_LEFT_MARGIN // Add right margin
            );
        } else {
            return super.measurePreferredWidth();
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
