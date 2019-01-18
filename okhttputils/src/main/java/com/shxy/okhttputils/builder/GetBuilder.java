package com.shxy.okhttputils.builder;

import android.net.Uri;

import com.shxy.okhttputils.RequestCall;
import com.shxy.okhttputils.request.GetRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by shxy on 2018/12/15.
 */

public class GetBuilder extends OkHttpRequestBuilder {
    private HashMap<String, String> mParams = new HashMap<>();
    private String mUrl;


    @Override
    public RequestCall build() {
        if (mParams.size() > 0) {
            mUrl = appendParams(mUrl, mParams);
        }
        return new GetRequest(mUrl, mParams).build();
    }

    @Override
    public GetBuilder addParam(String key, String value) {
        mParams.put(key, value);
        return this;
    }

    @Override
    public OkHttpRequestBuilder url(String url) {
        mUrl = url;
        return this;
    }

    protected String appendParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }
}
