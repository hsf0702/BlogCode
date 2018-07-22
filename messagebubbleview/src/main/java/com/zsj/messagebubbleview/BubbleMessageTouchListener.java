package com.zsj.messagebubbleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author 朱胜军
 * @date 2018/7/21
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class BubbleMessageTouchListener implements View.OnTouchListener, MessageBubbleView.MessageBubbleListener {
    private final Context mContext;
    private final WindowManager mWindowManager;
    private final WindowManager.LayoutParams mParams;
    private View mView;
    private final MessageBubbleView mMessageBubbleView;
    private BubbleDisappearListener mBubbleTouchListener;
    private FrameLayout mFrameLayout;
    private ImageView mBombImage;

    BubbleMessageTouchListener(View view, Context context, BubbleDisappearListener bubbleTouchListener) {
        mView = view;
        this.mContext = context;
        mMessageBubbleView = new MessageBubbleView(view.getContext());
        mBubbleTouchListener = bubbleTouchListener;
        mMessageBubbleView.setMessageBubbleListener(this);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        // 背景要透明
        mParams.format = PixelFormat.TRANSPARENT;

        mFrameLayout = new FrameLayout(context);
        mBombImage = new ImageView(context);
        mFrameLayout.addView(mBombImage);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mBombImage.getLayoutParams();
        layoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        mBombImage.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 要在WindowManager上面搞一个View ,上一节写好的贝塞尔的View
                mWindowManager.addView(mMessageBubbleView, mParams);
                //获取当前View的bitmap
                int[] location = new int[2];
                mView.getLocationOnScreen(location);
                Bitmap bitmap = getBitmapByView(mView);
                //初始化固定圆
                mMessageBubbleView.setDragBitmap(bitmap);
                mMessageBubbleView.initFixedPoint(location[0] + mView.getWidth() / 2,
                        location[1] + mView.getWidth() / 2 - BubbleUtils.getStatusBarHeight(mContext));
                //按下,隐藏当前的view.
                mView.setVisibility(View.INVISIBLE);
                break;
            case MotionEvent.ACTION_MOVE:
                mMessageBubbleView.updateDragPoint(event.getRawX(), event.getRawY() - BubbleUtils.getStatusBarHeight(mContext));
                break;
            case MotionEvent.ACTION_UP:
                mMessageBubbleView.handleActionUp();
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * 从一个View中获取Bitmap
     *
     * @param view
     * @return
     */
    private Bitmap getBitmapByView(View view) {
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    @Override
    public void restore() {
        mWindowManager.removeView(mMessageBubbleView);
        mView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss(PointF pointF) {
        mWindowManager.removeView(mMessageBubbleView);
        // 要在 mWindowManager 添加一个爆炸动画
        mWindowManager.addView(mFrameLayout,mParams);

        mBombImage.setBackgroundResource(R.drawable.anim_bubble_pop);

        AnimationDrawable drawable = (AnimationDrawable) mBombImage.getBackground();
        mBombImage.setX(pointF.x-drawable.getIntrinsicWidth()/2);
        mBombImage.setY(pointF.y-drawable.getIntrinsicHeight()/2);

        drawable.start();
        // 等它执行完之后我要移除掉这个 爆炸动画也就是 mBombFrame
        mBombImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWindowManager.removeView(mFrameLayout);
                // 通知一下外面该消失
                if(mBubbleTouchListener != null){
                    mBubbleTouchListener.dismiss();
                }
            }
        },getAnimationDrawableTime(drawable));
    }

    private long getAnimationDrawableTime(AnimationDrawable drawable) {
        int numberOfFrames = drawable.getNumberOfFrames();
        long time = 0;
        for (int i=0;i<numberOfFrames;i++){
            time += drawable.getDuration(i);
        }
        return time;
    }


    public interface BubbleDisappearListener {
        /**
         * 消失
         */
        void dismiss();
    }
}
