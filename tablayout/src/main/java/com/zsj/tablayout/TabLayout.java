package com.zsj.tablayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 朱胜军
 * @date 2018/6/2
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class TabLayout extends ViewGroup {
    private static final String TAG = "myTabLayout";
    private List<List<View>> mChildViews = new ArrayList<>();

    public TabLayout(Context context) {
        super(context);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 指定宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //会调用onMeasure 两次,情况集合
        mChildViews.clear();
        //for循环测量子view
        int childCount = getChildCount();

        //自己的宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //自己的高度，因为是自动换行，高度要计算
        int height = getPaddingTop() + getPaddingBottom();

        int lineWidth = getPaddingLeft();

        int maxHeight = 0;
        List<View> childViews = new ArrayList<>();
        mChildViews.add(childViews);

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //测量子View,调用这句话才能拿到子View的宽高，因为会调用子view的onMeasure方法
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            //margin值  ViewGroup.LayoutParams 没有 margin值
            //为什么LinearLayout有margin值. 因为LinearLayout有自己的LayoutParams 会复写很重要的一个方法
            ViewGroup.MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            //TODO 后面处理Margin的问题
            if (lineWidth + childView.getMeasuredWidth() + params.leftMargin + params.rightMargin > width) {
                //换行
                mChildViews.add(childViews);
                childViews = new ArrayList<>();
                //高度子view的高度
                height += childView.getMeasuredHeight();
                //行的宽度重置
                lineWidth = getPaddingLeft();
            } else {
                //没有换行
                childViews.add(childView);

                lineWidth += childView.getMeasuredWidth();
                //貌似会可能会有bug。后面出来
                maxHeight = Math.max(childView.getMeasuredHeight(), maxHeight);
            }
        }

        height += maxHeight;
        //根据子View的计算和指定自己的布局
        Log.d(TAG, "onMeasure width->" + width + " height ->" + height);
        setMeasuredDimension(width, height);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout ");
        int left, top = 0, right, bottom;
        for (List<View> childViews : mChildViews) {
            left = getPaddingLeft();
            for (View childView : childViews) {
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();
                Log.d(TAG, "onLayout left->" + left + " top ->" + top + " right ->" + right + " bottom ->" + bottom);
                childView.layout(left, top, right, bottom);
                left += childView.getMeasuredWidth();
            }
            top += childViews.get(0).getMeasuredHeight();
        }
    }
}
