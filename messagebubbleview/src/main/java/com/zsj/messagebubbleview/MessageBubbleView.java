package com.zsj.messagebubbleview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;

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

    private static final String TAG = "zsjTAG";
    private PointF mFixedPoint;
    private PointF mDragPoint;
    private int mFixedMaxRadius = 10;
    private int mFixedMinRadius = 5;
    private int mDragRadius = 12;
    private Paint mPaint;
    private PointF mP0;
    private PointF mP1;
    private PointF mP2;
    private PointF mP3;
    private PointF mControlPoint;
    private Bitmap mDragBitmap;

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

/*    @Override
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
    }*/

    public void updateDragPoint(float dragX, float dragY) {
        if (mDragPoint == null) {
            mDragPoint = new PointF( dragX, dragY);
        }
        mDragPoint.x =  dragX;
        mDragPoint.y = dragY;
        invalidate();
    }


    public void initFixedPoint(float fixedX, float fixedY) {
        if (mFixedPoint == null) {
            mFixedPoint = new PointF();
        }
        mFixedPoint.x = fixedX;
        mFixedPoint.y = fixedY;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFixedPoint == null || mDragPoint == null) {
            return;
        }
        //绘制拖拽圆
        canvas.drawCircle(mDragPoint.x, mDragPoint.y, mDragRadius, mPaint);
        //移动的时候,固定圆缩小.根据拖拽圆和固定圆的距离缩小
        //计算拖拽圆和固定圆的距离
        double dragFixedDistance = getDragFixedDistance(mFixedPoint, mDragPoint);
        float fixedRadius = (float) (mFixedMaxRadius - dragFixedDistance / 14);
        if (fixedRadius > mFixedMinRadius) {
            //绘制固定圆
            canvas.drawCircle(mFixedPoint.x, mFixedPoint.y, fixedRadius, mPaint);
            Path bezierPath = getBezierPath(mFixedPoint, mDragPoint);
            canvas.drawPath(bezierPath, mPaint);
        }
        canvas.drawBitmap(mDragBitmap, mDragPoint.x - mDragBitmap.getWidth() / 2
                , mDragPoint.y - mDragBitmap.getHeight() / 2, null);
    }

    private Path getBezierPath(PointF fixedPoint, PointF dagPoint) {
        double dragFixedDistance = getDragFixedDistance(mFixedPoint, mDragPoint);
        float fixedRadius = (float) (mFixedMaxRadius - dragFixedDistance / 14);
        float dx = Math.abs(fixedPoint.x - dagPoint.x);
        float dy = Math.abs(fixedPoint.y - dagPoint.y);
        float tanA = dy / dx;
        float a = (float) Math.atan(tanA);

        //P0 点
        if (mP0 == null) {
            mP0 = new PointF();
        }
        mP0.x = mDragPoint.x + (int) (mDragRadius * Math.sin(a));
        mP0.y = mDragPoint.y - (int) (mDragRadius * Math.cos(a));

        //P1 点
        if (mP1 == null) {
            mP1 = new PointF();
        }
        mP1.x = mFixedPoint.x + (int) (fixedRadius * Math.sin(a));
        mP1.y = mFixedPoint.y - (int) (fixedRadius * Math.cos(a));


        //P2 点
        if (mP2 == null) {
            mP2 = new PointF();
        }
        mP2.x = mFixedPoint.x - (int) (fixedRadius * Math.sin(a));
        mP2.y = mFixedPoint.y + (int) (fixedRadius * Math.cos(a));


        //P0 点
        if (mP3 == null) {
            mP3 = new PointF();
        }
        mP3.x = mDragPoint.x - (int) (mDragRadius * Math.sin(a));
        mP3.y = mDragPoint.y + (int) (mDragRadius * Math.cos(a));


        //绘制路径
        Path path = new Path();
        path.moveTo(mP0.x, mP0.y);

        //控制点选择固定圆和拖拽圆的中心点
        PointF controlPoint = getControlPoint();
        path.quadTo(controlPoint.x, controlPoint.y, mP1.x, mP1.y);

        path.lineTo(mP2.x, mP2.y);
        path.quadTo(controlPoint.x, controlPoint.y, mP3.x, mP3.y);
        path.close();
        return path;
    }

    private PointF getControlPoint() {
        if (mControlPoint == null) {
            mControlPoint = new PointF();
        }
        mControlPoint.x = (mDragPoint.x + mFixedPoint.x) / 2;
        mControlPoint.y = (mDragPoint.y + mFixedPoint.y) / 2;
        return mControlPoint;
    }

    private double getDragFixedDistance(PointF fixedPoint, PointF dagPoint) {
        return Math.sqrt((dagPoint.x - fixedPoint.x) * (dagPoint.x - fixedPoint.x) + (dagPoint.y - fixedPoint.y) * (dagPoint.y - fixedPoint.y));
    }

    public static void attach(View view, BubbleMessageTouchListener.BubbleDisappearListener bubbleTouchListener) {
        view.setOnTouchListener(new BubbleMessageTouchListener(view, view.getContext(),bubbleTouchListener));
    }

    public void setDragBitmap(Bitmap dragBitmap) {
        mDragBitmap = dragBitmap;
        invalidate();
    }

    public void handleActionUp() {
        double dragFixedDistance = getDragFixedDistance(mFixedPoint, mDragPoint);
        float fixedRadius = (float) (mFixedMaxRadius - dragFixedDistance / 14);
        if (fixedRadius > mFixedMinRadius) {
            //回弹
            // 0 - 1
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1);
            valueAnimator.setDuration(250);
            final PointF start = new PointF(mFixedPoint.x, mFixedPoint.y);
            final PointF end = new PointF(mDragPoint.x, mDragPoint.y);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    PointF pointF = BubbleUtils.getPointByPercent(end, start, percent);
                    updateDragPoint(pointF.x, pointF.y);
                }
            });
            valueAnimator.setInterpolator(new OvershootInterpolator(3f));
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mListener != null) {
                        mListener.restore();
                    }
                }
            });
            valueAnimator.start();
        } else {
            //爆炸
            if (mListener != null){
                mListener.dismiss(mDragPoint);
            }
        }
    }


    private MessageBubbleListener mListener;

    public void setMessageBubbleListener(MessageBubbleListener listener) {
        this.mListener = listener;
    }

    public interface MessageBubbleListener {
        // 还原
        public void restore();

        // 消失爆炸
        public void dismiss(PointF pointF);
    }
}
