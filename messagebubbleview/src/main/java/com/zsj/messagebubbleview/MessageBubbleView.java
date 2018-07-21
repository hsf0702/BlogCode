package com.zsj.messagebubbleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author 朱胜军
 * @date 2018/7/21
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class MessageBubbleView extends View {

    private Point mFixedPoint;
    private Point mDagPoint;
    private int mFixedMaxRadius = 10;
    private int mFixedMinRadius = 5;
    private int mDragRadius = 12;
    private Paint mPaint;
    private Point mP0;
    private Point mP1;
    private Point mP2;
    private Point mP3;
    private Point mControlPoint;

    public MessageBubbleView(Context context) {
        this(context, null);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFixedMaxRadius = dip2px(mFixedMaxRadius);
        mFixedMinRadius = dip2px(mFixedMinRadius);
        mDragRadius = dip2px(mDragRadius);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float fixedX = event.getX();
                float fixedY = event.getY();
                initFixedPoint(fixedX, fixedY);
                break;
            case MotionEvent.ACTION_MOVE:
                float dragX = event.getX();
                float dragY = event.getY();
                updateDragPoint(dragX, dragY);
                break;
            case MotionEvent.ACTION_UP:

                break;

            default:
                break;
        }
        invalidate();
        return true;
    }

    private void updateDragPoint(float dragX, float dragY) {
        if (mDagPoint == null) {
            mDagPoint = new Point((int) dragX, (int) dragY);
        }
        mDagPoint.x = (int) dragX;
        mDagPoint.y = (int) dragY;

    }


    private void initFixedPoint(float fixedX, float fixedY) {
        if (mFixedPoint == null) {
            mFixedPoint = new Point();
        }
        mFixedPoint.x = (int) fixedX;
        mFixedPoint.y = (int) fixedY;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFixedPoint == null || mDagPoint == null) {
            return;
        }
        //绘制拖拽圆
        canvas.drawCircle(mDagPoint.x, mDagPoint.y, mDragRadius, mPaint);
        //移动的时候,固定圆缩小.根据拖拽圆和固定圆的距离缩小
        //计算拖拽圆和固定圆的距离
        double dragFixedDistance = getDragFixedDistance(mFixedPoint, mDagPoint);
        float fixedRadius = (float) (mFixedMaxRadius - dragFixedDistance / 14);
        if (fixedRadius > mFixedMinRadius) {
            //绘制固定圆
            canvas.drawCircle(mFixedPoint.x, mFixedPoint.y, fixedRadius, mPaint);
            Path bezierPath = getBezierPath(mFixedPoint, mDagPoint);
            canvas.drawPath(bezierPath,mPaint);
        }
    }

    private Path getBezierPath(Point fixedPoint, Point dagPoint) {
        double dragFixedDistance = getDragFixedDistance(mFixedPoint, mDagPoint);
        float fixedRadius = (float) (mFixedMaxRadius - dragFixedDistance / 14);
        float dx = Math.abs(fixedPoint.x - dagPoint.x);
        float dy = Math.abs(fixedPoint.y - dagPoint.y);
        float tanA = dy / dx;
        float a = (float) Math.atan(tanA);

        //P0 点
        if (mP0 == null) {
            mP0 = new Point();
        }
        mP0.x = mDagPoint.x + (int) (mDragRadius * Math.sin(a));
        mP0.y = mDagPoint.y - (int) (mDragRadius * Math.cos(a));

        //P1 点
        if (mP1 == null) {
            mP1 = new Point();
        }
        mP1.x = mFixedPoint.x + (int) (fixedRadius * Math.sin(a));
        mP1.y = mFixedPoint.y - (int) (fixedRadius * Math.cos(a));


        //P2 点
        if (mP2 == null) {
            mP2 = new Point();
        }
        mP2.x = mFixedPoint.x - (int) (fixedRadius * Math.sin(a));
        mP2.y = mFixedPoint.y + (int) (fixedRadius * Math.cos(a));


        //P0 点
        if (mP3 == null) {
            mP3 = new Point();
        }
        mP3.x = mDagPoint.x - (int) (mDragRadius * Math.sin(a));
        mP3.y = mDagPoint.y + (int) (mDragRadius * Math.cos(a));


        //绘制路径
        Path path = new Path();
        path.moveTo(mP0.x, mP0.y);

        //控制点选择固定圆和拖拽圆的中心点
        Point controlPoint = getControlPoint();
        path.quadTo(controlPoint.x,controlPoint.y,mP1.x,mP1.y);

        path.lineTo(mP2.x,mP2.y);
        path.quadTo(controlPoint.x,controlPoint.y,mP3.x,mP3.y);
        path.close();
        return path;
    }

    private Point getControlPoint() {
        if (mControlPoint == null) {
            mControlPoint = new Point();
        }
        mControlPoint.x = (mDagPoint.x + mFixedPoint.x)/2;
        mControlPoint.y = (mDagPoint.y + mFixedPoint.y)/2;
        return mControlPoint;
    }

    private double getDragFixedDistance(Point fixedPoint, Point dagPoint) {
        return Math.sqrt((dagPoint.x - fixedPoint.x) * (dagPoint.x - fixedPoint.x) + (dagPoint.y - fixedPoint.y) * (dagPoint.y - fixedPoint.y));
    }
}
