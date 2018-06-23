package com.zsj.colortracktextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * @author 朱胜军
 * @date 2018/6/23
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 * <p>
 * 分析:
 * 1.继承TextView.onMeasure,常用自定义属性不需要自己处理
 * 2.字体变色,两种字体颜色
 * <p><p>2.1 两种字体,需要定义自定义属性
 * <p><p>2.2 两种字体,两种画笔
 * 3.不同方向的变色
 * 4.结合ViewPager
 */
public class ColorTrackTextView extends TextView {

    private int mOriginalColor = Color.BLACK;
    private int mChangeColor = Color.RED;
    private Paint mChangePaint;
    private Paint mOriginalPaint;
    private double mCurrentProgress = 0.0;

    private Direction mDirection = Direction.LEFT_TO_RIGHT;

    /**
     * 不同方向的变色
     */
    public enum Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT;
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取TypedArray
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);

        mOriginalColor = typedArray.getColor(R.styleable.ColorTrackTextView_originalColor, mOriginalColor);
        mChangeColor = typedArray.getColor(R.styleable.ColorTrackTextView_changeColor, mChangeColor);
        //回收
        typedArray.recycle();

        mOriginalPaint = getPaint(mOriginalColor);
        mChangePaint = getPaint(mChangeColor);

    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }

    public void setCurrentProgress(double currentProgress) {
        this.mCurrentProgress = currentProgress;
        invalidate();
    }

    private Paint getPaint(int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(getTextSize());
        //防抖动
        paint.setDither(true);

        return paint;
    }

    //一种字体两种颜色
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        //根据进度算出中间值
        int middle = (int) (mCurrentProgress * getWidth());
        //从左到右
        if (mDirection == Direction.LEFT_TO_RIGHT) {
            //绘制不变色部分
            drawText(canvas, 0, middle, mChangePaint);
            //绘制变色部分
            drawText(canvas, middle, getWidth(), mOriginalPaint);
        } else {//从右到左
            //绘制不变色部分
            drawText(canvas, getWidth() - middle, getWidth(), mOriginalPaint);
            //绘制变色部分
            drawText(canvas, 0, getWidth()-middle, mChangePaint);
        }

    }

    private void drawText(Canvas canvas, int start, int end, Paint paint) {
        canvas.save();
        //区域
        Rect rect = new Rect(start, 0, end, getHeight());
        //裁剪区域,只允许在这个区域绘制内容
        canvas.clipRect(rect);
        //获取文字
        String text = getText().toString();
        //字体宽度
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int x = getWidth() / 2 - bounds.width() / 2;
        //基线
        //指定中间位置,求基线
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int baseLine = getHeight() / 2 + (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        canvas.drawText(text, x, baseLine, paint);
        canvas.restore();
    }
}
