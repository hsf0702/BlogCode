package com.zsj.mytextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author 朱胜军
 * @date 2018/6/9
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class MyTextView extends LinearLayout {

    private String mMyText;
    private int mMyTextColor = Color.BLACK;
    private int mMyTextSize = 15;
    private Paint mPaint;
    private Rect mBounds;

    public MyTextView(Context context) {
        this(context, null);

    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取TypedArray
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        mMyText = typedArray.getString(R.styleable.MyTextView_myText);
        mMyTextColor = typedArray.getColor(R.styleable.MyTextView_myTextColor, mMyTextColor);
        mMyTextSize = typedArray.getDimensionPixelSize(R.styleable.MyTextView_myTextSize, mMyTextSize);

        //回收TypedArray
        typedArray.recycle();
        initPaint();

        //默认给一个背景
//        setBackgroundColor(Color.TRANSPARENT);
        setWillNotDraw(false);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(mMyTextColor);
        mPaint.setTextSize(mMyTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
        //指定控件的宽高
        //三种测量模式,AT_MOST , EXACTLY,UNSPECIFIED

        //获取测量模式
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);

        //宽度
        //1,确定的值,不需要计算.给多少就要多少
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) + getPaddingLeft() + getPaddingRight();

        //2.wrap_content. 需要计算
        if (widthMeasureMode == MeasureSpec.AT_MOST) {
            //计算宽度 . 宽度和字的长度和字大小有关系 .所以画笔进行测量
            if (mBounds == null) {
                mBounds = new Rect();
            }

            mPaint.getTextBounds(mMyText, 0, mMyText.length(), mBounds);
            widthSize = mBounds.width() + getPaddingLeft() + getPaddingRight();
        }


        //高度

        //1,确定的值,不需要计算.给多少就要多少
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) + getPaddingTop() + getPaddingBottom();

        //2.wrap_content. 需要计算
        if (heightMeasureMode == MeasureSpec.AT_MOST) {
            //计算宽度 . 宽度和字的长度和字大小有关系 .所以画笔进行测量
            mPaint.getTextBounds(mMyText, 0, mMyText.length(), mBounds);
            heightSize = mBounds.height() + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        //指定中间位置,绘制文本
        int baseLine = getHeight() / 2 + (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        canvas.drawText(mMyText, getPaddingLeft(), baseLine, mPaint);
    }
}
