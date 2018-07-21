package com.zsj.messagebubbleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
        }
    }

    private double getDragFixedDistance(Point fixedPoint, Point dagPoint) {
        return Math.sqrt((dagPoint.x - fixedPoint.x) * (dagPoint.x - fixedPoint.x) + (dagPoint.y - fixedPoint.y) * (dagPoint.y - fixedPoint.y));
    }
}
