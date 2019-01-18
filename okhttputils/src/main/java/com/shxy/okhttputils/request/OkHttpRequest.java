package com.shxy.okhttputils.request;



import com.shxy.okhttputils.RequestCall;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by shxy on 2019/1/11.
 */

public abstract class OkHttpRequest {
    protected Map<String, String> mParams;
    protected String mUrl;
    protected Request.Builder builder = new Request.Builder();

    public OkHttpRequest(Map<String, String> mParams, String mUrl) {
        this.mParams = mParams;
        this.mUrl = mUrl;

        builder.url(mUrl);
    }

    public RequestCall build() {
        return new RequestCall(this);
    }

    public Request generateRequest() {
        RequestBody requestBody = buildRequestBody();
        return buildRequest(requestBody);
    }



    protected abstract RequestBody buildRequestBody();

    protected abstract Request buildRequest(RequestBody requestBody);
}
