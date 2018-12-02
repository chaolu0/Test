package com.shxy.datasharedplatform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by shxy on 2018/12/2.
 */

public class LoginActivity extends BaseActivity implements
        View.OnClickListener {


    private Button loginButton;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private boolean[] isOk = new boolean[2];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Arrays.fill(isOk, false);
        loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(this);
        setLoginButtonBackground();
        loginButton.setEnabled(false);
        usernameEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);
        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isOk[0] = !(s.length() == 0);
                setLoginButtonBackground();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isOk[1] = !(s.length() == 0);
                setLoginButtonBackground();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.go_to_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                break;
            case R.id.go_to_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }
    }

    private void setLoginButtonBackground() {
        loginButton.setBackground(isOk[0] && isOk[1] ?
                getDrawable(R.drawable.selector_lar_button) :
                getDrawable(R.drawable.selector_lar_button_disable));
    }
}
