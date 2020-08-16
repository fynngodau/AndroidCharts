package im.dacer.androidcharts.bar;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import im.dacer.androidcharts.CommonPaint;
import im.dacer.androidcharts.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dacer on 11/11/13.
 */
public class BarView extends View {

    /**
     * Minimum bar width
     */
    private final int MIN_BAR_WIDTH;
    /**
     * Margin to the left and right of each bar
     */
    private final int BAR_SIDE_MARGIN;
    /**
     * Margin around text
     */
    private final int TEXT_MARGIN;

    private static final int BACKGROUND_COLOR = Color.parseColor("#F6F6F6");
    private static final int FOREGROUND_COLOR = Color.parseColor("#FC496D");

    private final List<Float> percentList;
    private float[] targetPercentList;

    private final Paint textPaint;
    private final Paint bgPaint;
    private final Paint fgPaint;

    private final Rect rect;
    private int barWidth;
    private int bottomTextDescent;

    private final int topMargin;
    private int leftMargin;

    private int bottomTextHeight;
    private int labelTextHeight;

    private List<String> bottomTextList = new ArrayList<>();
    private List<Typeface> typefaces = new ArrayList<>();
    private List<Float> verticalLines = new ArrayList<>();
    private List<String> verticalLineLabels = new ArrayList<>();

    private final Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            for (int i = 0; i < targetPercentList.length; i++) {
                if (percentList.get(i) < targetPercentList[i]) {
                    percentList.set(i, percentList.get(i) + 0.02f);
                    needNewFrame = true;
                } else if (percentList.get(i) > targetPercentList[i]) {
                    percentList.set(i, percentList.get(i) - 0.02f);
                    needNewFrame = true;
                }
                if (Math.abs(targetPercentList[i] - percentList.get(i)) < 0.02f) {
                    percentList.set(i, targetPercentList[i]);
                }
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

        fgPaint = new Paint(bgPaint);
        fgPaint.setColor(FOREGROUND_COLOR);

        rect = new Rect();

        topMargin = MyUtils.dip2px(context, 5);
        barWidth = MyUtils.dip2px(context, 22);
        MIN_BAR_WIDTH = MyUtils.dip2px(context, 22);
        BAR_SIDE_MARGIN = MyUtils.dip2px(context, 22);
        TEXT_MARGIN = MyUtils.dip2px(context, 5);

        textPaint = CommonPaint.getTextPaint(context);
        percentList = new ArrayList<>();
    }

    /**
     * Set list of bar labels
     *
     * @param bottomStringList An ordered list of Strings
     */
    public void setBottomTextList(List<String> bottomStringList) {
        //        this.dataList = null;
        this.bottomTextList = bottomStringList;
        Rect r = new Rect();
        bottomTextDescent = 0;
        barWidth = MIN_BAR_WIDTH;
        for (String s : bottomTextList) {
            textPaint.getTextBounds(s, 0, s.length(), r);
            if (bottomTextHeight < r.height()) {
                bottomTextHeight = r.height();
            }
            if (barWidth < r.width()) {
                barWidth = r.width();
            }
            if (bottomTextDescent < (Math.abs(r.bottom))) {
                bottomTextDescent = Math.abs(r.bottom);
            }
        }
        setMinimumWidth(2);
        postInvalidate();
    }

    /**
     * @param typefaces An ordered list of typefaces
     */
    public void setTypefaces(List<Typeface> typefaces) {
        this.typefaces = typefaces;

        postInvalidate();
    }

    /**
     * Sets typefaces to Typeface.DEFAULT except for the position-th typeface,
     * which will be set to Typeface.DEFAULT_BOLD
     *
     * @param position Position in bottom text list which should be bolded
     */
    public void setBoldPosition(int position) {
        typefaces.clear();
        for (int i = 0; i <= position + 1; i++) {
            if (i == position) {
                typefaces.add(Typeface.DEFAULT_BOLD);
            } else {
                typefaces.add(Typeface.DEFAULT);
            }
        }

        postInvalidate();
    }

    /**
     * Draw vertical background lines to these values if max is the maximum
     */
    public void setVerticalLines(List<Integer> values, int max) {

        verticalLines.clear();

        for (Integer value : values) {
            verticalLines.add((float) value / (float) max);
        }

        postInvalidate();
    }

    /**
     * Draw vertical background lines to these percentages of the maximum
     */
    public void setVerticalLinesByPercentage(List<Float> percentages) {
        this.verticalLines = percentages;

        postInvalidate();
    }

    /**
     * Set labels for vertical lines, which can function as a scale or legend for the chart.
     *
     * @param values Lables in the order of the values passed to {@link #setVerticalLines(List, int)}
     */
    public void setVerticalLineLabels(List<String> values) {
        verticalLineLabels = values;

        Rect r = new Rect();
        leftMargin = 0;
        for (String s : verticalLineLabels) {
            textPaint.getTextBounds(s, 0, s.length(), r);
            if (leftMargin < r.width()) {
                leftMargin = r.width();
            }
            if (labelTextHeight < r.height()) {
                labelTextHeight = r.height();
            }
        }

        postInvalidate();
    }

    /**
     * @param list The List of Integer with the range of [0-max].
     */
    public void setDataList(List<Value> list, int max) {
        targetPercentList = new float[list.size()];
        if (max == 0) max = 1;

        for (int i = 0; i < list.size(); i++) {
            targetPercentList[i] = (list.get(i).getPercentage(max));
        }

        // Make sure percentList.size() == targetPercentList.size()
        if (percentList.isEmpty() || percentList.size() < targetPercentList.length) {
            int temp = targetPercentList.length - percentList.size();
            for (int i = 0; i < temp; i++) {
                percentList.add(1f);
            }
        } else if (percentList.size() > targetPercentList.length) {
            int temp = percentList.size() - targetPercentList.length;
            for (int i = 0; i < temp; i++) {
                percentList.remove(percentList.size() - 1);
            }
        }
        setMinimumWidth(2);
        removeCallbacks(animator);
        post(animator);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Draw vertical background lines

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(MyUtils.dip2px(getContext(), 1f));
        paint.setColor(CommonPaint.BACKGROUND_LINE_COLOR);

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.DEFAULT);

        Path path = new Path();
        for (int i = 0; i < verticalLines.size(); i++) {

            int y = topMargin + (int) ((getHeight()
                    - topMargin
                    - bottomTextHeight
                    - TEXT_MARGIN) * (1f - verticalLines.get(i)));

            if (verticalLineLabels.size() > i) {
                canvas.drawText(verticalLineLabels.get(i), TEXT_MARGIN, y + labelTextHeight + TEXT_MARGIN, textPaint);
            }

            path.moveTo(0, y);
            path.lineTo(getWidth(), y);
            canvas.drawPath(path, paint);
        }

        textPaint.setTextAlign(Paint.Align.CENTER);

        // Draw bars
        int i = 1;
        if (percentList != null && !percentList.isEmpty()) {
            for (Float f : percentList) {
                rect.set(leftMargin + BAR_SIDE_MARGIN * i + barWidth * (i - 1), topMargin,
                        leftMargin + (BAR_SIDE_MARGIN + barWidth) * i,
                        getHeight() - bottomTextHeight - TEXT_MARGIN);
                canvas.drawRect(rect, bgPaint);
                /*rect.set(BAR_SIDE_MARGIN*i+barWidth*(i-1),
                        topMargin+(int)((getHeight()-topMargin)*percentList.get(i-1)),
                        (BAR_SIDE_MARGIN+barWidth)* i,
                        getHeight()-bottomTextHeight-TEXT_TOP_MARGIN);*/
                /**
                 * The correct total height is "getHeight()-topMargin-bottomTextHeight-TEXT_TOP_MARGIN",not "getHeight()-topMargin".
                 * fix by zhenghuiy@gmail.com on 11/11/13.
                 */
                rect.set(leftMargin + BAR_SIDE_MARGIN * i + barWidth * (i - 1), topMargin + (int) ((getHeight()
                                - topMargin
                                - bottomTextHeight
                                - TEXT_MARGIN) * percentList.get(i - 1)),
                        leftMargin + (BAR_SIDE_MARGIN + barWidth) * i,
                        getHeight() - bottomTextHeight - TEXT_MARGIN);
                canvas.drawRect(rect, fgPaint);
                i++;
            }
        }

        if (bottomTextList != null && !bottomTextList.isEmpty()) {
            i = 1;
            for (int j = 0; j < bottomTextList.size(); j++) {
                String s = bottomTextList.get(j);

                if (typefaces.size() > j) {
                    textPaint.setTypeface(typefaces.get(j));
                }

                canvas.drawText(s, leftMargin + BAR_SIDE_MARGIN * i + barWidth * (i - 1) + barWidth / 2,
                        getHeight() - bottomTextDescent, textPaint);
                i++;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mViewWidth = measureWidth(widthMeasureSpec);
        int mViewHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private int measureWidth(int measureSpec) {
        int preferred = 0;
        if (bottomTextList != null) {
            preferred = bottomTextList.size() * (barWidth + BAR_SIDE_MARGIN) + BAR_SIDE_MARGIN + leftMargin;
        }
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec) {
        int preferred = 222;
        return getMeasurement(measureSpec, preferred);
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
