package im.dacer.androidcharts.bar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BarLabelItemDecoration extends LabelItemDecoration<SingleBarContext> {

    public BarLabelItemDecoration(SingleBarContext context) {
        super(context);
    }

    @Override
    protected void updateValueLabelMeasurements(Value[] values) {
        Rect r = new Rect();
        valueLabelDescent = valueLabelHeight = 0;
        c.barWidth = c.minBarWidth;
        for (Value s : values) {
            if (s.getLabel() != null) {
                c.textPaint.getTextBounds(s.getLabel(), 0, s.getLabel().length(), r);
                if (valueLabelHeight < r.height()) {
                    valueLabelHeight = r.height();
                }
                if (c.barWidth < r.width()) {
                    c.barWidth = r.width();
                }
                if (valueLabelDescent < (Math.abs(r.bottom))) {
                    valueLabelDescent = Math.abs(r.bottom);
                }
            }
        }

        // Add text margin if labels are to be shown
        if (valueLabelHeight > 0) {
            valueLabelHeight += 2 * c.textMargin;
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = valueLabelHeight;
    }

    @Override
    protected void drawLabel(@NonNull Canvas c, @NonNull RecyclerView parent, View view, Value value) {

        super.c.textPaint.setTextAlign(Paint.Align.CENTER);

        c.drawText(value.getLabel(), view.getLeft() + super.c.barWidth / 2f,
                parent.getHeight() - valueLabelDescent - super.c.textMargin, super.c.textPaint);

    }
}
