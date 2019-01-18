package com.shxy.okhttputils;

import android.os.Handler;
import android.os.Looper;

import com.shxy.okhttputils.builder.GetBuilder;
import com.shxy.okhttputils.builder.PostBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by shxy on 2018/12/15.
 */

public class OkHttpUtils {
    private static OkHttpClient client = null;
    private static Handler mainHandler;

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostBuilder post() {
        return new PostBuilder();
    }

    public static void main(String[] args) {
        OkHttpUtils.get().url("http://www.baidu.com").build().execute(null);
    }

    public static OkHttpClient getClient() {
        if (client == null) {
            synchronized (OkHttpUtils.class) {
                if (client == null) {
                    client = new OkHttpClient();
                    mainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return client;
    }

    public static void execute(RequestCall requestCall, final CallBack callBack) {
        requestCall.getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final byte[] bytes = response.body().bytes();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(bytes);
                    }
                });
            }
        });
    }
}
