package com.zsj.lib.androidlib.ui.loader;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.wang.avi.AVLoadingIndicatorView;
import com.zsj.lib.androidlib.R;
import com.zsj.lib.androidlib.ui.dimen.DimenUtil;

import java.util.ArrayList;

/**
 * 创建者     朱胜军
 * 创建时间   2017/10/17 23:59
 * 描述	      Loader
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class LatteLoader {
    private static final int LOADER_SIZE_SCALE = 8;
    private static final int LOADER_OFFSET_SCALE = 10;
    private static final String DEFAULT_DIALOG_STYLE = LoaderStyle.BallBeatIndicator.name();

    private static final ArrayList<AppCompatDialog> DIALOG_ARRAY_LIST = new ArrayList<>();


    public static void showLoading(Context context) {
        showLoading(context, DEFAULT_DIALOG_STYLE);
    }

    public static void showLoading(Context context, Enum<LoaderStyle> type) {
        showLoading(context, type.name());
    }

    /**
     * 显示Dialog
     *
     * @param context
     * @param type
     */
    public static void showLoading(Context context, String type) {
        AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);

        //自定义Dialog的内容
        AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(context, type);
        dialog.setContentView(avLoadingIndicatorView);

        int deviceHeight = DimenUtil.getScreenHeight();
        int deviceWidth = DimenUtil.getScreenWidth();

        final Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = deviceWidth / LOADER_SIZE_SCALE;
            lp.height = deviceHeight / LOADER_SIZE_SCALE + LOADER_OFFSET_SCALE;
            lp.gravity = Gravity.CENTER;
        }
        //添加进入集合中
        DIALOG_ARRAY_LIST.add(dialog);
        dialog.show();
    }

    public static void stopLoad() {
        for (AppCompatDialog dialog : DIALOG_ARRAY_LIST) {
            if (dialog != null) {
                dialog.cancel();
            }
        }
    }
}
