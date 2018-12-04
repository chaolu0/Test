package com.shxy.datasharedplatform.utils;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by shxy on 2018/12/4.
 */

public class OkHttpUtils {
    private static final String baseUrl = "http://192.168.0.105:8080/";

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
}
