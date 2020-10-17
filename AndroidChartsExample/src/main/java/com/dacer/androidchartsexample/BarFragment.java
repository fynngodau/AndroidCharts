package com.dacer.androidchartsexample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.LayoutRes;
import im.dacer.androidcharts.bar.BarView;
import im.dacer.androidcharts.bar.Line;
import im.dacer.androidcharts.bar.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dacer on 11/15/13.
 */
public class BarFragment extends Fragment {
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayout(), container, false);
        final BarView barView = (BarView) rootView.findViewById(R.id.bar_view);
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
        return R.layout.fragment_bar;
    }

    protected void randomSet(BarView barView) {
        int random = (int) (Math.random() * 40) + 10;

        Value[] values = new Value[random];
        for (int i = 0; i < random; i++) {
            values[i] = new Value((int) (Math.random() * 100), String.valueOf(i + 1));
        }
        barView.setData(values, 100);

        barView.setBoldPosition(5);

        // Draw vertical lines aligning with 3 bars
        Line[] lines = new Line[3];
        for (int i = 0; i < 3; i++) {
            int randomPosition = (int) (Math.random() * random);
            lines[i] = new Line(values[randomPosition].getValue(), String.valueOf(values[randomPosition].getValue()));
        }
        barView.setVerticalLines(lines, 100);
    }
}