package com.shxy.datasharedplatform.utils;

import java.io.File;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by shxy on 2018/12/4.
 */

public class OkHttpUtils {
    private static final String baseUrl = "http://183.175.11.154:8080/";

    public static void basePostAsync(String url, Map<String, String> params, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        String realUrl = baseUrl + url;
        Request request = new Request.Builder()
                .url(realUrl)
                .post(builder.build())
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }

    public static void baseGetAsync(String url, Map<String, String> params, Callback callback) {
        StringBuilder realUrl = new StringBuilder();
        realUrl.append(baseUrl).append(url).append('?');
        for (Map.Entry<String, String> entry : params.entrySet()) {
            realUrl.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        realUrl = realUrl.deleteCharAt(realUrl.length() - 1);

        Request request = new Request.Builder()
                .url(realUrl.toString())
                .get()
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }

    public static void filePostAsync(String url, Map<String, String> params,
                                     Map<String, File> fileParams, Callback callback) {
        MultipartBody.Builder mBuilder = new MultipartBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            mBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        String TYPE = "application/octet-stream";
        for (Map.Entry<String, File> entry : fileParams.entrySet()) {
            RequestBody body = RequestBody.create(MediaType.parse(TYPE), entry.getValue());
            mBuilder.addFormDataPart("img", entry.getKey(), body);
        }
        MultipartBody body = mBuilder.build();
        String realUrl = baseUrl + url;
        Request request = new Request.Builder()
                .url(realUrl)
                .post(body)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }

}
