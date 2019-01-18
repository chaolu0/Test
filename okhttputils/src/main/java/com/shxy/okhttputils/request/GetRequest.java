package com.shxy.okhttputils.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by shxy on 2019/1/11.
 */

public class GetRequest extends OkHttpRequest {
    public GetRequest(String url, Map<String, String> params) {
        super(params, url);
        this.mUrl = url;
        this.mParams = params;
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.get().build();
    }
}
