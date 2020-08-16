package im.dacer.androidcharts;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Dacer on 11/13/13.
 */
public class ClockPieView extends View {

    private static final int GRAY_COLOR = Color.parseColor("#D4D3D4");

    private final Paint textPaint;
    private final Paint foregroundPaint;
    private final Paint linePaint;
    private final Paint whitePaint;

    private int mViewWidth;
    private int mViewHeight;
    private final int textSize;
    private int pieRadius;

    private final Point pieCenterPoint;
    private final Point tempPoint;
    private final Point tempPointRight;

    private final int lineLength;
    private final int lineThickness;
    private final float leftTextWidth;
    private final float rightTextWidth;
    private final float topTextHeight;

    private final RectF cirRect;

    private ClockPieSegment[] segments;

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

        foregroundPaint = CommonPaint.getForegroundPaint();

        linePaint = new Paint(textPaint);
        linePaint.setColor(GRAY_COLOR);
        linePaint.setStrokeWidth(lineThickness);

        whitePaint = new Paint(linePaint);
        whitePaint.setColor(Color.WHITE);
        tempPoint = new Point();
        pieCenterPoint = new Point();
        tempPointRight = new Point();
        cirRect = new RectF();
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

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        if (segments != null) {
            for (ClockPieSegment helper : segments) {
                canvas.drawArc(cirRect, helper.getStart(), helper.getSweep(), true, foregroundPaint);
            }
        }
    }

    private void drawBackground(Canvas canvas) {
        for (int i = 0; i < 12; i++) {
            tempPoint.set(pieCenterPoint.x - (int) (Math.sin(Math.PI / 12 * i) * (pieRadius
                    + lineLength)), pieCenterPoint.y - (int) (Math.cos(Math.PI / 12 * i) * (
                    pieRadius
                            + lineLength)));
            tempPointRight.set(pieCenterPoint.x + (int) (Math.sin(Math.PI / 12 * i) * (pieRadius
                    + lineLength)), pieCenterPoint.y + (int) (Math.cos(Math.PI / 12 * i) * (
                    pieRadius
                            + lineLength)));
            canvas.drawLine(tempPoint.x, tempPoint.y, tempPointRight.x, tempPointRight.y,
                    linePaint);
        }
        canvas.drawCircle(pieCenterPoint.x, pieCenterPoint.y, pieRadius + lineLength / 2,
                whitePaint);
        canvas.drawCircle(pieCenterPoint.x, pieCenterPoint.y, pieRadius + lineThickness, linePaint);
        canvas.drawCircle(pieCenterPoint.x, pieCenterPoint.y, pieRadius, whitePaint);
        canvas.drawText("0", pieCenterPoint.x, topTextHeight, textPaint);
        canvas.drawText("12", pieCenterPoint.x, mViewHeight, textPaint);
        canvas.drawText("18", leftTextWidth / 2, pieCenterPoint.y + topTextHeight / 2,
                textPaint);
        canvas.drawText("6", mViewWidth - rightTextWidth / 2,
                pieCenterPoint.y + topTextHeight / 2, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        pieRadius = mViewWidth / 2 - lineLength * 2 - (int) (textPaint.measureText("18") / 2);
        pieCenterPoint.set(mViewWidth / 2 - (int) rightTextWidth / 2 + (int) leftTextWidth / 2,
                mViewHeight / 2 + textSize / 2 - (int) (textPaint.measureText("18") / 2));
        cirRect.set(pieCenterPoint.x - pieRadius, pieCenterPoint.y - pieRadius,
                pieCenterPoint.x + pieRadius, pieCenterPoint.y + pieRadius);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private int measureWidth(int measureSpec) {
        int preferred = 3;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec) {
        int preferred = mViewWidth;
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
