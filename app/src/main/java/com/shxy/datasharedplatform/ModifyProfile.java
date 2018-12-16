package com.shxy.datasharedplatform;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shxy.datasharedplatform.bean.BaseMsg;
import com.shxy.datasharedplatform.utils.MainConfig;
import com.shxy.datasharedplatform.utils.OkHttpUtils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by shxy on 2018/12/15.
 */

public class ModifyProfile extends BaseActivity {

    public static final String TITLE_KEY = "title_key";
    public static final String INFO_KEY = "info_key";
    public static final String HINT_KEY = "hint_key";
    public static final String TYPE_KEY = "type_key";

    public static final int NICKNAME_TYPE = 0x1;
    public static final int INFO_TYPE = 0x3;
    private int type;
    ProgressBar progressBar;
    TextView titleV;
    EditText infoV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE_KEY);
        final String info = intent.getStringExtra(INFO_KEY);
        String hint = intent.getStringExtra(HINT_KEY);
        type = intent.getIntExtra(TYPE_KEY,0);

        titleV = (TextView) findViewById(R.id.title);
        infoV = (EditText) findViewById(R.id.info);
        TextView hintV = (TextView) findViewById(R.id.hint);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        titleV.setText(title);
        infoV.setText(info);
        hintV.setText(hint);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendModefyRequest();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent genarateIntent(Activity a, String title, String info, String hint, int type) {
        Intent intent = new Intent(a, ModifyProfile.class);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(INFO_KEY, info);
        intent.putExtra(HINT_KEY, hint);
        intent.putExtra(TYPE_KEY,type);
        return intent;
    }

    private void sendModefyRequest() {
        SharedPreferences sp = getSharedPreferences(MainConfig.MAIN_SP_FILE, MODE_PRIVATE);
        HashMap<String, String> map = new HashMap<>();
        if ((type & Integer.MAX_VALUE) == NICKNAME_TYPE){
            map.put("name", "nickname");
        }else{
            map.put("name", "personal_sign");
        }
        map.put("new_value",infoV.getText().toString());
        map.put("uid", sp.getString(MainConfig.UID_KEY, ""));
        map.put("SK", sp.getString(MainConfig.SK_KEY, ""));
        progressBar.setVisibility(View.VISIBLE);
        OkHttpUtils.basePostAsync("modify_info", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        BaseMsg msg = new Gson().fromJson(string,BaseMsg.class);
                        if (msg.getState() == 1){
                            Toast.makeText(ModifyProfile.this,"上传成功" , Toast.LENGTH_SHORT).show();
                            SharedPreferences sp = getSharedPreferences(MainConfig.MAIN_SP_FILE, MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            if ((type & Integer.MAX_VALUE) == NICKNAME_TYPE){
                                edit.putString(MainConfig.NICK_NAME_KEY,infoV.getText().toString());
                            }else{
                                edit.putString(MainConfig.INFO_KEY,infoV.getText().toString());
                            }
                            edit.apply();
                        }
                    }
                });
            }
        });
    }
}
