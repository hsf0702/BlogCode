package com.zsj.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zsj.lib.androidlib.ui.dimen.DimenUtil;

/**
 * @author 朱胜军
 * @date 2018/6/24
 * 描述	      TODO
 * <p>
 * 圆形进度条
 * 1. 绘制外圆
 * 2. 绘制进度圆
 * 3. 绘制中间文字
 * <p>
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class ProgressBar extends View {
    /**
     * 圆环的宽度
     */
    private int mRingWidth = 20;

    /**
     * 外圆的颜色
     */
    private int mOutCircleColor = Color.BLUE;

    /**
     * 内圆的颜色
     */
    private int mInnerCircleColor = Color.RED;

    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//         用到自定义属性,圆环的宽度,外圆的颜色,进度圆的颜色
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);

        mRingWidth = typedArray.getDimensionPixelOffset(R.styleable.ProgressBar_ringWidth, DimenUtil.dip2px(context, mRingWidth));

        mOutCircleColor = typedArray.getColor(R.styleable.ProgressBar_outCircleColor, mOutCircleColor);

        mInnerCircleColor = typedArray.getColor(R.styleable.ProgressBar_innerCircleColor, mInnerCircleColor);
        //回收
        typedArray.recycle();
    }

    
}
