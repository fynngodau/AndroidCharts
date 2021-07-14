package im.dacer.androidcharts.bar;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import godau.fynn.typedrecyclerview.SimpleRecyclerViewAdapter;
import godau.fynn.typedrecyclerview.PrimitiveViewHolder;

public class Adapter extends SimpleRecyclerViewAdapter<Bar, PrimitiveViewHolder<SingleBarView>> {

    private final SingleBarContext barContext;

    public Adapter(SingleBarContext barContext) {
        this.barContext = barContext;
    }


    @NonNull
    @Override
    public PrimitiveViewHolder<SingleBarView> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrimitiveViewHolder<>(new SingleBarView(context, barContext));
    }

    @Override
    public void onBindViewHolder(@NonNull PrimitiveViewHolder<SingleBarView> holder, Bar item, int position) {
        holder.itemView.setBar(item);
        holder.itemView.invalidate();
    }


    public void setData(Value[] values, int max) {

        for (int i = 0; i < content.size() && i < values.length; i++) {
            // Set new data to old bar
            content.get(i).setValue(values[i], max);
        }

        // Positive if more new values than bars (→ add new bars), negative if fewer (→ remove bars)
        int diff = values.length - content.size();

        // Add new bars if more new values than bars
        if (diff > 0) {
            for (int i = content.size(); i < values.length; i++) {
                Bar bar = new Bar();
                bar.setValue(values[i], max);
                content.add(bar);
            }

            notifyItemRangeInserted(values.length - diff, diff);
        }

        // Remove bars if fewer new values than bars
        if (diff < 0) {
            content.subList(values.length, content.size()).clear();
            notifyItemRangeRemoved(values.length, -diff);

        }

        barContext.updateValueLabelMeasurements(values);

    }

    public boolean animationStep(RecyclerView recyclerView) {

        boolean needNewFrame = false;
        for (Bar bar : content) {
            needNewFrame = bar.animationStep() | needNewFrame;
        }

        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).invalidate();
        }

        return needNewFrame;
    }
}
