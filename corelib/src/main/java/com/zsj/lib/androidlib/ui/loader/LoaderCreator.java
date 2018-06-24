package com.zsj.lib.androidlib.ui.loader;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

/**
 * 创建者     朱胜军
 * 创建时间   2017/10/17 23:41
 * 描述	      加载动画的创建者
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class LoaderCreator {
    private static final WeakHashMap<String, Indicator> LOADING_MAP = new WeakHashMap<>();

    /**
     * 创建一个loadingView
     *
     * @param context
     * @param type    loadView的类型
     * @return
     */
    public static AVLoadingIndicatorView create(Context context, String type) {
        AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        if (LOADING_MAP.get(type) == null) {
            Indicator indicator = getIndicator(type);
            LOADING_MAP.put(type, indicator);
        }

        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(type));
        return avLoadingIndicatorView;
    }

    /**
     * 获取Indicator
     *
     * @param name Indicator的名字
     * @return
     */
    private static Indicator getIndicator(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        StringBuilder drawableClassName = new StringBuilder();
        if (!name.contains(".")) {
            drawableClassName.append(AVLoadingIndicatorView.class.getPackage().getName())
                    .append(".indicators")
                    .append(".");
        }
        drawableClassName.append(name);

        try {
            Class<?> drawableClass = Class.forName(drawableClassName.toString());
            return (Indicator) drawableClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
