package im.dacer.androidcharts.bar;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class LabelItemDecoration<C extends SingleBarContext> extends RecyclerView.ItemDecoration {

    private Value[] values = new Value[0];
    protected C c;

    protected Rect rect = new Rect();

    protected int valueLabelHeight = 0, valueLabelDescent = 0;

    public LabelItemDecoration(C context) {
        c = context;

        updateValueLabelMeasurements(values);
    }

    protected abstract void updateValueLabelMeasurements(Value[] values);

    @Override
    @CallSuper
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        c.clipRect(
                parent.getPaddingLeft() - this.c.linePaint.getStrokeWidth() / 2, parent.getPaddingTop(),
                parent.getWidth() - parent.getPaddingRight(),
                parent.getHeight() - parent.getPaddingBottom()
        );

        for (int i = 0; i < parent.getChildCount(); i++) {

            View view = parent.getChildAt(i);
            int valuePos = parent.getChildAdapterPosition(view);

            if (values[valuePos].getLabel() != null) {
                this.c.textPaint.setTypeface(values[valuePos].getLabelTypeface());
                drawLabel(c, parent, view, values[valuePos]);
            }
        }
    }

    protected abstract void drawLabel(@NonNull Canvas c, @NonNull RecyclerView parent, View view, Value value);

    public void setValues(Value[] values) {
        this.values = values;
        updateValueLabelMeasurements(values);
    }
}
