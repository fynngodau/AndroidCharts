package im.dacer.androidcharts.bar;

import android.content.Context;
import android.graphics.*;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import im.dacer.androidcharts.CommonPaint;

class LegendView extends View {

    final SingleBarContext c;
    private Line[] lines = new Line[0];
    private boolean zeroLineEnabled;

    private int lineLabelTextHeight;

    private int zeroLineSpace;

    private final Paint textPaint;
    private Paint dashedLinePaint;
    private final Path renderPath;

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

    void setZeroLineEnabled(boolean enabled) {
        this.zeroLineEnabled = enabled;
        invalidate();
    }

    void setScrolledX(int x, int viewX, int totalX) {
        CommonPaint.makeCustomDashedPaint(getContext(), dashedLinePaint, x);

        zeroLineSpace = Math.min(totalX - (viewX + x) - c.barSideMargin, 0);

        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        // Draw vertical background lines

        for (Line line : lines) {

            int y = c.topMargin + (int) ((getHeight()
                    - c.topMargin
                    - c.valueLabelHeight
                    - 2 * c.textMargin) * (1f - line.getPercentage()));

            if (line.getLabel() != null) {
                canvas.drawText(line.getLabel(), c.textMargin, y + lineLabelTextHeight + c.textMargin, textPaint);
            }

            renderPath.moveTo(0, y);
            renderPath.lineTo(getWidth(), y);
            canvas.drawPath(renderPath, dashedLinePaint);
        }

        renderPath.rewind();

        // Draw 0 line
        if (zeroLineEnabled) {
            float y = c.topMargin + (getHeight()
                    - c.topMargin
                    - c.valueLabelHeight
                    - 2 * c.textMargin)
                    + c.linePaint.getStrokeWidth() / 2;
            renderPath.moveTo(c.lineLabelWidth + c.barSideMargin, y);
            renderPath.lineTo(getWidth() - zeroLineSpace, y);
            canvas.drawPath(renderPath, c.linePaint);
        }

        renderPath.rewind();

    }

}
