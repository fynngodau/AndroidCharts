package im.dacer.androidcharts.bar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CondensedBarLabelItemDecoration extends LabelItemDecoration<CondensedSingleBarContext> {

    CondensedBarView.LabelIndicatorMode labelIndicatorMode = CondensedBarView.LabelIndicatorMode.BELOW_CHART;

    public CondensedBarLabelItemDecoration(CondensedSingleBarContext context) {
        super(context);
    }

    /**
     * Compared to superclass, this method does not set the bar width according to the width
     * of the label
     */
    @Override
    protected void updateValueLabelMeasurements(Value[] values) {

        valueLabelDescent = valueLabelHeight = 0;

        for (Value s : values) {
            if (s.getLabel() != null) {
                c.textPaint.getTextBounds(s.getLabel(), 0, s.getLabel().length(), rect);
                if (valueLabelHeight < rect.height()) {
                    valueLabelHeight = rect.height();
                }
                if (valueLabelDescent < (Math.abs(rect.bottom))) {
                    valueLabelDescent = Math.abs(rect.bottom);
                }
            }
        }

        valueLabelHeight += c.textMargin * 2;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = valueLabelHeight;

        if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            // Find out whether additional space is required at the very last item

            // Find last value with label
            int lastBarWithValueIndex = -1;

            for (int i = 0; i < values.length; i++) {
                Value value = values[i];
                if (value.getLabel() != null) {
                    lastBarWithValueIndex = i;
                }
            }

            if (lastBarWithValueIndex >= 0) {

                // Calculate text size
                String label = values[lastBarWithValueIndex].getLabel();
                super.c.textPaint.getTextBounds(label, 0, label.length(), rect);

                // Calculate space that is available for this view
                int space = (state.getItemCount() - lastBarWithValueIndex) * (super.c.barSideMargin + super.c.barWidth);
                int textWidth = rect.width() + super.c.textLeftMargin * 2;

                if (space < textWidth) {
                    outRect.right = textWidth - space;
                }

            }
        }
    }

    @Override
    protected void drawLabel(@NonNull Canvas c, @NonNull RecyclerView parent, View view, Value value) {

        super.c.textPaint.setTextAlign(Paint.Align.LEFT);

        // Draw text
        c.drawText(value.getLabel(), view.getLeft() + super.c.textLeftMargin,
                parent.getHeight() - valueLabelDescent - super.c.textMargin, super.c.textPaint);

        // Draw indicator left of text
        switch (labelIndicatorMode) {
            case IN_CHART:
                c.drawLine(view.getLeft(),
                        view.getBottom(),
                        view.getLeft(),
                        super.c.topMargin,
                        super.c.dashedLinePaint
                );

            case BELOW_CHART:
                c.drawLine(view.getLeft(),
                        view.getBottom(),
                        view.getLeft(),
                        parent.getHeight(),
                        super.c.linePaint
                );
                break;

            case HIDDEN:


        }
    }

    public void setLabelIndicatorMode(CondensedBarView.LabelIndicatorMode labelIndicatorMode) {
        this.labelIndicatorMode = labelIndicatorMode;
    }

}
