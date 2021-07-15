package im.dacer.androidcharts.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Added to {@link BarView} through {@link Adapter}
 */
class SingleBarView extends View {

    private Bar bar;

    private final SingleBarContext c;

    private final Paint variousColorPaint;


    public SingleBarView(Context context, SingleBarContext barContext) {
        super(context);
        setMinimumWidth(barContext.barWidth);
        setMinimumHeight(100);
        this.c = barContext;
        variousColorPaint = new Paint(c.fgPaint);
    }

    void setBar(Bar bar) {
        this.bar = bar;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Bar background
        canvas.drawRect(
                0,
                0,
                c.barWidth,
                getHeight(),
                c.bgPaint
        );

        // Draw foreground
        if (bar.getValue() instanceof MultiValue) {
            MultiValue multiValue = (MultiValue) bar.getValue();

            // Total bar size in pixels
            float totalValueSize = getHeight() * bar.getDisplayPercentage();

            // Track percentage already drawn from bottom edge
            float percentageUntilNow = 0;

            // Iterate over multi-value's individual values
            for (int i = 0; i < multiValue.getValuePercentages().length; i++) {

                // Calculate bottom and top values of current individual value
                float bottom = getHeight() - Math.round(totalValueSize * percentageUntilNow);
                percentageUntilNow += multiValue.getValuePercentages()[i];
                float top = getHeight() - Math.round(totalValueSize * percentageUntilNow);

                // Choose paint
                final Paint paint;
                if (multiValue.getColors()[i] == null) {
                    // Use default color (accent color)
                    paint = c.fgPaint;
                } else {
                    // Use color from individual value
                    paint = variousColorPaint;
                    variousColorPaint.setColor(multiValue.getColors()[i]);
                }

                canvas.drawRect(
                        0,
                        top,
                        c.barWidth,
                        bottom,
                        paint
                );
            }
        } else {
            // Draw simple Value
            canvas.drawRect(0,
                    (int) ((getHeight()) * (1f - bar.getDisplayPercentage())),
                    c.barWidth,
                    getHeight(), c.fgPaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                c.barWidth,
                MeasureSpec.getSize(heightMeasureSpec)
        );
    }
}
