package com.zsj.lib.androidlib.net;

import android.content.Context;

import com.zsj.lib.androidlib.net.callback.IError;
import com.zsj.lib.androidlib.net.callback.IFailure;
import com.zsj.lib.androidlib.net.callback.IRequest;
import com.zsj.lib.androidlib.net.callback.ISuccess;
import com.zsj.lib.androidlib.ui.loader.LoaderStyle;

import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 创建者     朱胜军
 * 创建时间   2017/10/16 22:16
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class RestClientBuilder {
    private String mUrl;
    private WeakHashMap<String, Object> mParams = RestCreator.getParams();
    private IRequest mRequest;
    private ISuccess mSuccess;
    private IFailure mFailure;
    private IError mError;
    private RequestBody mBody;
    private Context mContext;
    private LoaderStyle mLoaderStyle;

    /**
     * 不能被包外的创建对象
     */
    RestClientBuilder() {

    }

    public RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public RestClientBuilder params(Map<String, Object> params) {
        this.mParams.putAll(params);
        return this;
    }

    public RestClientBuilder params(String key, Object value) {
        this.mParams.put(key, value);
        return this;
    }


    public RestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset = UTF-8"), raw);
        return this;
    }


    public RestClientBuilder request(IRequest request) {
        this.mRequest = request;
        return this;
    }

    public RestClientBuilder success(ISuccess success) {
        this.mSuccess = success;
        return this;
    }

    public RestClientBuilder failure(IFailure failure) {
        this.mFailure = failure;
        return this;
    }


    public RestClientBuilder error(IError error) {
        this.mError = error;
        return this;
    }

    public RestClientBuilder loaderStyle(Context context, LoaderStyle loaderStyle) {
        this.mContext = context;
        this.mLoaderStyle = loaderStyle;
        return this;
    }

    public RestClientBuilder loaderStyle(Context context) {
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallBeatIndicator;
        return this;
    }

    public final RestClient build() {
        return new RestClient(mUrl, mParams, mRequest, mSuccess, mFailure, mError, mBody, mContext, mLoaderStyle);
    }

}
