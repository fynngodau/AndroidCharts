package com.dacer.androidchartsexample;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.LayoutRes;
import im.dacer.androidcharts.bar.*;
import im.dacer.androidcharts.bar.BarView;

/**
 * Created by Dacer on 11/15/13.
 */
public class RecyclerBarFragment extends Fragment {
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayout(), container, false);
        final BarView barView = rootView.findViewById(R.id.bar_view);
        Button button = (Button) rootView.findViewById(R.id.bar_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                randomSet(barView);
            }
        });
        randomSet(barView);
        return rootView;
    }

    protected @LayoutRes int getLayout() {
        return R.layout.fragment_bar_recycle;
    }

    protected void randomSet(BarView barView) {
        int random = (int) (Math.random() * 40) + 300;

        Value[] values = new Value[random];
        for (int i = 0; i < random; i += 2) {
            values[i] = new Value((int) (Math.random() * 100), String.valueOf(i + 1));
        }
        for (int i = 1; i < random; i += 2) {
            values[i] = new MultiValue(
                    new float[]{0.5f, 0.5f},
                    (int) (Math.random() * 100),
                    new Integer[]{Color.BLUE, Color.RED},
                    String.valueOf(i + 1)
            );
        }
        barView.setData(values, 100);

        //barView.setBoldPosition(5);

        // Draw vertical lines aligning with 3 bars
        Line[] lines = new Line[3];
        for (int i = 0; i < 3; i++) {
            int randomPosition = (int) (Math.random() * random);
            lines[i] = new Line(values[randomPosition].getValue(), String.valueOf(values[randomPosition].getValue()));
        }
        barView.setVerticalLines(lines, 100);

        barView.setZeroLineEnabled(Math.random() > 0.5);
    }
}