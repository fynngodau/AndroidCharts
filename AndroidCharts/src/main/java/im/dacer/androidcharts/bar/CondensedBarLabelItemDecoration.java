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
