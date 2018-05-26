package com.zsj.recyclerviewsimple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
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

    private static final String TAG = "GridItemDecoration";
    private final Drawable mLineDrawable;

    public GridItemDecoration(Context context, int drawableRes) {
        mLineDrawable = ContextCompat.getDrawable(context, drawableRes);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = 10;
        outRect.bottom = 10;
        if (isLastCloum(view, parent)) {
            outRect.right = 0;
        }

        if (isLastRow(view, parent)) {
            outRect.bottom = 0;
        }

    }

    private boolean isLastCloum(View view, RecyclerView parent) {
        //当前的位置
        int currentPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        //列数
        int spanCount = getSpanCount(parent);
        if ((currentPosition + 1) % spanCount == 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取一共有多少列
     *
     * @param recyclerView
     * @return
     */
    private int getSpanCount(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }

        return 0;
    }

    /**
     * 是不是最后一行
     *
     * @param view
     * @param parent
     * @return
     */
    public boolean isLastRow(View view, RecyclerView parent) {
        //后面再看
//        //如果最后一行的个数<列数 或者 最后一行的个数刚好等于列数就是最后一行
//        //列数
//        int spanCount = getSpanCount(parent);
//        int childCount = parent.getAdapter().getItemCount();
//
//        //最后一行的个数
//        int lastRowCount = childCount % spanCount;
//        Log.d(TAG, "isLastRow lastRowCount ="+lastRowCount +" ,childCount ="+childCount+",spanCount= "+spanCount);
//        if (lastRowCount < spanCount) {
//            return true;
//        }
//        return false;


        int childCount = parent.getAdapter().getItemCount();
        int spanCount = getSpanCount(parent);
        // 100 - 100 % 3 = 100 -1 == 99  
        childCount = childCount - childCount % spanCount;
        //当前的位置
        int currentPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        // 如果是最后一行，则不需要绘制底部,当前位置大于或者等于childCount代表是最后一行的Item
        if (currentPosition >= childCount) {
            return true;
        }
        return false;
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
            int bottom = top + 10;

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

            int left = childView.getRight() + layoutParams.rightMargin;
            int right = left + 10;
            int top = childView.getTop() - layoutParams.topMargin;
            int bottom = childView.getBottom() + layoutParams.bottomMargin;

            mLineDrawable.setBounds(left, top, right, bottom);
            mLineDrawable.draw(canvas);

        }
    }


}
