package com.zsj.lovelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * @author 朱胜军
 * @date 2018/7/22
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class LoveLayout extends RelativeLayout {
    private static final String TAG = "zsjTAG";
    private int[] mLoveImages;
    private Random mRandom;
    private Interpolator[] mInterpolator;

    /**
     * 控件的宽高
     */
    private int mHeight, mWidth;
    private int mLoveImageHeight, mLoveImageWidth;

    public LoveLayout(Context context) {
        this(context, null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRandom = new Random();
        mLoveImages = new int[]{
                R.drawable.pl_blue, R.drawable.pl_red, R.drawable.pl_yellow
        };

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.pl_blue);
        mLoveImageHeight = drawable.getIntrinsicHeight();
        mLoveImageWidth = drawable.getIntrinsicWidth();


        mInterpolator = new Interpolator[]{new AccelerateDecelerateInterpolator(), new AccelerateInterpolator(),
                new DecelerateInterpolator(), new LinearInterpolator()};
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    public void addLove() {
        //在底部中心出现爱心的图片
        ImageView loveIv = new ImageView(getContext());
        loveIv.setImageResource(mLoveImages[mRandom.nextInt(mLoveImages.length)]);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(CENTER_HORIZONTAL);
        loveIv.setLayoutParams(layoutParams);
        addView(loveIv);

        AnimatorSet animationSet = getAnimationSet(loveIv);
        animationSet.start();
    }

    private AnimatorSet getAnimationSet(ImageView loveIv) {
        //动画,一个是放大动画,透明度动画
        AnimatorSet animatorSet = new AnimatorSet();
        // 放大动画
        AnimatorSet innerAnimatorSet = new AnimatorSet();
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(loveIv, "alpha", 0.2f, 1.0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(loveIv, "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(loveIv, "scaleY", 0.2f, 1.0f);

        innerAnimatorSet.setDuration(350);
        innerAnimatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
        //贝塞尔曲线动画
        animatorSet.playSequentially(innerAnimatorSet, getBezierAnimator(loveIv));

        return animatorSet;

    }

    private Animator getBezierAnimator(final ImageView loveIv) {
        //固定,在最低的中间
        PointF pointF0 = new PointF(mWidth / 2 - mLoveImageWidth / 2, mHeight - mLoveImageHeight);
        //确保 p2 的y 值一定要小于 p1 的y值.
        PointF pointF1 = new PointF(mRandom.nextInt(mWidth) - mLoveImageWidth, mRandom.nextInt(mHeight / 2) + mHeight / 2);
        PointF pointF2 = new PointF(mRandom.nextInt(mWidth) - mLoveImageWidth, mRandom.nextInt(mHeight / 2));
        //y是固定的.x的宽度的范围
        PointF pointF3 = new PointF(mRandom.nextInt(mWidth) - mLoveImageWidth, 0);
        BezierTypeEvaluator bezierTypeEvaluator = new BezierTypeEvaluator(pointF1, pointF2);
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(bezierTypeEvaluator, pointF0, pointF3);
        valueAnimator.setDuration(3000);
        //设置随机插值器
        valueAnimator.setInterpolator(mInterpolator[mRandom.nextInt(mInterpolator.length)]);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                loveIv.setX(pointF.x);
                loveIv.setY(pointF.y);

                float fraction = animation.getAnimatedFraction();
                loveIv.setAlpha(1 - fraction + 0.2f);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(loveIv);
            }
        });
        return valueAnimator;
    }
}
