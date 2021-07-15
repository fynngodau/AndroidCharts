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
    protected CondensedSingleBarContext getBarContext() {
        return new CondensedSingleBarContext(getContext());
    }

    @Override
    protected LabelItemDecoration<? extends SingleBarContext> getLabelItemDecoration(SingleBarContext barContext) {
        return new CondensedBarLabelItemDecoration((CondensedSingleBarContext) barContext);
    }

    public void setBarWidth(int dp) {
        barContext.barWidth = MyUtils.dip2px(getContext(), dp);
    }

    public void setLabelIndicatorMode(LabelIndicatorMode labelIndicatorMode) {
        ((CondensedBarLabelItemDecoration) labelItemDecoration).setLabelIndicatorMode(labelIndicatorMode);
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
