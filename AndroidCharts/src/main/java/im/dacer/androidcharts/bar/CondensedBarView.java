package im.dacer.androidcharts.bar;

import android.content.Context;
import android.util.AttributeSet;
import im.dacer.androidcharts.MyUtils;

public class CondensedBarView extends BarView {

    private CondensedBarViewItemDecoration labelItemDecoration;

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
    protected CondensedSingleBarContext getBarContext() {
        return new CondensedSingleBarContext(getContext());
    }

    @Override
    protected void init() {
        super.init();

        recycler.addItemDecoration(labelItemDecoration = new CondensedBarViewItemDecoration(getBarContext()));
    }

    public void setBarWidth(int dp) {
        barContext.barWidth = MyUtils.dip2px(getContext(), dp);
    }

    @Override
    public void setData(Value[] values, int max) {

        labelItemDecoration.setValues(values);
        super.setData(values, max);
    }

    public void setLabelIndicatorMode(LabelIndicatorMode labelIndicatorMode) {
        labelItemDecoration.setLabelIndicatorMode(labelIndicatorMode);
        recycler.invalidateItemDecorations();
    }

    public enum LabelIndicatorMode {
        /**
         * Do not draw label indicators
         */
        HIDDEN,
        /**
         * Draw label indicators below the chart
         */
        BELOW_CHART,
        /**
         * Draw label indicators below as well as through the chart
         */
        IN_CHART
    }
}
