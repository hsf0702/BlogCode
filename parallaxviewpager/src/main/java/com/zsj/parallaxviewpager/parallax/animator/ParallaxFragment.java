package com.zsj.parallaxviewpager.parallax.animator;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.zsj.parallaxviewpager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 朱胜军
 * @date 2018/7/22
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class ParallaxFragment extends Fragment implements LayoutInflaterFactory {
    public static final String FRAGMENT_ID_KEY = "FRAGMENT_ID_KEY";
    private static final String TAG = "zsjTAG";
    private CompatViewInflater mCompatViewInflater;
    private List<View> mParallaxViews = new ArrayList<>();
    private int[] mParallaxAttrs = new int[]{
            R.attr.translationXIn,
            R.attr.translationXOut,
            R.attr.translationYIn,
            R.attr.translationYOut
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        int fragmentResId = arguments.getInt(FRAGMENT_ID_KEY, -1);
        // 2.2.2 把所有需要移动的属性解析出来，内涵端子插件式换肤
        // View创建的时候 我们去解析属性  这里传 inflater 有没有问题？ 单例设计模式 代表着所有的View的创建都会是该 Fragment 去创建的
        inflater = inflater.cloneInContext(getActivity());// 克隆一个出来
        LayoutInflaterCompat.setFactory(inflater, this);
        return inflater.inflate(fragmentResId, container, false);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // View都会来这里,创建View
        // 拦截到View的创建  获取View之后要去解析
        // 1. 创建View
        // If the Factory didn't handle it, let our createView() method try
        View view = createView(parent, name, context, attrs);

        // 2.1 一个activity的布局肯定对应多个这样的 SkinView
        if (view != null) {
            // Log.e("TAG", "我来创建View");
            // 解析所有的我们自己关注属性
            analysisAttrs(view, context, attrs);
        }
        return view;
    }

    private void analysisAttrs(View view, Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, mParallaxAttrs);
        if (typedArray != null && typedArray.getIndexCount() != 0) {
            int indexCount = typedArray.getIndexCount();
            ParallaxTag tag = new ParallaxTag();
            for (int i = 0; i < indexCount; i++) {
                int attr = typedArray.getIndex(i);
                switch (attr) {
                    case 0:
                        tag.translationXIn = typedArray.getFloat(attr, 0f);
                        break;
                    case 1:
                        tag.translationXOut = typedArray.getFloat(attr, 0f);
                        break;
                    case 2:
                        tag.translationYIn = typedArray.getFloat(attr, 0f);
                        break;
                    case 3:
                        tag.translationYOut = typedArray.getFloat(attr, 0f);
                        break;

                    default:
                        break;
                }
            }
            view.setTag(R.id.parallax_tag,tag);
            mParallaxViews.add(view);
        }
        typedArray.recycle();
    }

    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mCompatViewInflater == null) {
            mCompatViewInflater = new CompatViewInflater();
        }

        // We only want the View to inherit it's context if we're running pre-v21
        final boolean inheritContext = isPre21 && true
                && shouldInheritContext((ViewParent) parent);

        return mCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true /* Read read app:theme as a fallback at all times for legacy reasons */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (!(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    public List<View> getParallaxViews() {
        return mParallaxViews;
    }
}
