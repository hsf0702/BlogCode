package com.zsj.parallaxviewpager.parallax.animator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zsj.parallaxviewpager.R;

import java.util.ArrayList;
import java.util.List;

import static com.zsj.parallaxviewpager.parallax.animator.ParallaxFragment.FRAGMENT_ID_KEY;

/**
 * @author 朱胜军
 * @date 2018/7/22
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class ParallaxViewPager extends ViewPager {

    private static final String TAG = "zsjTAG";
    private ArrayList<ParallaxFragment> mParallaxFragments = new ArrayList<>();

    public ParallaxViewPager(@NonNull Context context) {
        this(context, null);
    }

    public ParallaxViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLayout(FragmentManager fm, int[] fragmentIds) {
        for (int i = 0; i < fragmentIds.length; i++) {
            ParallaxFragment parallaxFragment = new ParallaxFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(FRAGMENT_ID_KEY, fragmentIds[i]);
            parallaxFragment.setArguments(bundle);
            mParallaxFragments.add(parallaxFragment);
        }
        setAdapter(new MyPagerAdapter(fm));

        setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //监听滑动改变位移
                ParallaxFragment outFragment = mParallaxFragments.get(position);
                List<View> parallaxViews = outFragment.getParallaxViews();
                for (View parallaxView : parallaxViews) {
                    ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                    parallaxView.setTranslationX((-positionOffsetPixels)*tag.translationXOut);
                    parallaxView.setTranslationY((-positionOffsetPixels)*tag.translationYOut);
                }


                try {
                    ParallaxFragment inFragment = mParallaxFragments.get(position + 1);

                    parallaxViews = inFragment.getParallaxViews();
                    for (View parallaxView : parallaxViews) {
                        ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                        parallaxView.setTranslationX((getMeasuredWidth()-positionOffsetPixels)*tag.translationXIn);
                        parallaxView.setTranslationY((getMeasuredWidth()-positionOffsetPixels)*tag.translationYIn);
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mParallaxFragments.get(position);
        }

        @Override
        public int getCount() {
            return mParallaxFragments.size();
        }
    }
}
