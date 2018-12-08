package com.shxy.datasharedplatform;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

/**
 * Created by shxy on 2018/12/8.
 */

public class CreateDataActivity extends BaseActivity implements View.OnClickListener {

    private View mSendButton;
    private EditText mContent;
    private RecyclerView mRecyclerView;
    private View mAddImageButton;
    private View mBackView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_data);
        mSendButton = findViewById(R.id.send);
        mContent = (EditText) findViewById(R.id.content);
        mRecyclerView = (RecyclerView) findViewById(R.id.img_recycler_view);
        mAddImageButton = findViewById(R.id.add_img);
        mBackView = findViewById(R.id.back);

        mBackView.setOnClickListener(this);
        mAddImageButton.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add_img:
                selectImages();
                break;
        }
    }
    private void selectImages(){

    }
}
