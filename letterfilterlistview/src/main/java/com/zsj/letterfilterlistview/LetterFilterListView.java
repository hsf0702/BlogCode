package com.zsj.letterfilterlistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.zsj.corelibrary.utils.DimenUtils;

/**
 * @author 朱胜军
 * @date 2018/7/1
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class LetterFilterListView extends View {

    private static final String TAG = "LetterFilterListView";
    private Paint mDefaultPaint;
    private Paint mTouchPaint;
    private Rect mBounds;
    // 定义26个字母
    public static String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private String mTouchLetter;
    private OnTouchLetterListener mOnTouchLetterListener;


    public LetterFilterListView(Context context) {
        this(context, null);
    }

    public LetterFilterListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterFilterListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //1.1 自定义属性..略
        initDefaultPaint(context);
        initTouchPaint(context);
    }

    private void initDefaultPaint(Context context) {
        mDefaultPaint = new Paint();
        mDefaultPaint.setAntiAlias(true);
        mDefaultPaint.setColor(Color.BLACK);
        mDefaultPaint.setTextSize(DimenUtils.dip2px(context, 18));
    }

    private void initTouchPaint(Context context) {
        mTouchPaint = new Paint();
        mTouchPaint.setAntiAlias(true);
        mTouchPaint.setColor(Color.RED);
        mTouchPaint.setTextSize(DimenUtils.dip2px(context, 18));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //2 onMeasure
        //宽度,左右padding + 文字宽度
        //3.绘制中间文本
        String text = "A";
        //字体的宽度高度
        if (mBounds == null) {
            mBounds = new Rect();
        }
        mDefaultPaint.getTextBounds(text, 0, text.length(), mBounds);

        int width = mBounds.width() + getPaddingLeft() + getPaddingRight();

        //获取屏幕高度
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();

        int widthSize = resolveSize(width, widthMeasureSpec);
        int heightSize = resolveSize(height, heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //3 绘制
        //每个文字的高度空间
        int letterSpace = getMeasuredHeight() / LetterFilterListView.mLetters.length;
        for (int i = 0; i < LetterFilterListView.mLetters.length; i++) {
            //文字中间的位置
            // 0 --> 0 + letterSpace /2
            // 1 --> letterSpace + letterSpace /2
            int latterCenter = letterSpace * i + letterSpace / 2;
            //指定中间位置,绘制文本
            Paint.FontMetricsInt fontMetricsInt = mDefaultPaint.getFontMetricsInt();
            if (mBounds == null) {
                mBounds = new Rect();
            }
            mDefaultPaint.getTextBounds(mLetters[i], 0, mLetters[i].length(), mBounds);
            int x = getMeasuredWidth() / 2 - mBounds.width() / 2;

            int baseLine = latterCenter + (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;

            if (mLetters[i].equals(mTouchLetter)) {
                canvas.drawText(LetterFilterListView.mLetters[i], x, baseLine, mTouchPaint);
            } else {
                canvas.drawText(LetterFilterListView.mLetters[i], x, baseLine, mDefaultPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //4 onTouch
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //4.1 获取当前的位置
                float y = event.getY();
                //4.2 获取y对应的字母
                // 每个文字的高度空间
                int letterSpace = getMeasuredHeight() / LetterFilterListView.mLetters.length;
                // A = 0  <==> y / letterSpace =0
                int number = (int) (y / letterSpace);

                //4.4 范围为题
                if (number < 0) {
                    number = 0;
                }

                if (number > mLetters.length - 1) {
                    number = mLetters.length - 1;
                }
                String touchLetter = mLetters[number];
                //4.5 优化,减少invalidate的调用
                if (touchLetter.equals(mTouchLetter)) {
                    return true;
                }

                if (mOnTouchLetterListener != null) {
                    mOnTouchLetterListener.OnTouchLetter(touchLetter);
                }
                mTouchLetter = touchLetter;
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                //4.3 松手 恢复到原来的状态
                mTouchLetter = "";
                if (mOnTouchLetterListener != null) {
                    mOnTouchLetterListener.OnTouchLetter(mTouchLetter);
                }
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }


    //5 回调
    interface OnTouchLetterListener {
        void OnTouchLetter(String touchLetter);
    }

    public void setOnTouchLetterListener(OnTouchLetterListener onTouchLetterListener) {
        mOnTouchLetterListener = onTouchLetterListener;
    }

}
