package com.zsj.lib.androidlib.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;


/**
 * 创建者     朱胜军
 * 创建时间   2017/10/11 20:21
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
