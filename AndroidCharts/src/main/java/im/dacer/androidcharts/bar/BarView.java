package im.dacer.androidcharts.bar;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import im.dacer.androidcharts.CommonPaint;
import im.dacer.androidcharts.MyUtils;

import java.util.*;

/**
 * Created by Dacer on 11/11/13.
 */
public class BarView extends View {

    /**
     * Minimum bar width (not used by subclasses with own implementation of
     * {@link #updateValueLabelMeasurements(Value[])})
     */
    private final int MIN_BAR_WIDTH;
    /**
     * Margin to the left and right of each bar
     */
    protected int BAR_SIDE_MARGIN;
    /**
     * Margin around text
     */
    protected final int TEXT_MARGIN;

    private static final int BACKGROUND_COLOR = Color.parseColor("#F6F6F6");

    protected Bar[] bars = new Bar[0];

    protected final Paint textPaint;
    protected final Paint bgPaint;
    protected final Paint fgPaint;
    protected final Paint linePaint;

    protected final Rect rect;
    protected int barWidth;
    protected int valueLabelDescent;

    protected final int topMargin;
    protected int lineLabelWidth;

    protected int valueLabelHeight;
    private int lineLabelTextHeight;

    private Line[] lines = new Line[0];

    private boolean zeroLineEnabled = true;

    private final Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;

            for (Bar bar : bars) {
                needNewFrame = needNewFrame | bar.animationStep();
            }

            if (needNewFrame) {
                postDelayed(this, 20);
            }
            invalidate();
        }
    };

    public BarView(Context context) {
        this(context, null);
    }

    public BarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(BACKGROUND_COLOR);

        fgPaint = CommonPaint.getForegroundPaint(context);
        linePaint = CommonPaint.getBackgroundLinePaint(context);

        rect = new Rect();

        topMargin = MyUtils.dip2px(context, 5);
        barWidth = MyUtils.dip2px(context, 22);
        MIN_BAR_WIDTH = MyUtils.dip2px(context, 22);
        BAR_SIDE_MARGIN = MyUtils.dip2px(context, 22);
        TEXT_MARGIN = MyUtils.dip2px(context, 8);

        textPaint = CommonPaint.getTextPaint(context);
    }

    /**
     * Update {@link #barWidth}, {@link #valueLabelDescent} and {@link #valueLabelHeight}
     * using labels from the provided values
     */
    protected void updateValueLabelMeasurements(Value[] values) {

        Rect r = new Rect();
        valueLabelDescent = 0;
        barWidth = MIN_BAR_WIDTH;
        for (Value s : values) {
            if (s.getLabel() != null) {
                textPaint.getTextBounds(s.getLabel(), 0, s.getLabel().length(), r);
                if (valueLabelHeight < r.height()) {
                    valueLabelHeight = r.height();
                }
                if (barWidth < r.width()) {
                    barWidth = r.width();
                }
                if (valueLabelDescent < (Math.abs(r.bottom))) {
                    valueLabelDescent = Math.abs(r.bottom);
                }
            }
        }
    }

    /**
     * Sets typefaces to <code>Typeface.DEFAULT</code> except for the position-th typeface,
     * which will be set to <code>Typeface.DEFAULT_BOLD</code>
     *
     * @param position Position in bottom text list which should be bolded
     */
    public void setBoldPosition(int position) {
        for (int i = 0; i < bars.length; i++) {
            if (i == position) {
                bars[i].getValue().setLabelTypeface(Typeface.DEFAULT_BOLD);
            } else {
                bars[i].getValue().setLabelTypeface(Typeface.DEFAULT);
            }
        }

        postInvalidate();
    }

    /**
     * Draw vertical background lines
     *
     * @param max The top border of the chart
     */
    public void setVerticalLines(Line[] lines, int max) {

        lineLabelWidth = 0;

        Rect r = new Rect();

        for (Line line : lines) {
            line.setPercentageByMax(max);

            // Calculate maximum width and height of label text
            if (line.getLabel() != null) {
                textPaint.getTextBounds(line.getLabel(), 0, line.getLabel().length(), r);
                if (lineLabelWidth < r.width()) {
                    lineLabelWidth = r.width();
                }
                if (lineLabelTextHeight < r.height()) {
                    lineLabelTextHeight = r.height();
                }
            }
        }

        this.lines = lines;

        postInvalidate();
    }

    public void setZeroLineEnabled(boolean enabled) {
        zeroLineEnabled = enabled;
    }

    public void setData(Value[] values) {
        setData(values, 0);
    }

    /**
     * @param max The top border of the chart, or 0 to use highest value
     */
    public void setData(Value[] values, int max) {

        int highestValue = Collections.max(Arrays.asList(values), new Comparator<Value>() {
            @Override
            public int compare(Value value, Value t1) {
                return Integer.compare(value.getValue(), t1.getValue());
            }
        }).getValue();

        if (max < highestValue) {
            if (max != 0) {
                Log.w("BarView", "Inappropriate max! Using highest value as max instead. If you meant to do that, pass 0 to remove this warning.");
            }
            max = highestValue;
        }

        // Copy references to bars that continue to exist
        Bar[] newBars = Arrays.copyOf(bars, values.length);

        // Fill remaining with new Bars
        for (int i = Math.min(values.length, bars.length); i < newBars.length; i++) {
            newBars[i] = new Bar();
        }

        // Set values to bars
        for (int i = 0; i < newBars.length; i++) {
            newBars[i].setValue(values[i], max);
        }

        bars = newBars;

        updateValueLabelMeasurements(values);

        // Why is this call here?
        setMinimumWidth(2);

        // Stop ongoing animation
        removeCallbacks(animator);

        // Start new animation
        post(animator);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Draw vertical background lines



        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.DEFAULT);

        Path path = new Path();
        for (Line line : lines) {

            int y = topMargin + (int) ((getHeight()
                    - topMargin
                    - valueLabelHeight
                    - 2 * TEXT_MARGIN) * (1f - line.getPercentage()));

            if (line.getLabel() != null) {
                canvas.drawText(line.getLabel(), TEXT_MARGIN, y + lineLabelTextHeight + TEXT_MARGIN, textPaint);
            }

            path.moveTo(0, y);
            path.lineTo(getWidth(), y);
            canvas.drawPath(path, linePaint);
        }

        // Draw 0 line
        if (zeroLineEnabled && lines.length > 1) {
            float y = topMargin + (getHeight()
                    - topMargin
                    - valueLabelHeight
                    - 2 * TEXT_MARGIN)
                    + linePaint.getStrokeWidth() / 2;
            path.moveTo(lineLabelWidth + BAR_SIDE_MARGIN, y);
            path.lineTo(getWidth(), y);
            canvas.drawPath(path, linePaint);
        }

        // Draw bars
        drawBars(canvas);
    }

    protected void drawBars(Canvas canvas) {
        textPaint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < bars.length; i++) {
            // Bar background
            rect.set(lineLabelWidth + BAR_SIDE_MARGIN * (i + 1) + barWidth * i, topMargin,
                    lineLabelWidth + (BAR_SIDE_MARGIN + barWidth) * (i + 1),
                    getHeight() - valueLabelHeight - 2 * TEXT_MARGIN);
            canvas.drawRect(rect, bgPaint);

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

                canvas.drawText(label, lineLabelWidth + BAR_SIDE_MARGIN * (i + 1) + barWidth * i + barWidth / 2f,
                        getHeight() - valueLabelDescent - TEXT_MARGIN, textPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mViewWidth = getMeasurement(widthMeasureSpec, measurePreferredWidth());
        int mViewHeight = getMeasurement(heightMeasureSpec, 222);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    protected int measurePreferredWidth() {
        return bars.length * (barWidth + BAR_SIDE_MARGIN) + BAR_SIDE_MARGIN + lineLabelWidth;
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }
}
