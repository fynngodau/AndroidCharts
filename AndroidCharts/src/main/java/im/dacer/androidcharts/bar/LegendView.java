package im.dacer.androidcharts.bar;

import android.content.Context;
import android.graphics.*;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;
import im.dacer.androidcharts.CommonPaint;

class LegendView extends View {

    final SingleBarContext c;
    private Line[] lines = new Line[0];

    private int lineLabelTextHeight;

    private final Paint textPaint;
    private final Paint dashedLinePaint;
    private final Path renderPath;

    private final Paint linePaint;

    public LegendView(Context context, SingleBarContext barContext) {
        super(context);
        c = barContext;

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        setLayoutParams(layoutParams);

        textPaint = new Paint(c.textPaint);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.DEFAULT);

        dashedLinePaint = CommonPaint.makeCustomDashedPaint(context, CommonPaint.getForegroundLinePaint(context), 0);

        renderPath = new Path();

        linePaint = new Paint(CommonPaint.getForegroundLinePaint(context));
        linePaint.setColor(0x00000000);
    }

    void setLines(Line[] lines, int max) {
        this.lines = lines;

        Rect r = new Rect();

        for (Line line : lines) {
            line.setPercentageByMax(max);

            // Calculate maximum width and height of label text
            if (line.getLabel() != null) {
                textPaint.getTextBounds(line.getLabel(), 0, line.getLabel().length(), r);
                if (c.lineLabelWidth < r.width()) {
                    c.lineLabelWidth = r.width();
                }
                if (lineLabelTextHeight < r.height()) {
                    lineLabelTextHeight = r.height();
                }
            }
        }

        invalidate();
    }

    void setScrolledX(int x) {
        CommonPaint.makeCustomDashedPaint(getContext(), dashedLinePaint, x);

        invalidate();
    }

    void attachBackgroundColor(@ColorInt int color) {
        linePaint.setColor(color);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        // Draw vertical background lines

        for (Line line : lines) {

            int y = c.topMargin + (int) ((getHeight()
                    - c.topMargin
                    - c.valueLabelHeight
            ) * (1f - line.getPercentage()));

            if (line.getLabel() != null) {
                canvas.drawText(line.getLabel(), c.textMargin, y + lineLabelTextHeight + c.textMargin, textPaint);
            }

            renderPath.moveTo(0, y);
            renderPath.lineTo(getWidth(), y);
            canvas.drawPath(renderPath, linePaint);
            renderPath.rewind();

            renderPath.moveTo(0, y);
            renderPath.lineTo(getWidth(), y);

            canvas.drawPath(renderPath, dashedLinePaint);
            renderPath.rewind();
        }

    }

}
