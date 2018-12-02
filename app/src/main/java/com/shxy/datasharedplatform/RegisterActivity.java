package com.shxy.datasharedplatform;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by shxy on 2018/12/2.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
