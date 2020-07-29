package im.dacer.androidcharts;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dacer on 11/11/13.
 */
public class BarView extends View {
    private final int MINI_BAR_WIDTH;
    private final int BAR_SIDE_MARGIN;
    private final int TEXT_TOP_MARGIN;
    private final int TEXT_COLOR = Color.parseColor("#9B9A9B");
    private final int BACKGROUND_COLOR = Color.parseColor("#F6F6F6");
    private final int FOREGROUND_COLOR = Color.parseColor("#FC496D");
    private List<Float> percentList;
    private List<Float> targetPercentList;
    private Paint textPaint;
    private Paint bgPaint;
    private Paint fgPaint;
    private Rect rect;
    private int barWidth;
    private int bottomTextDescent;
    private boolean autoSetWidth = true;
    private int topMargin;
    private int leftMargin;
    private int bottomTextHeight;
    private List<String> bottomTextList = new ArrayList<String>();
    private final int textSize;
    private List<Typeface> typefaces = new ArrayList<>();
    private List<Float> verticalLines = new ArrayList<>();
    private List<String> verticalLineLabels = new ArrayList<>();
    private Runnable animator = new Runnable() {
        @Override public void run() {
            boolean needNewFrame = false;
            for (int i = 0; i < targetPercentList.size(); i++) {
                if (percentList.get(i) < targetPercentList.get(i)) {
                    percentList.set(i, percentList.get(i) + 0.02f);
                    needNewFrame = true;
                } else if (percentList.get(i) > targetPercentList.get(i)) {
                    percentList.set(i, percentList.get(i) - 0.02f);
                    needNewFrame = true;
                }
                if (Math.abs(targetPercentList.get(i) - percentList.get(i)) < 0.02f) {
                    percentList.set(i, targetPercentList.get(i));
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
        textSize = MyUtils.sp2px(context, 15);
        barWidth = MyUtils.dip2px(context, 22);
        MINI_BAR_WIDTH = MyUtils.dip2px(context, 22);
        BAR_SIDE_MARGIN = MyUtils.dip2px(context, 22);
        TEXT_TOP_MARGIN = MyUtils.dip2px(context, 5);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        percentList = new ArrayList<Float>();
    }

    /**
     * dataList will be reset when called is method.
     *
     * @param bottomStringList The String List in the bottom.
     */
    public void setBottomTextList(List<String> bottomStringList) {
        //        this.dataList = null;
        this.bottomTextList = bottomStringList;
        Rect r = new Rect();
        bottomTextDescent = 0;
        barWidth = MINI_BAR_WIDTH;
        for (String s : bottomTextList) {
            textPaint.getTextBounds(s, 0, s.length(), r);
            if (bottomTextHeight < r.height()) {
                bottomTextHeight = r.height();
            }
            if (autoSetWidth && (barWidth < r.width())) {
                barWidth = r.width();
            }
            if (bottomTextDescent < (Math.abs(r.bottom))) {
                bottomTextDescent = Math.abs(r.bottom);
            }
        }
        setMinimumWidth(2);
        postInvalidate();
    }

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
        }

        postInvalidate();
    }

    /**
     * @param list The List of Integer with the range of [0-max].
     */
    public void setDataList(List<Integer> list, int max) {
        targetPercentList = new ArrayList<Float>();
        if (max == 0) max = 1;

        for (Integer integer : list) {
            targetPercentList.add(1 - (float) integer / (float) max);
        }

        // Make sure percentList.size() == targetPercentList.size()
        if (percentList.isEmpty() || percentList.size() < targetPercentList.size()) {
            int temp = targetPercentList.size() - percentList.size();
            for (int i = 0; i < temp; i++) {
                percentList.add(1f);
            }
        } else if (percentList.size() > targetPercentList.size()) {
            int temp = percentList.size() - targetPercentList.size();
            for (int i = 0; i < temp; i++) {
                percentList.remove(percentList.size() - 1);
            }
        }
        setMinimumWidth(2);
        removeCallbacks(animator);
        post(animator);
    }

    @Override protected void onDraw(Canvas canvas) {

        // Draw vertical background lines

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(MyUtils.dip2px(getContext(), 1f));
        paint.setColor(LineView.BACKGROUND_LINE_COLOR);

        textPaint.setTextAlign(Paint.Align.LEFT);

        Path path = new Path();
        for (int i = 0; i < verticalLines.size(); i++) {

            int y = topMargin + (int) ((getHeight()
                    - topMargin
                    - bottomTextHeight
                    - TEXT_TOP_MARGIN) * (1f - verticalLines.get(i)));

            if (verticalLineLabels.size() > i) {
                canvas.drawText(verticalLineLabels.get(i), TEXT_TOP_MARGIN, y + textSize + TEXT_TOP_MARGIN, textPaint);
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
                        getHeight() - bottomTextHeight - TEXT_TOP_MARGIN);
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
                                - TEXT_TOP_MARGIN) * percentList.get(i - 1)),
                        leftMargin + (BAR_SIDE_MARGIN + barWidth) * i,
                        getHeight() - bottomTextHeight - TEXT_TOP_MARGIN);
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

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
