package com.kongzue.albumdialog.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;

    public int getSpanCount() {
        return spanCount;
    }

    public GridSpacingItemDecoration setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return this;
    }

    public int getSpacing() {
        return spacing;
    }

    public GridSpacingItemDecoration setSpacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        if (column >= 1) {
            outRect.left = spacing;
        }

        if (position >= spanCount) {
            outRect.top = spacing;
        }
    }
}
