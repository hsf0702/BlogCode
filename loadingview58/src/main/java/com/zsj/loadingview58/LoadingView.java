package com.zsj.loadingview58;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * @author 朱胜军
 * @date 2018/7/14
 * 描述	      58同城加载数据View
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class LoadingView extends FrameLayout {

    private static final String TAG = "ZsjTAG";
    /**
     * 图形View
     */
    private ShapeView mShapeView;
    /**
     * 阴影View
     */
    private View mShadowView;

    /**
     * 位移距离
     */
    private int mTranslationDistance = 0;

    /**
     * 位移持续时间
     */
    private final long ANIMATOR_DURATION = 350;

    /**
     * 是否停止动画
     */
    private boolean mIsStopAni = false;

    public LoadingView(@NonNull Context context) {
        this(context, null);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTranslationDistance = dip2px(80);

        initLayout();
        //1.2.1  下落动画同时阴影缩小，当下落动画执行完成，开始上抛动画同时阴影放大，当上抛动画执行完成开始下落动画。一直轮训执行。
        post(new Runnable() {
            @Override
            public void run() {
                startFallAni();
            }
        });
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    private void initLayout() {
        //加载显示组合控件
        inflate(getContext(), R.layout.loading_view, this);

        mShapeView = (ShapeView) findViewById(R.id.shape_view);
        mShadowView = findViewById(R.id.shadow_view);
    }

    /**
     * 开始执行下落动画
     */
    private void startFallAni() {
        if (mIsStopAni) {
            return;
        }
        Log.e(TAG, "startFallAni " + this);
        //下落动画同时阴影缩小
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", 0, mTranslationDistance);
        //2.2.1.1 下落的速度应该是开始慢后来越来快的 ,设置加速差值器
        translationAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 1.0f, 0.3f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATOR_DURATION);
        animatorSet.playTogether(translationAnimator, scaleAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //当下落动画执行完成，开始上抛动画
                startUpAni();

                // 2.2.2 ： 下落动画执行完毕的时候ShapeView改变形状
                mShapeView.exchange();
            }
        });
        animatorSet.start();
    }

    /**
     * 开始上抛动画
     */
    private void startUpAni() {
        if (mIsStopAni) {
            return;
        }
        //开始上抛动画同时阴影放大
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", mTranslationDistance, 0);
        //2.2.1.1 上抛的速度应该是开始快后来越来越慢的，设置减速插值器
        translationAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 0.3f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATOR_DURATION);
        animatorSet.playTogether(translationAnimator, scaleAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                // 2.2.3 ： 上抛动画开始的时候执行 旋转动画，正方形旋转180°，三角形旋转120°
                startRotation();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //当上抛动画执行完成开始下落动画
                startFallAni();
            }
        });
        animatorSet.start();
    }

    /**
     * 开始旋转动画
     */
    private void startRotation() {
        // 2.2.3 :正方形旋转180°，三角形旋转120°
        ObjectAnimator rotationAnimator = null;

        //判断当前图形是什么？
        switch (mShapeView.getCurrentShape()) {
            case Circle:
            case Square:
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 180);
                break;

            case Triangle:
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, -120);
                break;

            default:
                break;
        }
        rotationAnimator.setInterpolator(new DecelerateInterpolator());
        rotationAnimator.setDuration(ANIMATOR_DURATION);
        rotationAnimator.start();
    }


    @Override
    public void setVisibility(int visibility) {
        // 不要再去排放和计算，少走一些系统的源码
        super.setVisibility(INVISIBLE);

        //取消动画
        mShapeView.clearAnimation();
        mShadowView.clearAnimation();

        // 把LoadingView从父布局移除
        ViewGroup parentView = (ViewGroup) getParent();
        if (parentView != null) {
            parentView.removeView(this);
            removeAllViews();
        }

        mIsStopAni = true;
    }
}
