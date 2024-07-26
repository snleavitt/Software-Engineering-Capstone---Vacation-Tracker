package my.trackerapp.vacationtracker.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Paint;
import android.graphics.Rect;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

//For grid lines
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint dividerPaint;
    private final int dividerHeight;


    //Constructor
    public DividerItemDecoration(Context context, @ColorInt int dividerColor, int dividerHeight) {
        dividerPaint = new Paint();
        dividerPaint.setColor(dividerColor);
        this.dividerHeight = dividerHeight;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int spanCount = getSpanCount(parent);

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (position >= spanCount) {
                int top = child.getTop() - dividerHeight;
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                int bottom = child.getTop();
                c.drawRect(left, top, right, bottom, dividerPaint);
            }

            if ((position + 1) % spanCount != 0) {
                int left = child.getRight();
                int top = child.getTop();
                int right = left + dividerHeight;
                int bottom = child.getBottom();
                c.drawRect(left, top, right, bottom, dividerPaint);
            }
        }
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return 1;
    }
}
