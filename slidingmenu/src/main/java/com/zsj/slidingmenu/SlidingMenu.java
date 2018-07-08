package com.zsj.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.zsj.corelibrary.utils.DimenUtils;

/**
 * @author 朱胜军
 * @date 2018/7/8
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class SlidingMenu extends HorizontalScrollView {
    private static final String TAG = "TAG";
    private int mMenuRightMargin = 0;
    private int mMenuWidth;
    private View mMenuView;
    private View mContentView;
    /**
     * 当前menu的状态,false为关闭,true为打开
     */
    private boolean mCurrentMenuState = false;
    /**
     * 处理快速滑动
     */
    private GestureDetector mGestureDetector;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        mMenuRightMargin = DimenUtils.dip2px(getContext(), 50);
        mMenuRightMargin = typedArray.getDimensionPixelSize(R.styleable.SlidingMenu_menuRightMargin, mMenuRightMargin);
        typedArray.recycle();
        //处理快速滑动
        mGestureDetector = new GestureDetector(context, mOnGestureListener);
    }

    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //Log.e(TAG, "onFling velocityX -->"+velocityX);
            //往右快速滑动为正数,往左快速滑动为负数

            if (mCurrentMenuState) {
                //当menu打开的时候,往左快速滑动关闭menu
                if (velocityX < 0) {
                    closeMenu();
                    return true;
                }
            } else {
                //当menu关闭的时候,往右快速滑动打开menu
                if (velocityX> 0){
                    openMenu();
                    return true;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };


    //宽度不对,全乱了.指定宽度
    @Override
    protected void onFinishInflate() {
        //这个方法是xml解析完毕调用
        super.onFinishInflate();

        //获取LinearLayout
        ViewGroup container = (ViewGroup) getChildAt(0);

        int childCount = container.getChildCount();
        if (childCount > 2) {
            new IllegalAccessException("不能超过2个子View");
        }
        //获取menu
        mMenuView = container.getChildAt(0);
        //获取内容
        mContentView = container.getChildAt(1);

        //指定内容也的宽度
        ViewGroup.LayoutParams contentLayoutParams = mContentView.getLayoutParams();
        //内容的宽度 = 屏幕的宽度
        contentLayoutParams.width = DimenUtils.getScreenWidth(getContext());
        mContentView.setLayoutParams(contentLayoutParams);

        //指定menu的宽度
        ViewGroup.LayoutParams menuLayoutParams = mMenuView.getLayoutParams();
        //menu的宽度 = 屏幕的宽度 - mMenuRightMargin
        mMenuWidth = DimenUtils.getScreenWidth(getContext()) - mMenuRightMargin;
        menuLayoutParams.width = mMenuWidth;
        mMenuView.setLayoutParams(menuLayoutParams);

        //发现,初始化关闭没有作用
//        scrollTo(mMenuWidth, 0);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //初始化关闭menu
        scrollTo(mMenuWidth, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //快速滑动处理
        if (mGestureDetector.onTouchEvent(ev)) {
            //当处理了快速滑动就不需要执行下面的逻辑
            return true;
        }
        //menu打开的时候的点击内容,关闭menu,同时不会响应内容页的事件 事件拦截.
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //只管手指抬起了的事件
            //根据当前滑动的位置处理关闭还是打开
            int currentScrollX = getScrollX();
            if (currentScrollX > mMenuWidth / 2) {
                //关闭
                closeMenu();
            } else {
                //打开
                openMenu();
            }
            //确保不会调用super,因为super.里面也有滑动的方法
            return true;
        }
        return super.onTouchEvent(ev);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 打开 Menu l == mMenuWidth --> 0
        // 所以 scale == 0 --> 1.0
        double scale = 1 - l * 1.0 / mMenuWidth;

        //右边的contentView 从 1 --> 0.7 缩放
        // 1 --> 0.7
        float rightScale = (float) (1.0 - 0.3 * scale);
        ViewCompat.setScaleX(mContentView, rightScale);
        ViewCompat.setScaleY(mContentView, rightScale);
        //设置锚点
        ViewCompat.setPivotX(mContentView, 0);
        ViewCompat.setPivotY(mContentView, mContentView.getMeasuredHeight() / 2);
    }

    private void closeMenu() {
        scrollTo(mMenuWidth, 0);
        mCurrentMenuState = false;
    }

    private void openMenu() {
        scrollTo(0, 0);
        mCurrentMenuState = true;
    }
}
