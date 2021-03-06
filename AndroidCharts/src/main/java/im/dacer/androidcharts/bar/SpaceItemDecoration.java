package im.dacer.androidcharts.bar;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    protected final int space;

    SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = space;
        }
        outRect.right = space;
    }

    static class ZeroLineDecoration extends SpaceItemDecoration {

        private final SingleBarContext barContext;

        public ZeroLineDecoration(SingleBarContext barContext) {
            super(barContext.barSideMargin);
            this.barContext = barContext;
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            // Draw zero lines for all visible views, but not through parent's padding
            for (int i = 0; i < parent.getChildCount(); i++) {

                View view = parent.getChildAt(i);

                int position = parent.getChildLayoutPosition(view);

                int xRight = position == parent.getAdapter().getItemCount() - 1?
                        Math.max(parent.getPaddingLeft(), view.getRight()) :
                        Math.max(parent.getPaddingLeft(), view.getRight() + space);

                c.drawLine(
                        Math.max(parent.getPaddingLeft(), view.getLeft()),
                        view.getBottom(),
                        xRight,
                        view.getBottom(),
                        barContext.linePaint
                );
            }
        }
    }
}
