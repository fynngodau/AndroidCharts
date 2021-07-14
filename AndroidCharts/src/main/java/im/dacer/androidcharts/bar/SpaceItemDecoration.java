package im.dacer.androidcharts.bar;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    protected final int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = space;
        }
        outRect.right = space;
    }

    public static class ZeroLineDecoration extends SpaceItemDecoration {

        private final SingleBarContext barContext;

        public ZeroLineDecoration(SingleBarContext barContext) {
            super(barContext.barSideMargin);
            this.barContext = barContext;
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            int y = parent.getHeight() - barContext.valueLabelHeight;

            for (int i = 0; i < parent.getChildCount(); i++) {

                View view = parent.getChildAt(i);

                int position = parent.getChildLayoutPosition(view);

                int xRight = position == parent.getAdapter().getItemCount() - 1?
                        Math.max(parent.getPaddingLeft(), view.getRight()) :
                        Math.max(parent.getPaddingLeft(), view.getRight() + space);

                c.drawLine(
                        Math.max(parent.getPaddingLeft(), view.getLeft()),
                        y,
                        xRight,
                        y,
                        barContext.linePaint
                );
            }
        }
    }
}
