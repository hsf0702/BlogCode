package com.zsj.recyclerviewsimple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author 朱胜军
 * @date 2018/5/26
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mLineDrawable;

    public GridItemDecoration(Context context, int drawableRes) {
        mLineDrawable = ContextCompat.getDrawable(context, drawableRes);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = 10;
        outRect.bottom = 10;
    }


    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        int childCount = parent.getChildCount();
        drawV(canvas, parent, childCount);
        drawH(canvas, parent, childCount);
    }

    /**
     * 绘制行
     *
     * @param canvas
     * @param parent
     * @param childCount
     */
    private void drawH(Canvas canvas, RecyclerView parent, int childCount) {
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int left = childView.getLeft() - layoutParams.leftMargin;
            int right = childView.getRight() + layoutParams.rightMargin;
            int top = childView.getBottom() + layoutParams.topMargin;
            int bottom = top + 10 ;

            mLineDrawable.setBounds(left, top, right, bottom);
            mLineDrawable.draw(canvas);

        }
    }

    /**
     * 绘制竖
     *
     * @param canvas
     * @param parent
     * @param childCount
     */
    private void drawV(Canvas canvas, RecyclerView parent, int childCount) {
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();

            int left = childView.getRight()+layoutParams.rightMargin;
            int right = left + 10;
            int top = childView.getTop() - layoutParams.topMargin;
            int bottom = childView.getBottom() + layoutParams.bottomMargin;

            mLineDrawable.setBounds(left, top, right, bottom);
            mLineDrawable.draw(canvas);

        }
    }
}
