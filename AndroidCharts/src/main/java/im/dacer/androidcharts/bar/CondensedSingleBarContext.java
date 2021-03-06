package im.dacer.androidcharts.bar;

import android.content.Context;
import im.dacer.androidcharts.CommonPaint;
import im.dacer.androidcharts.MyUtils;

class CondensedSingleBarContext extends SingleBarContext {

    final int textLeftMargin;

    protected CondensedSingleBarContext(Context context) {
        super(
                MyUtils.dip2px(context, 4),
                MyUtils.dip2px(context, 8),
                MyUtils.dip2px(context, 5),
                CommonPaint.getTextPaint(context),
                CommonPaint.getBackgroundPaint(),
                CommonPaint.getForegroundPaint(context),
                CommonPaint.getForegroundLinePaint(context),
                CommonPaint.getDashedForegroundLinePaint(context),
                false
        );

        barSideMargin = 0;
        barWidth = minBarWidth;

        textLeftMargin = MyUtils.dip2px(context, 4);
    }
}
