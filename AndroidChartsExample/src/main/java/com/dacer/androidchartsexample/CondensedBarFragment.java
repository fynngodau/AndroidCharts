package com.dacer.androidchartsexample;

import im.dacer.androidcharts.bar.BarView;
import im.dacer.androidcharts.bar.CondensedBarView;
import im.dacer.androidcharts.bar.Value;

public class CondensedBarFragment extends BarFragment {

    int mode = 1;

    @Override
    protected int getLayout() {
        return R.layout.fragment_bar_condensed;
    }

    @Override
    protected void randomSet(BarView barView) {
        int amount = 156;

        ((CondensedBarView) barView).setBarWidth(8);

        Value[] values = new Value[amount];
        for (int i = 0; i < amount; i++) {
            if (i % 8 == 0) {
                values[i] = new Value((int) (Math.random() * 100), String.valueOf(i + 1));
            } else {
                values[i] = new Value((int) (Math.random() * 100));
            }
        }
        barView.setData(values, 100);

        //barView.setBoldPosition(5);

        // Draw vertical lines aligning with 3 bars
        /*Line[] lines = new Line[3];
        for (int i = 0; i < 3; i++) {
            int randomPosition = (int) (Math.random() * amount);
            lines[i] = new Line(values[randomPosition].getValue(), String.valueOf(values[randomPosition].getValue()));
        }
        barView.setVerticalLines(lines, 100);*/

        ((CondensedBarView) barView).setLabelIndicatorMode(CondensedBarView.LabelIndicatorMode.values()[mode++ % 3]);
    }
}
