package com.shxy.datasharedplatform;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shxy on 2018/12/2.
 */

public class BaseActivity extends AppCompatActivity {
    private static List<Activity> activitys = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        activitys.add(this);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        getWindow().setStatusBarColor(Color.parseColor("#ffffff"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activitys.remove(this);
    }

    protected void removeAllActivitys() {
        for (Activity a : activitys) {
            a.finish();
            activitys.remove(a);
        }
    }
}
