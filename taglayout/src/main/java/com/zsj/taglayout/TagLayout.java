package com.zsj.taglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 朱胜军
 * @date 2018/7/1
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class TagLayout extends ViewGroup {

    /**
     * 用来保存所有的子View
     */
    private List<List<View>> mChildViews = new ArrayList<>();

    private BaseAdapter mAdapter;

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //2.1 onMeasure() 指定宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //避免onMeasure被调用多次的情况.
        mChildViews.clear();

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = getPaddingTop() + getPaddingBottom();
        //一行宽度
        int lineWidth = getPaddingLeft();
//        2.1.1 for循环测量子View
        //高度不一致,计算最大的高度.
        int maxHeight = 0;
        int childCount = getChildCount();
        ArrayList<View> childViews = new ArrayList<>();
        mChildViews.add(childViews);

        for (int i = 0; i < childCount; i++) {
            //获取子View
            View childView = getChildAt(i);
            // 这段话执行之后就可以获取子View的宽高，因为会调用子View的onMeasure
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            // margin值 ViewGroup.LayoutParams 没有 就用系统的MarginLayoutParams
            // 想想 LinearLayout为什么有？
            // LinearLayout有自己的 LayoutParams  会复写一个非常重要的方法
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();

            //2.1.2根据子View计算自己布局的宽高度
            // // 什么时候需要换行，一行不够的情况下 考虑 margin
            if (lineWidth + childView.getMeasuredWidth() + params.leftMargin + params.rightMargin > width) {
                //换行
                //高度累加
                height += maxHeight;
                //一行宽度重置
                lineWidth = getPaddingLeft() + params.leftMargin + params.rightMargin;
                childViews = new ArrayList<>();
                mChildViews.add(childViews);
            } else {
                //不换行
                // 宽度累加
                lineWidth += childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                maxHeight = Math.max(childView.getMeasuredHeight() + params.topMargin + params.bottomMargin, maxHeight);
            }

            childViews.add(childView);
        }

        height += maxHeight;
        width = resolveSize(width, widthMeasureSpec);
        height = resolveSize(height, heightMeasureSpec);
//        2.1.3 指定自己的布局
        setMeasuredDimension(width, height);
    }

    @Override
    public MarginLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        for循环摆放所有的子View
        int left, top = 0, right, bottom;
        for (List<View> childViews : mChildViews) {
            //高度不一致,计算最大的高度.
            int maxHeight = 0;
            left = getPaddingLeft();
            for (View childView : childViews) {
                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
                left += params.leftMargin;
                int childTop = top + params.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = childTop + childView.getMeasuredHeight();
                // 摆放
                childView.layout(left, childTop, right, bottom);
                //left 不断叠加
                left += childView.getMeasuredWidth() + params.rightMargin;

                int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                maxHeight = Math.max(childHeight, maxHeight);
            }
            //不断叠加Top
            top += maxHeight;
        }
    }


    public void setAdapter(BaseAdapter adapter) {
        if (adapter == null) {
            new NullPointerException("adapter 不能为空");
        }

        //如果多次设置Adapter清除所有的子view
        removeAllViews();

        mAdapter = null;
        mAdapter = adapter;

        //获取子view的个数
        int childViewCount = mAdapter.getCount();

        for (int i = 0; i < childViewCount; i++) {
            View childView = mAdapter.getView(i, this);
            addView(childView);
        }
    }
}
