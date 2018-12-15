package com.shxy.datasharedplatform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by shxy on 2018/12/15.
 */

public class ModifyProfile extends AppCompatActivity {

    public static final String TITLE_KEY = "title_key";
    public static final String INFO_KEY = "info_key";
    public static final String HINT_KEY = "hint_key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE_KEY);
        final String info = intent.getStringExtra(INFO_KEY);
        String hint = intent.getStringExtra(HINT_KEY);

        TextView titleV = (TextView) findViewById(R.id.title);
        final EditText infoV = (EditText) findViewById(R.id.info);
        TextView hintV = (TextView) findViewById(R.id.hint);

        titleV.setText(title);
        infoV.setText(info);
        hintV.setText(hint);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newInfo = infoV.getText().toString();
                //start net work task
            }
        });
    }

    public static Intent genarateIntent(Activity a, String title, String info, String hint) {
        Intent intent = new Intent(a, ModifyProfile.class);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(INFO_KEY, info);
        intent.putExtra(HINT_KEY, hint);
        return intent;
    }
}
