package com.zsj.loadingview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * @author 朱胜军
 * @date 2018/7/15
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class LoadingView extends RelativeLayout {

    private final CircleView mRightView;
    private final CircleView mMiddleView;
    private CircleView mLeftView;
    private long ANIMATOR_DURATION = 350;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.WHITE);

        mLeftView = getCircleView(context);
        mRightView = getCircleView(context);
        mMiddleView = getCircleView(context);

        mLeftView.exChangeColor(Color.RED);
        mMiddleView.exChangeColor(Color.BLUE);
        mRightView.exChangeColor(Color.GREEN);

        addView(mLeftView);
        addView(mRightView);
        addView(mMiddleView);

        post(new Runnable() {
            @Override
            public void run() {
                expandAnimation();
            }
        });
    }

    /**
     * 往外跑的动画
     */
    private void expandAnimation() {
        //左边的View往左走
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeftView, "translationX", 0, -dip2px(20));
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(mRightView, "translationX", 0, dip2px(20));

        AnimatorSet set = new AnimatorSet();
        set.playTogether(leftTranslationAnimator, rightTranslationAnimator);
        set.setDuration(ANIMATOR_DURATION);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                inAnimation();
            }
        });

        set.start();
    }

    /**
     * 往里跑的动画
     */
    protected void inAnimation() {
//左边的View往左走
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeftView, "translationX", -dip2px(20), 0);
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(mRightView, "translationX", dip2px(20), 0);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(leftTranslationAnimator, rightTranslationAnimator);
        set.setDuration(ANIMATOR_DURATION);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //往里动画执行完毕.改变颜色.
                //改变颜色规则,左边的颜色给中间,中间给右边,右边给左边
                int leftColor = mLeftView.getColor();
                int middleColor = mMiddleView.getColor();
                int rightColor = mRightView.getColor();

                mMiddleView.exChangeColor(leftColor);
                mRightView.exChangeColor(middleColor);
                mLeftView.exChangeColor(rightColor);

                expandAnimation();
            }
        });

        set.start();
    }


    private CircleView getCircleView(Context context) {
        CircleView circleView = new CircleView(context);
        LayoutParams params = new LayoutParams(dip2px(10), dip2px(10));
        params.addRule(CENTER_IN_PARENT);
        circleView.setLayoutParams(params);
        return circleView;
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }
}
