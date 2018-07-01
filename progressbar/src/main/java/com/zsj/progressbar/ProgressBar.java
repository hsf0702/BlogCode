package com.zsj.progressbar;

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

import com.zsj.corelibrary.utils.DimenUtils;

/**
 * @author 朱胜军
 * @date 2018/6/24
<<<<<<< HEAD
 * 描述	      TODO
 * <p>
 * 圆形进度条
 * 1. 绘制外圆
 * 2. 绘制进度圆
 * 3. 绘制中间文字
 * <p>
 * <p>
=======
 * 描述	      圆形进度条
 * 1.绘制外圆
 * 2.绘制进度圆
 * 3.绘制中间文本
>>>>>>> 圆形进度条 2
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class ProgressBar extends View {
    /**
     * 圆环宽度
     */
    private int mRingWidth = 20;

    /**
     * 外圆颜色
     */
    private int mOutCircleColor = Color.BLUE;

    /**
     * 进度圆颜色
     */
    private int mInnerCircleColor = Color.RED;

    /**
     * 中间文字的大小
     */
    private int mCenterTextSize = 18;

    /**
     * 中间文字的颜色
     */
    private int mCenterTextColor = Color.RED;

    private Paint mOutCirclePaint;
    private Paint mInnerCirclePaint;
    private double mCurrentPercentage = 0.0;
    private RectF mOval;
    private Paint mCenterTextPaint;
    private Rect mBounds;

    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        mRingWidth = typedArray.getDimensionPixelOffset(R.styleable.ProgressBar_ringWidth,
                DimenUtils.dip2px(context, mRingWidth));
        mOutCircleColor = typedArray.getColor(R.styleable.ProgressBar_outCircleColor, mOutCircleColor);
        mInnerCircleColor = typedArray.getColor(R.styleable.ProgressBar_innerCircleColor, mInnerCircleColor);
        mCenterTextSize = typedArray.getDimensionPixelOffset(R.styleable.ProgressBar_centerTextSize,
                DimenUtils.dip2px(context, mCenterTextSize));
        mCenterTextColor = typedArray.getColor(R.styleable.ProgressBar_centerTextColor, mCenterTextColor);
        typedArray.recycle();

        //初始化画笔
        initOutPaint();
        initInnerPaint();
        initCenterPaint();
    }

    private void initCenterPaint() {
        mCenterTextPaint = new Paint();
        mCenterTextPaint.setAntiAlias(true);
        mCenterTextPaint.setColor(mCenterTextColor);
        mCenterTextPaint.setTextSize(mCenterTextSize);
    }

    private void initOutPaint() {
        mOutCirclePaint = new Paint();
        mOutCirclePaint.setAntiAlias(true);
        mOutCirclePaint.setColor(mOutCircleColor);
        mOutCirclePaint.setStyle(Paint.Style.STROKE);
        mOutCirclePaint.setStrokeWidth(mRingWidth);
    }

    private void initInnerPaint() {
        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setAntiAlias(true);
        mInnerCirclePaint.setColor(mInnerCircleColor);
        mInnerCirclePaint.setStyle(Paint.Style.STROKE);
        mInnerCirclePaint.setStrokeWidth(mRingWidth);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量,宽高一样

        //默认大小
        int defaultSize = DimenUtils.dip2px(getContext(), 200);
        //宽度
//        //获取模式
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        //获取大小
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//
//        //包裹内容
//        if (widthMode == MeasureSpec.AT_MOST) {
//            widthSize = defaultSize;
//        }
//
//        //高度
//        //获取模式
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        //获取大小
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        //包裹内容
//        if (heightMode == MeasureSpec.AT_MOST) {
//            heightSize = defaultSize;
//        }

        int widthSize = resolveSize(defaultSize, widthMeasureSpec);
        int heightSize = resolveSize(defaultSize, heightMeasureSpec);

        //宽高要一致
        int progressSize = Math.min(widthSize, heightSize);

        //设置宽高
        setMeasuredDimension(progressSize, progressSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 1.绘制外圆
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        float radius = getWidth() / 2 - mRingWidth / 2;
        canvas.drawCircle(cx, cy, radius, mOutCirclePaint);

        //2.绘制进度圆
        float currentProgress = (float) (mCurrentPercentage * 360);
        if (mOval == null) {
            mOval = new RectF(mRingWidth / 2, mRingWidth / 2,
                    getWidth() - mRingWidth / 2, getHeight() - mRingWidth / 2);
        }
        canvas.drawArc(mOval, 0, currentProgress, false, mInnerCirclePaint);

        //3.绘制中间文本
        String text = (int) (mCurrentPercentage * 100) + "%";
        //字体的宽度高度
        if (mBounds == null) {
            mBounds = new Rect();
        }
        mCenterTextPaint.getTextBounds(text, 0, text.length(), mBounds);
        float x = getWidth() / 2 - mBounds.width() / 2;
        //知道中间位置求基线位置
        Paint.FontMetricsInt fontMetricsInt = mCenterTextPaint.getFontMetricsInt();
        //指定中间位置,绘制文本
        int baseLine = getHeight() / 2 + (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        canvas.drawText(text, x, baseLine, mCenterTextPaint);
    }


    /**
     * 设置当前进度
     *
     * @param currentPercentage
     */
    public void setCurrentPercentage(double currentPercentage) {
//        提供公共方法,因为这样进度是动态变化的.
        mCurrentPercentage = currentPercentage;
        invalidate();
    }

}
