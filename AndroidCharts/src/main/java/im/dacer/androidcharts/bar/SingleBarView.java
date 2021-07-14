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
    private final Rect renderRect = new Rect();
    private final Rect renderMultiValueRect = new Rect();

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
        renderRect.set(
                0,
                0,
                c.barWidth,
                getHeight() - c.valueLabelHeight - 2 * c.textMargin
        );
        canvas.drawRect(renderRect, c.bgPaint);

        // Bar foreground
        renderRect.set(0,
                (int) ((getHeight() - c.valueLabelHeight - 2 * c.textMargin) * (1f - bar.getDisplayPercentage())),
                c.barWidth,
                getHeight() - c.valueLabelHeight - 2 * c.textMargin);
        drawValueInRectangle(canvas, renderRect, bar.getValue());

        // Draw bar label if present
        String label = bar.getValue().getLabel();
        if (label != null) {

            // Use provided typeface
            c.textPaint.setTypeface(bar.getValue().getLabelTypeface());

            canvas.drawText(label, c.barWidth / 2f,
                    getHeight() - c.valueLabelDescent - c.textMargin, c.textPaint);
        }


    }

    /**
     * Specifically to deal with {@link MultiValue}s, which define percentages
     * of the bar that should be colored in specific colors, this method is
     * responsible for rendering the <code>value</code> into the space that
     * <code>rect</code> defines on <code>canvas</code> somehow.
     */
    protected void drawValueInRectangle(Canvas canvas, Rect rect, Value value) {
        if (value instanceof MultiValue) {
            MultiValue multiValue = (MultiValue) value;

            renderMultiValueRect.left = rect.left;
            renderMultiValueRect.right = rect.right;

            int size = rect.top - rect.bottom;

            // Track percentage of bottom edge
            float percentageUntilNow = 0;
            for (int i = 0; i < multiValue.getValuePercentages().length; i++) {
                renderMultiValueRect.bottom = rect.bottom + Math.round(size * percentageUntilNow);
                percentageUntilNow += multiValue.getValuePercentages()[i];
                renderMultiValueRect.top = rect.bottom + Math.round(size * percentageUntilNow);


                if (multiValue.getColors()[i] == null) {
                    // Use default color (accent color)
                    canvas.drawRect(renderMultiValueRect, c.fgPaint);
                } else {
                    variousColorPaint.setColor(multiValue.getColors()[i]);
                    canvas.drawRect(renderMultiValueRect, variousColorPaint);
                }
            }
        } else {
            canvas.drawRect(rect, c.fgPaint);
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
