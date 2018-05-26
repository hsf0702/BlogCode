package com.zsj.recyclerviewsimple;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author 朱胜军
 * @date 2018/5/26
 * 描述	      分割线
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "LinearItemDecoration";
    private Paint mPaint;

    LinearItemDecoration() {


        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //代表每个Item下面留出10px空间
//        outRect.bottom = 10;

        //但是最后一个Item的底部不需要绘制分割线的
        //方法一:每一Item底部留出空间.最后一个不留空间
        //位置
//        int position = parent.getChildAdapterPosition(view);
//        //不是最后一个Item就留出10px空间
//        if (position != parent.getAdapter().getItemCount() - 1) {
//            outRect.bottom = 10;
//        }

//        //方法二:每一Item头部留出空间.第一个不留出空间
        int position = parent.getChildAdapterPosition(view);
        //不是第一个
        if (position != 0) {
            outRect.top = 10;
        }
    }


    /**
     * 绘制分割线
     *
     * @param canvas
     * @param parent
     * @param state
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        //方法一:每一Item底部留出空间.最后一个不留空间位置的绘制分割线的方式
//        int childCount = parent.getChildCount();
//        Rect rect = new Rect();
//        rect.left = parent.getPaddingStart();
//        rect.right = parent.getWidth() - parent.getPaddingEnd();
//
//        //最后一个不用绘制.childCount - 1;
//        for (int i = 0; i < childCount - 1; i++) {
//            //top 当前Item的底部
//            View childAt = parent.getChildAt(i);
//            rect.top = childAt.getBottom();
//            rect.bottom = rect.top + 10;
//            canvas.drawRect(rect, mPaint);
//        }

//        //方法二:每一Item头部留出空间.第一个不留出空间的绘制分割的方式
//        //在每个Item头部绘制
        int childCount = parent.getChildCount();
        //绘制区域
        Rect rect = new Rect();
        rect.left = parent.getPaddingStart();
        rect.right = parent.getWidth() - parent.getPaddingEnd();
        //第一个不用绘制.i从1开始
        for (int i = 1; i < childCount; i++) {
            //Bottom .当前Item的Top
            View childAt = parent.getChildAt(i);
            rect.bottom = childAt.getTop();
            rect.top = rect.bottom - 10;
            canvas.drawRect(rect, mPaint);
        }

    }
}
