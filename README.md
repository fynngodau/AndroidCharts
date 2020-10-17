# AndroidCharts

This is a fork of the [AndroidCharts](https://github.com/HackPlan/AndroidCharts) library.

### Improvement status

| View | code quality improvement status | new features | other improvements | original example screenshot
|---|---|---|---|---|
| `LineView` | unchanged | no new features | none | ![Line Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/line.png)
| `BarView` | **significantly improved** | background lines for scale (including labels), support for bold / italic labels, zero line (optional) | fix missing margins, color set to accent color | ![Bar Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/bar.png)
| `ClockPieView` | **improved** | gray out background partially (e.g. to display time as not passed yet) | support for drawing in views with larger width than height, support for drawing when view is set to `match_parent`, color set to accent color, remove animation as it was too slow | ![Clock Pie Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/pie.png)
| `PieView` | unchanged | no new features | none | ![Pie Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/pie2.png)

Note: no new screenshots were taken. The clock pie view will have more intense colors.

## Including in Your Project

Please use JitPack for downloading compiled AAR bundles. Sorry. [See how.](https://jitpack.io/#fynngodau/AndroidCharts/)

## Usage

#### Line Chart

![Line Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/line.png)

```xml
<HorizontalScrollView>
        <im.dacer.androidcharts.line.LineView
            android:layout_width="wrap_content"
            android:layout_height="300dp"
            android:id="@+id/line_view" />
</HorizontalScrollView>
```

```java
LineView lineView = findViewById(R.id.line_view);
lineView.setDrawDotLine(false); //optional
lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
lineView.setBottomTextList(strList);
lineView.setColorArray(new int[]{Color.BLACK,Color.GREEN,Color.GRAY,Color.CYAN});
lineView.setDataList(dataLists); //or lineView.setFloatDataList(floatDataLists)
```

#### Bar Chart

![Bar Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/bar.png)

```xml
<HorizontalScrollView>
        <im.dacer.androidcharts.bar.BarView
            android:layout_width="wrap_content"
            android:layout_height="300dp"
            android:id="@+id/bar_view" />
</HorizontalScrollView>
```

```java
BarView barView = findViewById(R.id.bar_view);

// Construct Value object for each bar
Value[] values = new Value[]{
    new Value(50, "50")
};
barView.setData(values, 100);

// Optionally add vertical lines for scale
barView.setLines(new Line[]{new Line(25, "25%")});
```

#### Clock Pie Chart

![Clock Pie Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/pie.png)

```xml
<im.dacer.androidcharts.clockpie.ClockPieView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/clock_pie_view" />
```

```java
ClockPieView pieView = findViewById(R.id.clock_pie_view);
ArrayList<ClockPieSegment> pieSegments = new ArrayList<ClockPieHelper>();
pieView.setData(pieSegments);
```

#### Pie Chart

![Pie Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/pie2.png)

```xml
<im.dacer.androidcharts.pie.PieView
    android:layout_width="300dp"
    android:layout_height="wrap_content"
    android:id="@+id/pie_view" />
```

```java
PieView pieView = findViewById(R.id.pie_view);
ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
pieView.setDate(pieHelperArrayList);
pieView.selectedPie(2); //optional
pieView.setOnPieClickListener(listener) //optional
pieView.showPercentLabel(false); //optional
```

## License

The MIT License (MIT)

Copyright (c) 2020 Fynn Godau

Copyright (c) 2013 Ding Wenhao

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


## Contributing

Please fork this repository and contribute back using
[pull requests](https://github.com/github/android/pulls).

Any contributions, large or small, major features, bug fixes, additional
language translations, unit/integration tests are welcomed
