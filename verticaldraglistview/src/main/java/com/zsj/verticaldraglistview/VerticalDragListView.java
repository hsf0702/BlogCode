package com.zsj.verticaldraglistview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ListViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * @author 朱胜军
 * @date 2018/7/14
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class VerticalDragListView extends FrameLayout {

    private static final String TAG = "TAG";
    private ViewDragHelper mViewDragHelper;
    private View mDragList;
    private View mMenuView;

    private int mMenuHeight;

    /**
     * 菜单是不是打开
     */
    private boolean mMenuIsOpen = false;

    public VerticalDragListView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 创建实例需要3个参数，第一个就是当前的ViewGroup，
         * 第二个sensitivity，
         * 第三个参数就是Callback，在用户的触摸过程中会回调相关方法
         */
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, mDragCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("VerticalDragListView 只能有两个子View");
        }

        //后面的菜单
        mMenuView = getChildAt(0);
        //列表
        mDragList = getChildAt(1);
    }


    private float mDownY;

    /**
     * 事件拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //3 处理事件拦截
        // 3.2 当菜单打开的时候。执行自己的onTouchEvent方法
        if (mMenuIsOpen) {
            return true;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                //gnoring pointerId=0 because ACTION_DOWN was not received for this pointer before ACTION_MOVE
                //让mViewDragHelper有ACTION_DOWN的完整流程
                mViewDragHelper.processTouchEvent(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                // 3.1 当向下滑动 && 列表在最顶部的时候 拦截事件，return true 。走自己的onTouchEvent方法
                float moveY = ev.getY();
                if (moveY > mDownY && !canChildScrollUp()) {
                    return true;
                }
                break;

            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 还能不能向上滑动，如果可以返回true，否则返回false
     *
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (mDragList instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mDragList, -1);
        }
        return mDragList.canScrollVertically(-1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            //2.3.1 获取菜单的高度 ,只要在setMeasuredDimension后面都可以拿到控件的宽高
            mMenuHeight = mMenuView.getMeasuredHeight();
        }
    }

    ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            //2,1 前面列表可以动，后面菜单不可以动
            return mDragList == child;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            // 2.3、垂直滑动的距离只能是后面菜单的高度
            // 2.3.1、先获取后面菜单的高度
            if (top < 0) {
                top = 0;
            }
            if (top > mMenuHeight) {
                top = mMenuHeight;
            }
            return top;
        }

        /*
        // 2.2 只能垂直滑动 实现clampViewPositionVertical，实现clampViewPositionHorizontal
        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return super.clampViewPositionHorizontal(child, left, dx);
        }*/

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if (releasedChild == mDragList) {
                // 2.4、 手指松开二选一，要么打开菜单，要么关闭菜单
                if (mDragList.getTop() > mMenuHeight / 2) {
                    // 2.4.1 当 滑动的距离大于菜单高度的一半，打开菜单
                    mViewDragHelper.settleCapturedViewAt(0, mMenuHeight);
                    mMenuIsOpen = true;
                } else {
                    //否则，关闭菜单
                    mViewDragHelper.settleCapturedViewAt(0, 0);
                    mMenuIsOpen = false;
                }
                invalidate();
            }
        }
    };

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
