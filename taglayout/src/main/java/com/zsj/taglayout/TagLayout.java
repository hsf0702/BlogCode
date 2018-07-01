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
        int firstMaxHeight = 0;
        int childCount = getChildCount();
        ArrayList<View> childViews = new ArrayList<>();
        mChildViews.add(childViews);

        for (int i = 0; i < childCount; i++) {
            //获取子View
            View childView = getChildAt(i);
            // 这段话执行之后就可以获取子View的宽高，因为会调用子View的onMeasure
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            //2.1.2根据子View计算自己布局的宽高度
            if (lineWidth + childView.getMeasuredWidth() > width) {
                //换行
                //高度累加
                height += childView.getMeasuredHeight();
                //一行宽度重置
                lineWidth = getPaddingLeft();
                childViews = new ArrayList<>();
                mChildViews.add(childViews);
            } else {
                //不换行
                // 宽度累加
                lineWidth += childView.getMeasuredWidth();
                firstMaxHeight = Math.max(childView.getMeasuredHeight(), firstMaxHeight);
            }

            childViews.add(childView);
        }

        height += firstMaxHeight;
        width = resolveSize(width, widthMeasureSpec);
        height = resolveSize(height, heightMeasureSpec);
//        2.1.3 指定自己的布局
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        for循环摆放所有的子View
        int left, top = 0, right, bottom;
        for (List<View> childViews : mChildViews) {
            left = getPaddingLeft();
            for (View childView : childViews) {
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();
                // 摆放
                childView.layout(left, top, right, bottom);
                //left 不断叠加
                left += childView.getMeasuredWidth();
            }
            //不断叠加Top
            top += childViews.get(0).getMeasuredHeight();
        }
    }
}
