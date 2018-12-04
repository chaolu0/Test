package com.shxy.datasharedplatform.controller;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.shxy.datasharedplatform.R;

import java.util.Arrays;

/**
 * Created by shxy on 2018/12/4.
 */

public class SubmitButtonController {
    private EditText editText1;
    private EditText editText2;
    private Button button;
    private Context context;
    private boolean[] isOk = new boolean[2];
    private boolean lastState = false;

    public SubmitButtonController(Context context, Button button, EditText editText1, EditText editText2) {
        this.context = context;
        this.button = button;
        this.editText1 = editText1;
        this.editText2 = editText2;
        Arrays.fill(isOk, false);
        editText1.addTextChangedListener(new TextWatcher() {
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
        editText2.addTextChangedListener(new TextWatcher() {
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
    }

    private void setLoginButtonBackground() {
        if ((isOk[0] && isOk[1]) == lastState)
            return;
        lastState = !lastState;
        button.setEnabled(lastState);
        button.setBackground(lastState ?
                context.getDrawable(R.drawable.selector_lar_button) :
                context.getDrawable(R.drawable.selector_lar_button_disable));
    }
}
