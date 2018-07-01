package com.zsj.stepprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author 朱胜军
 * @date 2018/6/10
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class StepProgressView extends View {
    private static final String TAG = "StepProgressView";
    private int mOutProgressColor = Color.BLUE;
    private int mInnerProgressColor = Color.RED;
    private int mProgressWidth = 20;
    private int mCenterTextColor = Color.RED;
    private int mCenterTextSize = 16;
    private Paint mOutPaint;
    private Paint mInnerPaint;
    private Paint mCenterTextPaint;
    private RectF mRectF;
    private float mMaxProgress = 100;
    private float mCurrentProgress;
    private Rect mCenterTextRect;

    public StepProgressView(Context context) {
        this(context, null);
    }

    public StepProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //有那些不能写死的.通过自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepProgressView);
        mOutProgressColor = typedArray.getColor(R.styleable.StepProgressView_outProgressColor,
                mOutProgressColor);
        mInnerProgressColor = typedArray.getColor(R.styleable.StepProgressView_innerProgressColor,
                mInnerProgressColor);
        mProgressWidth = typedArray.getDimensionPixelOffset(R.styleable.StepProgressView_progressWidth,
                dip2px(getContext(), mProgressWidth));

        mCenterTextColor = typedArray.getColor(R.styleable.StepProgressView_centerTextColor, mCenterTextColor);
        mCenterTextSize = typedArray.getDimensionPixelSize(R.styleable.StepProgressView_centerTextSize,
                dip2px(getContext(), mCenterTextSize));
        //回收
        typedArray.recycle();

        initOutPaint();
        initInnerPaint();
        initCenterTextPaint();
    }

    private void initOutPaint() {
        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setColor(mOutProgressColor);
        mOutPaint.setStrokeWidth(mProgressWidth);
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initInnerPaint() {
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerProgressColor);
        mInnerPaint.setStrokeWidth(mProgressWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initCenterTextPaint() {
        mCenterTextPaint = new Paint();
        mCenterTextPaint.setAntiAlias(true);
        mCenterTextPaint.setColor(mCenterTextColor);
        mCenterTextPaint.setTextSize(mCenterTextSize);
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    //指定控件的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取测量模式
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        //确定大小,给多少用多少
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        //不确定大小,wrap_content .给一个默认值.
//        int defaultSize = dip2px(getContext(), 200);
//        if (widthMode == MeasureSpec.AT_MOST) {
//            width = defaultSize;
//        }
//        width = width + getPaddingLeft() + getPaddingRight();
//        //高
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        //不确定大小,wrap_content .给一个默认值.
//        if (heightMode == MeasureSpec.AT_MOST) {
//            height = defaultSize;
//        }
//        height = height + getPaddingTop() + getPaddingBottom();
        int defaultSize = dip2px(getContext(), 200);
        int width = resolveSize(defaultSize, widthMeasureSpec);
        int height = resolveSize(defaultSize, heightMeasureSpec);

        //指定控件的宽高,要正方形.取宽高比较后的最小值
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画外圆弧
        if (mRectF == null) {
            mRectF = new RectF(mProgressWidth / 2 + getPaddingLeft(), mProgressWidth / 2 + getPaddingTop(),
                    getWidth() - mProgressWidth / 2 - getPaddingRight(),
                    getHeight() - mProgressWidth / 2 - getPaddingBottom());
        }
        canvas.drawArc(mRectF, 135, 270, false, mOutPaint);


        float percentage = (mCurrentProgress / mMaxProgress);
        //绘制内圆弧
        canvas.drawArc(mRectF, 135, 270 * percentage, false, mInnerPaint);

        //绘制中间文字 文字颜色,文字大小不能写死.用自定义属性
        String centerText = (int) (percentage * 100) + " %";
        if (mCenterTextRect == null) {
            mCenterTextRect = new Rect();
        }
        mCenterTextPaint.getTextBounds(centerText, 0, centerText.length(), mCenterTextRect);

        //指定中间位置,求基线
        Paint.FontMetricsInt fontMetricsInt = mCenterTextPaint.getFontMetricsInt();
        int baseLine = getHeight() / 2 + (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;

        canvas.drawText(centerText, (getWidth() / 2 - mCenterTextRect.width() / 2), baseLine, mCenterTextPaint);
    }

    public synchronized void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
    }

    public synchronized void setCurrentProgress(float currentProgress) {
        mCurrentProgress = currentProgress;
        invalidate();
    }

}
