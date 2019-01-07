package com.shxy.datasharedplatform;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.shxy.datasharedplatform.bean.BaseMsg;
import com.shxy.datasharedplatform.utils.OkHttpUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by shxy on 2019/1/6.
 */

public class UpLoadService extends IntentService{

    public static final String BUNDLE_NAME = "data";
    public static final String URL_KEY = "url";
    public static final String PARAM_KEY = "param";
    public static final String FILE_KEY = "file";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UpLoadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getBundleExtra(BUNDLE_NAME);
        String response = OkHttpUtils.filePost(bundle.getString(URL_KEY),
                (Map<String, String>) bundle.get(PARAM_KEY),
                (Map<String, File>) bundle.get(FILE_KEY));
        BaseMsg msg = new Gson().fromJson(response, BaseMsg.class);
        Intent toActivity = new Intent();
//        sendBroadcast();
    }
}
