package com.zsj.shapeview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author 朱胜军
 * @date 2018/6/24
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class ShapeView extends View {

    private final Paint mSquarePaint;
    private final Paint mTrianglePaint;
    private final Paint mCirclePaint;
    /**
     * 圆形的颜色
     */
    private int mCircleColor = Color.RED;
    /**
     * 正方形的颜色
     */
    private int mSquareColor = Color.BLUE;
    /**
     * 三角形的颜色
     */
    private int mTriangleColor = Color.YELLOW;
    private Rect mRect;
    private Path mPath;


    /**
     * 三种形状
     */
    private enum Shape {
        CIRCLE, SQUARE, TRIANGLE
    }

    /**
     * 当前的形状
     */
    private Shape mCurrentShape = Shape.CIRCLE;

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //自定义属性,圆的颜色,正方形的颜色,三角形的颜色
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);

        mCircleColor = typedArray.getColor(R.styleable.ShapeView_circleColor, mCircleColor);
        mSquareColor = typedArray.getColor(R.styleable.ShapeView_squareColor, mSquareColor);
        mTriangleColor = typedArray.getColor(R.styleable.ShapeView_triangleColor, mTriangleColor);
        //回收
        typedArray.recycle();

        mCirclePaint = getPaint(mCircleColor);
        mSquarePaint = getPaint(mSquareColor);
        mTrianglePaint = getPaint(mTriangleColor);
    }


    private Paint getPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //之确保是正方形即可
        setMeasuredDimension(Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
                , Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec)));
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        //判断当前的是什么形状就绘制什么形状
        switch (mCurrentShape) {
            case CIRCLE:
                //绘制圆形
                float cx = getWidth() / 2;
                float cy = getWidth() / 2;
                float radius = getWidth() / 2;
                canvas.drawCircle(cx, cy, radius, mCirclePaint);
                break;
            case SQUARE:
                //绘制正方形
                if (mRect == null) {
                    mRect = new Rect(0, 0, getWidth(), getHeight());
                }
                canvas.drawRect(mRect, mSquarePaint);
                break;

            case TRIANGLE:
                //绘制三角形,canvas没有直接绘制三角形的方法.只能通过绘制路径
                if (mPath == null) {
                    mPath = new Path();
                    //等腰三角形
//                    mPath.moveTo(getWidth() / 2, 0);
//                    mPath.lineTo(0, getWidth());
//                    mPath.lineTo(getWidth(), getWidth());
//                    mPath.lineTo(getWidth() / 2, 0);
                    //改成等边三角形
                    mPath.moveTo(getWidth() / 2, 0);
                    mPath.lineTo(0, (float) (getWidth() / 2 * Math.sqrt(3)));
                    mPath.lineTo(getWidth(), (float) (getWidth() / 2 * Math.sqrt(3)));
                    mPath.close();//闭合路径
                }
                canvas.drawPath(mPath, mTrianglePaint);
                break;
            default:
                break;
        }
    }

    public void changeShape() {
        switch (mCurrentShape) {
            case CIRCLE:
                mCurrentShape = Shape.SQUARE;
                break;

            case SQUARE:
                mCurrentShape = Shape.TRIANGLE;
                break;

            case TRIANGLE:
                mCurrentShape = Shape.CIRCLE;
                break;
            default:
                break;
        }

        invalidate();
    }

}
