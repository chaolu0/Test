package com.shxy.datasharedplatform;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.shxy.datasharedplatform.utils.MainConfig;

/**
 * Created by shxy on 2018/12/19.
 */

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkLogin();
                    }
                });
            }
        }.start();
    }

    private void checkLogin() {
        SharedPreferences sp = getSharedPreferences(MainConfig.MAIN_SP_FILE, MODE_PRIVATE);
        boolean need = !sp.getBoolean(MainConfig.LOGIN_KEY, false);
        Intent intent = null;
        if (need) {
            intent = new Intent(this, LoginActivity.class);

        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
