package im.dacer.androidcharts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.TypedValue;

public class CommonPaint {

    private static final int TEXT_COLOR = Color.parseColor("#9B9A9B");
    private static final int DEFAULT_FOREGROUND_COLOR = Color.parseColor("#FC496D");

    public static final int BACKGROUND_LINE_COLOR = Color.parseColor("#EEEEEE");

    public static Paint getTextPaint(Context context) {

        int textSize = MyUtils.sp2px(context, 15);

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        return textPaint;
    }

    public static Paint getForegroundPaint(Context context) {

        Paint foregroundPaint = new Paint();
        foregroundPaint.setAntiAlias(true);
        foregroundPaint.setColor(getThemeAccentColor(context));

        return foregroundPaint;
    }

    public static Paint getBackgroundLinePaint(Context context) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(MyUtils.dip2px(context, 1f));
        paint.setColor(CommonPaint.BACKGROUND_LINE_COLOR);
        return paint;
    }

    /**
     * @see <a href="https://stackoverflow.com/a/36192770">StackOverflow</a>
     * @return The accent color from the currently set theme
     */
    private static int getThemeAccentColor(Context context) {
        int colorAttr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAttr = android.R.attr.colorAccent;
        } else {
            //Get colorAccent defined for AppCompat
            colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttr, outValue, true);

        if (outValue.data != 0) {
            return outValue.data;
        } else {
            return DEFAULT_FOREGROUND_COLOR;
        }
    }
}
