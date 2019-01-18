package com.shxy.okhttputils.request;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by shxy on 2019/1/12.
 */

public class PostRequest extends OkHttpRequest {
    private Map<String,Map<String, File>> files;

    public PostRequest(Map<String, String> mParams, String mUrl, Map<String,Map<String, File>> files) {
        super(mParams, mUrl);
        this.files = files;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (files.size() > 0) {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            addParamsMulti(builder);
            addFiles(builder);
            return builder.build();
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder);
            return builder.build();
        }
    }

    private void addFiles(MultipartBody.Builder builder) {
        for (Map.Entry<String, Map<String,File>> entry : files.entrySet()) {
            for (Map.Entry<String,File> e:entry.getValue().entrySet()) {
                builder.addFormDataPart(entry.getKey(), e.getValue().getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), e.getValue()));
            }
        }
    }

    private void addParamsMulti(MultipartBody.Builder builder) {
        for (Map.Entry<String, String> entry : mParams.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
    }

    private void addParams(FormBody.Builder builder) {
        for (Map.Entry<String, String> entry : mParams.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }
}
