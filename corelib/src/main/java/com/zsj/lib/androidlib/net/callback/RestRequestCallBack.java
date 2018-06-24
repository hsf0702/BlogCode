package com.zsj.lib.androidlib.net.callback;

import android.os.Handler;

import com.zsj.lib.androidlib.ui.loader.LatteLoader;
import com.zsj.lib.androidlib.ui.loader.LoaderStyle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 创建者     朱胜军
 * 创建时间   2017/10/16 23:59
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class RestRequestCallBack implements Callback<String> {
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final LoaderStyle LOADERSTYLE;
    private static final Handler mHandler = new Handler();

    public RestRequestCallBack(IRequest REQUEST, ISuccess SUCCESS, IFailure FAILURE, IError ERROR, LoaderStyle loaderstyle) {
        this.REQUEST = REQUEST;
        this.SUCCESS = SUCCESS;
        this.FAILURE = FAILURE;
        this.ERROR = ERROR;
        LOADERSTYLE = loaderstyle;
    }


    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
//            请求成功
            if (call.isExecuted()) {
                //执行成功
                if (SUCCESS != null) {
                    SUCCESS.onSuccess(response.body());
                }
            }
        } else {
//            请求失败
            if (ERROR != null) {
                ERROR.onError(response.code(), response.message());
            }
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if (FAILURE != null) {
            FAILURE.onFailure();
        }
    }

    private void onRequestFinish() {
        if (LOADERSTYLE != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LatteLoader.stopLoad();
                }
            }, 500);
        }
    }
}
