package com.shxy.okhttputils;


import com.shxy.okhttputils.request.OkHttpRequest;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by shxy on 2019/1/11.
 */

public class RequestCall {
    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;

    public RequestCall(OkHttpRequest okHttpRequest) {
        this.okHttpRequest = okHttpRequest;
    }

    public void execute(CallBack callBack) {
        buildCall(callBack);

        OkHttpUtils.execute(this, callBack);
    }

    private Call buildCall(CallBack callBack) {
        request = okHttpRequest.generateRequest();

        call = OkHttpUtils.getClient().newCall(request);
        return call;
    }

    public Call getCall() {
        return call;
    }
}
