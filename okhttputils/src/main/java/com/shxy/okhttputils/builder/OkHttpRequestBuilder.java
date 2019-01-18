package com.shxy.okhttputils.builder;


import com.shxy.okhttputils.RequestCall;

import java.util.HashMap;

/**
 * Created by shxy on 2019/1/11.
 */

public abstract class OkHttpRequestBuilder {
    protected HashMap<String, String> mParams = new HashMap<>();
    protected String mUrl;

    abstract public RequestCall build();

    abstract public OkHttpRequestBuilder addParam(String key, String value);
    abstract public OkHttpRequestBuilder url(String url);
}
