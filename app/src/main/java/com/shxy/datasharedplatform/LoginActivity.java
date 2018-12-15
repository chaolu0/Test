package com.shxy.datasharedplatform;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shxy.datasharedplatform.bean.LoginMessage;
import com.shxy.datasharedplatform.utils.MainConfig;
import com.shxy.datasharedplatform.utils.OkHttpUtils;
import com.shxy.datasharedplatform.controller.SubmitButtonController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by shxy on 2018/12/2.
 */

public class LoginActivity extends BaseActivity implements
        View.OnClickListener {
    private EditText usernameEdit;
    private EditText passwordEdit;
    private ProgressBar progressBar;
    private Button loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(this);
        loginButton.setEnabled(false);
        usernameEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        new SubmitButtonController(this, loginButton, passwordEdit, usernameEdit);
        findViewById(R.id.go_to_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                sendLoginHttp();
                break;
            case R.id.go_to_register:
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 1);
//                finish();
                break;
        }
    }

    private void sendLoginHttp() {
        Map<String, String> map = new HashMap<>();
        map.put("username", usernameEdit.getText().toString());
        map.put("password", passwordEdit.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
        OkHttpUtils.basePostAsync("login", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final LoginMessage loginMessage = new Gson().fromJson(response.body().string(), LoginMessage.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        if (loginMessage.getState() == 1) {
                            saveInfo(loginMessage);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, loginMessage.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void saveInfo(LoginMessage loginMessage) {
        SharedPreferences.Editor edit = getSharedPreferences(MainConfig.MAIN_SP_FILE, MODE_PRIVATE).edit();
        edit.putString(MainConfig.UID_KEY, loginMessage.getId() + "");
        edit.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            usernameEdit.setText(data.getExtras().getString("email"));
            passwordEdit.setText(data.getExtras().getString("password"));
        }
    }
}
