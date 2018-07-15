package com.zsj.listdatascreenview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * @author 朱胜军
 * @date 2018/7/15
 * 描述	      菜单筛选
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
//是一个组合布局，extends LinearLayout 两层结构，上的Tab，下面菜单内容和阴影
public class ListDataScreenView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "ZsjTAG";
    /**
     * 阴影的颜色
     */
    private int mShadowColor = Color.parseColor("#88888888");
    private BaseMenuAdapter mAdapter;
    private LinearLayout mTabContainer;
    private FrameLayout mMenuContentContainer;
    private int mMenuContentHeight;
    /**
     * 当前的点击位置
     */
    private int mCurrentPosition = -1;
    private long ANIMATOR_DURATION = 350;
    private View mShadowView;
    /**
     * 动画在执行
     */
    private boolean mIsAnimatorRun = false;

    public ListDataScreenView(Context context) {
        this(context, null);
    }

    public ListDataScreenView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListDataScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initLayout();
    }

    private void initLayout() {
        //设置垂直布局
        setOrientation(VERTICAL);

        //组合布局可以分成两种写法
        //1  inflate 一个 Lyaout布局
//        LayoutInflater.from(getContext()).inflate()
        //2  代码实现
        //上面的TabLayout 是一个线性布局，然后均分。
        mTabContainer = new LinearLayout(getContext());
        //给TabContainer设置大小
        mTabContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        //添加到自己中
        addView(mTabContainer);

        //下面剩下的内容是阴影和菜单内容
        //用FrameLayout作为容器
        FrameLayout menuContainer = new FrameLayout(getContext());
        //先添加一个阴影，用View设置背景即可
        mShadowView = new View(getContext());
        mShadowView.setBackgroundColor(mShadowColor);
        mShadowView.setAlpha(0f);
//         添加到menuContainer中
        menuContainer.addView(mShadowView);
        mShadowView.setOnClickListener(this);
        //添加菜单内容
        mMenuContentContainer = new FrameLayout(getContext());
        mMenuContentContainer.setBackgroundColor(Color.WHITE);
        menuContainer.addView(mMenuContentContainer);

        LinearLayout.LayoutParams menuContainerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        menuContainerParams.weight = 1;
        menuContainer.setLayoutParams(menuContainerParams);

        //把才菜单阴影容器添加到自己身上。
        addView(menuContainer);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure ");
        ////菜单的高度应该的屏幕宽度的75％
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if ((mMenuContentHeight == 0) && height > 0) {
            mMenuContentHeight = (int) (height * 0.75);
            ViewGroup.LayoutParams params = mMenuContentContainer.getLayoutParams();
            params.height = mMenuContentHeight;
            mMenuContentContainer.setLayoutParams(params);

            //菜单内容默认收起来,向上位移 - mMenuContentHeight
            mMenuContentContainer.setTranslationY(-mMenuContentHeight);
        }
    }

    public void setAdapter(BaseMenuAdapter adapter) {
        mAdapter = adapter;

        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View tabView = mAdapter.getTabView(i, mTabContainer);
            setTabClickListener(i, tabView);
            mTabContainer.addView(tabView);
//            TabLayout布局均分
            LinearLayout.LayoutParams params = (LayoutParams) tabView.getLayoutParams();
            params.weight = 1;
            tabView.setLayoutParams(params);

            View menuContentView = mAdapter.getMenuContentView(i, mMenuContentContainer);
            //菜单内容叠在一起了
            //一开始全部菜单内容全部隐藏
            menuContentView.setVisibility(GONE);
            mMenuContentContainer.addView(menuContentView);
        }
        //TabLayout布局均分
        //菜单内容叠在一起了
        //菜单的高度应该的屏幕宽度的75％
        //菜单内容默认收起来
        //点击Tab打开菜单或者关闭菜单
        //点击阴影关闭菜单
        //菜单打开,Tab的颜色值变化,丢给Adapter处理
        //打开或者关闭菜单的动画还在执行就不处理东湖
        //菜单已打开,点击其他的TAB,切换而也不是关闭菜单
    }

    private void setTabClickListener(final int position, final View tabView) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition == -1) {
                    //没有打开菜单,去打开菜单
                    openMenu(position, tabView);
                } else {
                    //打开过菜单,去关闭菜单
                    if (position != mCurrentPosition) {
                        //菜单已打开,点击其他的TAB,切换而也不是关闭菜单
                        //切换菜单内容
                        //隐藏上一个菜单内容
                        View preMenuContent = mMenuContentContainer.getChildAt(mCurrentPosition);
                        preMenuContent.setVisibility(GONE);
                        //把上一个Tab设置为默认颜色
                        View tabView = mTabContainer.getChildAt(mCurrentPosition);
                        mAdapter.closeMenu(tabView);

                        mCurrentPosition = position;
                        View currentMenuContent = mMenuContentContainer.getChildAt(mCurrentPosition);
                        currentMenuContent.setVisibility(VISIBLE);
                        //把当期Tab设置点击颜色
                        View tabView2 = mTabContainer.getChildAt(mCurrentPosition);
                        mAdapter.openMenu(tabView2);

                    } else {
                        closeMenu();
                    }
                }
            }
        });
    }

    private void closeMenu() {
        if (mIsAnimatorRun) {
            return;
        }
        // 给菜单内容启动移动动画,同时给阴影启动一个透明度动画
        //位移动画
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContentContainer,
                "translationY", 0, -mMenuContentHeight);

        //透明度动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView, "alpha", 1f, 0f);

        animatorSet.playTogether(translationAnimator, alphaAnimator);
        animatorSet.setDuration(ANIMATOR_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimatorRun = true;
                View tabView = mTabContainer.getChildAt(mCurrentPosition);
                mAdapter.closeMenu(tabView);
            }


            @Override
            public void onAnimationEnd(Animator animation) {
                mIsAnimatorRun = false;
                //关闭菜单,隐藏菜单内容
                View menuContentView = mMenuContentContainer.getChildAt(mCurrentPosition);
                menuContentView.setVisibility(GONE);
                mCurrentPosition = -1;
            }
        });
        animatorSet.start();

    }

    /**
     * 打开菜单
     *
     * @param position
     * @param tabView
     */
    private void openMenu(final int position, final View tabView) {
        if (mIsAnimatorRun) {
            return;
        }
        // 给菜单内容启动移动动画,同时给阴影启动一个透明度动画
        //位移动画
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContentContainer,
                "translationY", -mMenuContentHeight, 0);

        //透明度动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView, "alpha", 0f, 1f);

        animatorSet.playTogether(translationAnimator, alphaAnimator);
        animatorSet.setDuration(ANIMATOR_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimatorRun = true;
                mAdapter.openMenu(tabView);
                mCurrentPosition = position;
                //打开菜单显示菜单内容
                View menuContentView = mMenuContentContainer.getChildAt(position);
                menuContentView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsAnimatorRun = false;
            }
        });
        animatorSet.start();
    }

    @Override
    public void onClick(View v) {
        closeMenu();
    }
}