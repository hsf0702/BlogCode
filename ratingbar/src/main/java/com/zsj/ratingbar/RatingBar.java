package com.zsj.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author 朱胜军
 * @date 2018/6/24
 * 描述	      TODO
 * 交互控件
 * 1. 刚进来初始化的样子
 * 2. 处理用户交互部分(onTouch())
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class RatingBar extends View {
    private static final String TAG = "RatingBar";
    /**
     * 没有触摸的星星
     */
    private Bitmap mStarNormal = null;

    /**
     * 触摸后的星星
     */
    private Bitmap mStarFocus = null;

    /**
     * 级别数
     */
    private int mGradeNumber = 5;
    private int mCurrentGrade;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        context.obtainStyledAttributes()
        //1.1 自定义属性 2张图片资源，评分的等级数量
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        int starNormalId = typedArray.getResourceId(R.styleable.RatingBar_starNormal, 0);
        if (starNormalId == 0) {
            throw new RuntimeException("starNormal 属性没有设置");
        }
        mStarNormal = BitmapFactory.decodeResource(getResources(), starNormalId);

        int starFocusId = typedArray.getResourceId(R.styleable.RatingBar_starFocus, 0);
        if (starFocusId == 0) {
            throw new RuntimeException("starFocus 属性没有设置");
        }
        mStarFocus = BitmapFactory.decodeResource(getResources(), starFocusId);

        mGradeNumber = typedArray.getInt(R.styleable.RatingBar_gradeNumber, mGradeNumber);
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //1.2 测量大小
        //高度 = 星星的高度
        int height = mStarNormal.getHeight();

        //宽度 = 每一个星星的宽度 *  星星的个数 + Padding +间隔
        int width = mStarNormal.getWidth() * mGradeNumber;

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //2. 处理用户交互部分(onTouch())
        Log.d(TAG, "onTouchEvent event =" + event.getAction());
        switch (event.getAction()) {
            //由于按下,移动,抬起都是处理一样的逻辑.所以写在一处
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
//            case MotionEvent.ACTION_UP:   //2.3.1 尽量减少onDraw()的调用
                //2.1 计算当前的触摸的分数.
                /**
                 * 假设每一个星星的宽度是50
                 *
                 * 触摸的位置是40 --> 1 分  <==>  event.getX()/宽度 +1
                 * 触摸的位置是80 --> 2分   <==>  event.getX()/宽度 +1
                 */
                int currentGrade = (int) (event.getX() / mStarNormal.getWidth()) + 1;

                //范围问题
                if (currentGrade < 0) {
                    currentGrade = 0;
                }

                if (currentGrade > mGradeNumber) {
                    currentGrade = mCurrentGrade;
                }

                //2.3.2如果分数和上一次是一致的就无需触发onDraw() 方法
                if (currentGrade == mCurrentGrade) {
                    return true;
                }

                mCurrentGrade = currentGrade;
                //再去刷新显示
                invalidate();// 2.3 invalidate() 会触发layout布局所有view的Draw方法-> onDraw()方法.尽量减少触发
                break;

            default:
                break;
        }


        return true;//2.2 super.onTouchEvent(event)默认值是false,不消费,第一次进来DOWN,之后DOWN以后的事件是进不来的
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //1.3 绘制刚进来的样子
        for (int i = 0; i < mGradeNumber; i++) {
            //paddingTop
            float top = 0;
            // 第一个星星: 0 , 第二个星星:一个星星的宽度
            float left = i * mStarNormal.getWidth();

            /**
             * 如果mCurrentGrade = 0 就绘制默认状态的星星 ==>  mCurrentGrade=0 > i=0 为 false
             * 如果mCurrentGrade = 1 就第一个绘制触摸的星星和其他位置绘制默认状态的星星
             * ==>  mCurrentGrade=1 > i=0 为 true 绘制触摸状态的星星
             * ==> mCurrentGrade=1 > i=1 为 false 绘制默认状态的星星
             */
            if (mCurrentGrade > i) {
                //绘制触摸状态的星星
                canvas.drawBitmap(mStarFocus, left, top, null);
            } else {
                //绘制默认状态的星星
                canvas.drawBitmap(mStarNormal, left, top, null);
            }

        }
    }
}
