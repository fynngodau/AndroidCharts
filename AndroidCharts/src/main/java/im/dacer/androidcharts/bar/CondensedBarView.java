package im.dacer.androidcharts.bar;

import android.content.Context;
import android.util.AttributeSet;
import im.dacer.androidcharts.MyUtils;

public class CondensedBarView extends BarView {
    public CondensedBarView(Context context) {
        super(context);
    }

    public CondensedBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CondensedBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected SingleBarContext getBarContext() {
        return new CondensedSingleBarContext(getContext());
    }

    public void setBarWidth(int dp) {
        barContext.barWidth = MyUtils.dip2px(getContext(), dp);
    }
}
