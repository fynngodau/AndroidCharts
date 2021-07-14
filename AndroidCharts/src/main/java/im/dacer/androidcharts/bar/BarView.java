package im.dacer.androidcharts.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class BarView extends FrameLayout {

    private RecyclerView recycler;
    private Adapter adapter;
    private LegendView legend;
    private SingleBarContext barContext;

    private SpaceItemDecoration spaceDecoration;

    private final Runnable animator = new Runnable() {
        @Override
        public void run() {
            if (adapter.animationStep(recycler)) {
                postDelayed(this, 20);
            }
        }
    };

    public BarView(Context context) {
        super(context);
        init();
    }

    public BarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        barContext = new SingleBarContext(getContext());
        addLegend();
        addRecycler();
    }

    private void addLegend() {
        legend = new LegendView(getContext(), barContext);
        addView(legend);
    }

    private void addRecycler() {
        recycler = new RecyclerView(getContext());
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recycler.addItemDecoration(spaceDecoration = new SpaceItemDecoration(barContext.barSideMargin));

        recycler.setAdapter(adapter = new Adapter(barContext));
        recycler.setPadding(0, barContext.topMargin, 0, 0);

        recycler.setItemViewCacheSize(10);

        // Attach legend
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int x = recyclerView.computeHorizontalScrollOffset();
                legend.setScrolledX(x);
            }
        });

        addView(recycler);
    }

    public void setData(Value[] values) {
        setData(values, 0);
    }

    /**
     * @param max The top border of the chart, or 0 to use highest value
     */
    public void setData(Value[] values, int max) {

        int highestValue = Collections.max(Arrays.asList(values), new Comparator<Value>() {
            @Override
            public int compare(Value x, Value y) {
                return (x.getValue() < y.getValue()) ? -1 : ((x.getValue() == y.getValue()) ? 0 : 1);
            }
        }).getValue();

        if (max < highestValue) {
            if (max != 0) {
                Log.w("BarView", "Inappropriate max! Using highest value as max instead. If you meant to do that, pass 0 to remove this warning.");
            }
            max = highestValue;
        }

        adapter.setData(values, max);

        // Stop ongoing animation
        removeCallbacks(animator);

        // Start new animation
        post(animator);
    }

    public void setVerticalLines(Line[] lines, int max) {
        legend.setLines(lines, max);
        recycler.setPadding(barContext.lineLabelWidth + 2 * barContext.textMargin, barContext.topMargin, 0, 0);
    }

    public void setZeroLineEnabled(boolean enabled) {
        recycler.removeItemDecoration(spaceDecoration);

        recycler.addItemDecoration(
                spaceDecoration = enabled?
                        new SpaceItemDecoration(barContext.barSideMargin)
                        : new SpaceItemDecoration.ZeroLineDecoration(barContext)
        );
    }

    public void scrollToEnd() {
        recycler.scrollToPosition(adapter.getItemCount() - 1);
    }

}
