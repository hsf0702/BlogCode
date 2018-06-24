package com.zsj.lib.androidlib.net;

import android.content.Context;

import com.zsj.lib.androidlib.net.callback.IError;
import com.zsj.lib.androidlib.net.callback.IFailure;
import com.zsj.lib.androidlib.net.callback.IRequest;
import com.zsj.lib.androidlib.net.callback.ISuccess;
import com.zsj.lib.androidlib.net.callback.RestRequestCallBack;
import com.zsj.lib.androidlib.ui.loader.LatteLoader;
import com.zsj.lib.androidlib.ui.loader.LoaderStyle;

import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * 创建者     朱胜军
 * 创建时间   2017/10/16 21:18
 * 描述	      网络请求框架
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class RestClient {
    private final String URL;
    private final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;
    private final Context CONTEXT;
    private final LoaderStyle LOADERSTYLE;

    public RestClient(String url, Map<String, Object> params, IRequest request, ISuccess success
            , IFailure failure, IError error, RequestBody body, Context context, LoaderStyle loaderstyle) {
        this.URL = url;
        this.CONTEXT = context;
        LOADERSTYLE = loaderstyle;
        this.PARAMS.putAll(params);
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private void request(HttpMethod httpMethod) {
        RestService restService = RestCreator.getRestService();
        Call<String> call = null;
        //开始网络请求
        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

//        显示Loading
        if (LOADERSTYLE != null) {
            LatteLoader.showLoading(CONTEXT, LOADERSTYLE);
        }

        switch (httpMethod) {
            case GET:
                call = restService.get(URL, PARAMS);
                break;
            case POST:
                call = restService.post(URL, PARAMS);
                break;
            case PUT:
                call = restService.put(URL, PARAMS);
                break;
            case DELETE:
                call = restService.delete(URL, PARAMS);
                break;
            default:
                break;
        }
        if (call != null) {
            call.enqueue(getRequestCallBack());
        }
    }

    private RestRequestCallBack getRequestCallBack() {
        return new RestRequestCallBack(REQUEST, SUCCESS, FAILURE, ERROR, LOADERSTYLE);
    }

    public void get() {
        request(HttpMethod.GET);
    }

    public void post() {
        request(HttpMethod.POST);
    }

    public void put() {
        request(HttpMethod.PUT);
    }

    public void delete() {
        request(HttpMethod.DELETE);
    }
}
