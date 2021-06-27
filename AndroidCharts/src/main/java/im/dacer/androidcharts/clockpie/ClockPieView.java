package im.dacer.androidcharts.clockpie;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import im.dacer.androidcharts.CommonPaint;
import im.dacer.androidcharts.MyUtils;

import java.util.List;

/**
 * Created by Dacer on 11/13/13.
 */
public class ClockPieView extends View {

    private final Paint textPaint;
    private final Paint foregroundPaint;

    /**
     * Paint for background and lines
     */
    private final Paint backgroundPaint;
    private final Paint backgroundSegmentPaint;

    /**
     * Size the view is drawn in.
     * <p>Set the the maximum of width and height during {@link #onMeasure(int, int)}.</p>
     */
    private int viewSize;

    /**
     * Pixels the whole view is moved to the right from x = 0,
     * as the view is drawn centered.
     */
    private int offset;
    private final int textSize;
    private int pieRadius;

    private final Point pieCenterPoint = new Point();

    /**
     * Length of the 24 lines that surround the clock
     */
    private final int lineLength;

    /**
     * Thickness of the 24 lines that surround the clock
     */
    private final int lineThickness;

    /*
     * Measured variables
     */
    private final float leftTextWidth;
    private final float rightTextWidth;
    private final float topTextHeight;

    /**
     * Helper rectangle with width = height = radius * 2 & center = pieCenterPoint
     */
    private final RectF helperRectangle = new RectF();

    /*
     * Helper points
     */
    private final Point outerTempPoint = new Point();
    private final Point innerTempPoint = new Point();

    private ClockPieSegment[] segments;

    private ClockPieSegment backgroundSegment = new ClockPieSegment(0, 0, 24, 0);

    public ClockPieView(Context context) {
        this(context, null);
    }

    public ClockPieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        textSize = MyUtils.sp2px(context, 15);
        lineThickness = MyUtils.dip2px(context, 1);
        lineLength = MyUtils.dip2px(context, 10);

        textPaint = CommonPaint.getTextPaint(context);

        Rect textRect = new Rect();
        textPaint.getTextBounds("18", 0, 1, textRect);

        foregroundPaint = CommonPaint.getForegroundPaint(context);

        backgroundPaint = CommonPaint.getBackgroundPaint();
        backgroundPaint.setAlpha(CommonPaint.BACKGROUND_ALPHA * 2);

        backgroundPaint.setStrokeWidth(lineThickness);

        backgroundSegmentPaint = new Paint(backgroundPaint);
        backgroundSegmentPaint.setColor(Color.WHITE);
        backgroundSegmentPaint.setAlpha(CommonPaint.BACKGROUND_ALPHA * 2);

        leftTextWidth = textPaint.measureText("18");
        rightTextWidth = textPaint.measureText("6");
        topTextHeight = textRect.height();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setData(List<ClockPieSegment> segments) {
        setData(segments.toArray(new ClockPieSegment[0]));
    }

    public void setData(ClockPieSegment[] segments) {
        this.segments = segments;

        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    /**
     * Sets a segment used for drawing the clock's background. Times outside of this segment
     * are drawn gray.
     */
    public void setBackgroundSegment(ClockPieSegment segment) {
        this.backgroundSegment = segment;

        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        if (segments != null) {
            for (ClockPieSegment helper : segments) {
                canvas.drawArc(helperRectangle, helper.getStart(), helper.getSweep(), true, foregroundPaint);
            }
        }
    }

    private void drawBackground(Canvas canvas) {

        backgroundPaint.setStyle(Paint.Style.FILL);

        // Draw lines
        // 1 through 12
        for (int i = 0; i < 12; i++) {
            outerTempPoint.set(
                    pieCenterPoint.x
                            - (int) (Math.sin(Math.PI / 12 * i) * (pieRadius + lineLength)),
                    pieCenterPoint.y
                            - (int) (Math.cos(Math.PI / 12 * i) * (pieRadius + lineLength))
            );
            innerTempPoint.set(
                    pieCenterPoint.x
                            - (int) (Math.sin(Math.PI / 12 * i) * (pieRadius + lineThickness)),
                    pieCenterPoint.y
                            - (int) (Math.cos(Math.PI / 12 * i) * (pieRadius + lineThickness))
            );
            canvas.drawLine(outerTempPoint.x, outerTempPoint.y, innerTempPoint.x, innerTempPoint.y,
                    backgroundPaint);
        }

        // 13 through 24
        for (int i = 0; i < 12; i++) {
            outerTempPoint.set(
                    pieCenterPoint.x
                            + (int) (Math.sin(Math.PI / 12 * i) * (pieRadius + lineLength)),
                    pieCenterPoint.y
                            + (int) (Math.cos(Math.PI / 12 * i) * (pieRadius + lineLength))
            );
            innerTempPoint.set(
                    pieCenterPoint.x
                            + (int) (Math.sin(Math.PI / 12 * i) * (pieRadius + lineThickness)),
                    pieCenterPoint.y
                            + (int) (Math.cos(Math.PI / 12 * i) * (pieRadius + lineThickness))
            );
            canvas.drawLine(outerTempPoint.x, outerTempPoint.y, innerTempPoint.x, innerTempPoint.y,
                    backgroundPaint);
        }

        // gray-ish non-segment background arc
        canvas.drawArc(helperRectangle,
                backgroundSegment.getStart() + backgroundSegment.getSweep(), 360 - backgroundSegment.getSweep(), true,
                backgroundPaint);

        // white-ish background segment
        canvas.drawArc(
                helperRectangle,
                backgroundSegment.getStart(), backgroundSegment.getSweep(),
                true, backgroundSegmentPaint
        );

        // gray-ish circle around clock
        backgroundPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(pieCenterPoint.x, pieCenterPoint.y, pieRadius, backgroundPaint);

        // Don't ask. I'm glad the 0 and 12 are where they are supposed to be.
        canvas.drawText("0", pieCenterPoint.x, viewSize - 2 * pieRadius - (int) (leftTextWidth) - lineLength * 4 + topTextHeight, textPaint);
        canvas.drawText("12", pieCenterPoint.x, 2 * pieRadius + (int) (leftTextWidth) + lineLength * 4, textPaint);
        canvas.drawText("18", offset + leftTextWidth / 2, pieCenterPoint.y + topTextHeight / 2, textPaint);
        canvas.drawText("6", offset + viewSize - rightTextWidth / 2,
                pieCenterPoint.y + topTextHeight / 2, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = measureWidth(widthMeasureSpec);
        int maxHeight = measureHeight(heightMeasureSpec, maxWidth);
        viewSize = Math.min(maxWidth, maxHeight);

        offset = (maxWidth - viewSize) / 2;

        pieRadius = (viewSize - lineLength * 4 - (int) (leftTextWidth)) / 2;
        pieCenterPoint.set(maxWidth / 2 - (int) rightTextWidth / 2 + (int) leftTextWidth / 2,
                viewSize / 2 + textSize / 2 - (int) (leftTextWidth / 2));
        helperRectangle.set(pieCenterPoint.x - pieRadius, pieCenterPoint.y - pieRadius,
                pieCenterPoint.x + pieRadius, pieCenterPoint.y + pieRadius);
        setMeasuredDimension(viewSize, viewSize);
    }

    private int measureWidth(int measureSpec) {
        int preferred = 3;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec, int maxWidth) {
        int preferred = maxWidth;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int measurement;

        switch (View.MeasureSpec.getMode(measureSpec)) {
            case View.MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case View.MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }
}
