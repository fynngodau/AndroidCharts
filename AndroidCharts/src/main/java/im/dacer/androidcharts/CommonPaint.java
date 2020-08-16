package im.dacer.androidcharts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

public class CommonPaint {

    private static final int TEXT_COLOR = Color.parseColor("#9B9A9B");
    private static final int FOREGROUND_COLOR = Color.parseColor("#FC496D");

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

    public static Paint getForegroundPaint() {

        Paint foregroundPaint = new Paint();
        foregroundPaint.setAntiAlias(true);
        foregroundPaint.setColor(FOREGROUND_COLOR);

        return foregroundPaint;
    }
}
