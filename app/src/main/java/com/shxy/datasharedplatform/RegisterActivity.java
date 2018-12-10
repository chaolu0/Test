package com.shxy.datasharedplatform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shxy.datasharedplatform.message.BaseMsg;
import com.shxy.datasharedplatform.utils.OkHttpUtils;
import com.shxy.datasharedplatform.controller.SubmitButtonController;
import com.shxy.datasharedplatform.utils.RegularExpressionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by shxy on 2018/12/2.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText usernameEdit;
    private EditText passwordEdit;
    private ProgressBar progressBar;
    private Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        new SubmitButtonController(this, registerButton, passwordEdit, usernameEdit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.register:
                if (!checkEmail()) {
                    Toast.makeText(this, "邮箱格式非法", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendRegisterHttp();
                break;
        }
    }

    private void sendRegisterHttp() {
        Map<String, String> map = new HashMap<>();
        map.put("username", usernameEdit.getText().toString());
        map.put("password", passwordEdit.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);
        OkHttpUtils.basePostAsync("register", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        registerButton.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                final BaseMsg msg = new Gson().fromJson(s, BaseMsg.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        registerButton.setEnabled(true);
                        if (msg.getState() == 1) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("email", usernameEdit.getText().toString());
                            bundle.putString("password", passwordEdit.getText().toString());
                            intent.putExtras(bundle);
                            setResult(1, intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, msg.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private boolean checkEmail() {
        return RegularExpressionUtils.matchEmail(usernameEdit.getText().toString());
    }
}
