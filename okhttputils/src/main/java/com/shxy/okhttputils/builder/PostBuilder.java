package com.shxy.okhttputils.builder;

import com.shxy.okhttputils.RequestCall;
import com.shxy.okhttputils.request.PostRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shxy on 2019/1/12.
 */

public class PostBuilder extends OkHttpRequestBuilder {

    private Map<String, Map<String, File>> files = new HashMap<>();

    @Override
    public RequestCall build() {
        return new PostRequest(mParams, mUrl, files).build();
    }

    @Override
    public PostBuilder addParam(String key, String value) {
        mParams.put(key, value);
        return this;
    }

    @Override
    public PostBuilder url(String url) {
        mUrl = url;
        return this;
    }

    public PostBuilder addFile(String key, File file) {
        if (!files.containsKey(key)) {
            files.put(key, new HashMap<String, File>());
        }
        files.get(key).put(file.getName(), file);
        return this;
    }
}
