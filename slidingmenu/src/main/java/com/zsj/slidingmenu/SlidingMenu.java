package com.zsj.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.zsj.corelibrary.utils.DimenUtils;

/**
 * @author 朱胜军
 * @date 2018/7/8
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class SlidingMenu extends HorizontalScrollView {
    private int mMenuRightMargin = 0;
    private int mMenuWidth;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);

//        mMenuRightMargin =

        mMenuRightMargin = typedArray.getDimensionPixelSize(R.styleable.SlidingMenu_menuRightMargin, mMenuRightMargin);

        typedArray.recycle();
    }

    //宽度不对,全乱了.指定宽度
    @Override
    protected void onFinishInflate() {
        //这个方法是xml解析完毕调用
        super.onFinishInflate();

        //获取LinearLayout
        ViewGroup container = (ViewGroup) getChildAt(0);

        int childCount = container.getChildCount();
        if (childCount > 2) {
            new IllegalAccessException("不能超过2个子View");
        }
        //获取menu
        View menuView = container.getChildAt(0);
        //获取内容
        View contentView = container.getChildAt(1);

        //指定内容也的宽度
        ViewGroup.LayoutParams contentLayoutParams = contentView.getLayoutParams();
        //内容的宽度 = 屏幕的宽度
        contentLayoutParams.width = DimenUtils.getScreenWidth(getContext());
        contentView.setLayoutParams(contentLayoutParams);

        //指定menu的宽度
        ViewGroup.LayoutParams menuLayoutParams = menuView.getLayoutParams();
        //menu的宽度 = 屏幕的宽度 - mMenuRightMargin
        mMenuWidth = DimenUtils.getScreenWidth(getContext()) - mMenuRightMargin;
        menuLayoutParams.width = mMenuWidth;
        menuView.setLayoutParams(menuLayoutParams);

        //发现,初始化关闭没有作用
//        scrollTo(mMenuWidth, 0);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //初始化关闭menu
        scrollTo(mMenuWidth, 0);
    }
}
