# chartDirect

This is a fork of the [AndroidCharts](https://github.com/HackPlan/AndroidCharts) library. It was created due to necessity for the [usageDirect](https://codeberg.org/fynngodau/usageDirect/) project.

It contains:

* a bar chart with condensed variant
* a clock pie chart
* an ordinary pie chart (unmaintained)
* a line chart (unmaintained)

### Features

The following may not apply to the unmaintained chart types.

* lightweight
* support for dark background
* `BarView` and `CondensedBarView` backed by `RecyclerView` for highest performance
* display different colors in charts
* accent color as default color

## Including in Your Project

Please use JitPack for downloading compiled AAR bundles. Sorry. [See how.](https://jitpack.io/#org.codeberg.fynngodau/chartDirect)

## Usage

### Bar chart

![Bar Chart](https://codeberg.org/fynngodau/chartDirect/raw/branch/main/pic/bar.png)


Include the `BarView` in your layout like this:

```xml
<im.dacer.androidcharts.bar.BarView
    android:layout_width="wrap_content"
    android:layout_height="300dp"
    android:id="@+id/bar_view" />
```

Set it up using Java:

```java
BarView barView = findViewById(R.id.bar_view);

// Construct Value object for each bar
Value[] values = new Value[]{

    // Single-colored bar
    new Value(50, "50"),

    // Multi-colored bar
    new MultiValue(
        new int[]{20, 40}, new Integer[]{Color.RED, Color.BLUE}
    )
};
barView.setData(values, 100);

// Optionally add vertical lines for scale
barView.setHorizontalLines(new Line[]{new Line(25, "25%")});

// Optionally enable zero line
barView.setZeroLineEnabled(true);

// Optionally scroll along dashed scale lines
// (need to pass windowBackground color for performance reasons)
barView.setScrollHorizontalLines(true, windowBackground)
```

A condensed variant with no margin between bars is also available – simply use `CondensedBarView` instead of `BarView`.

### Clock Pie Chart

![Clock Pie Chart](https://codeberg.org/fynngodau/chartDirect/raw/branch/main/pic/pie.png)

```xml
<im.dacer.androidcharts.clockpie.ClockPieView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/clock_pie_view" />
```

```java
ClockPieView pieView = findViewById(R.id.clock_pie_view);
ClockPieSegment[] pieSegments = new ClockPieSegment[]{
    new ClockPieSegemnt(14, 30, 17, 45); // from 14:30 until 17:45
}
pieView.setData(pieSegments);
```

You may also set a segment that is displayed as a background segment.

### Deprecated classes

For usage instructions on the deprecated classes, please refer to [the AndroidCharts README](https://github.com/HackPlan/AndroidCharts#line-chart).

## Contributing

Any contributions, large or small, major features, bug fixes, and so forth, are welcome.

You are encouraged to utilize the issue tracker to discuss new ideas before working on them.

## Comparison to original AndroidCharts library

| View | code quality improvement status | new features | other improvements | original example screenshot
|---|---|---|---|---|
| `LineView` | unchanged | no new features | none | ![Line Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/line.png)
| `BarView` | **significantly improved** | background lines for scale (including labels), support for bold / italic labels, zero line (optional), condensed variant (`CondensedBarView`), multiple colors in one bar (`MultiValue`), backed by `RecyclerView` | fix missing margins, color set to accent color, support for dark background | ![Bar Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/bar.png)
| `ClockPieView` | **improved** | gray out background partially (e.g. to display time as not passed yet), colored segments | support for drawing in views with larger width than height, support for drawing when view is set to `match_parent`, color set to accent color, remove animation as it was too slow, support for dark background | ![Clock Pie Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/pie.png)
| `PieView` | unchanged | no new features | none | ![Pie Chart](https://raw.github.com/dacer/AndroidCharts/master/pic/pie2.png)



## License

This library is licensed under the MIT license.

The license text can be seen below.

---

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

---


If you are using [librariesDirect](), you may use this snippet to generate a `Library` class for this library:

    new Library("chartDirect", License.MIT_LICENSE, "The MIT License (MIT)\n" +
        "\n" +
        "Copyright (c) 2020 Fynn Godau\n" +
        "\n" +
        "Copyright (c) 2013 Ding Wenhao", "Fynn Godau and AndroidChart contributors", true, "https://codeberg.org/fynngodau/chartDirect"
    )