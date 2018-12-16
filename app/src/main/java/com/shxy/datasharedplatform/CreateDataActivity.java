package com.shxy.datasharedplatform;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.shxy.datasharedplatform.utils.FileUtils;
import com.shxy.datasharedplatform.utils.MainConfig;
import com.shxy.datasharedplatform.utils.OkHttpUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by shxy on 2018/12/8.
 */

public class CreateDataActivity extends BaseActivity implements View.OnClickListener {

    private View mSendButton;
    private EditText mContent;
    private RecyclerView mRecyclerView;
    private View mAddImageButton;
    private View mBackView;
    private GridAdapter mAdapter;
    private List<Uri> mData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_data);
        mSendButton = findViewById(R.id.send);
        mContent = (EditText) findViewById(R.id.content);
        mRecyclerView = (RecyclerView) findViewById(R.id.img_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(10, 3));
        mAdapter = new GridAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);
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
            case R.id.send:
                sendData();
                break;
        }
    }

    private void sendData() {

        String id = getSharedPreferences(MainConfig.MAIN_SP_FILE, MODE_PRIVATE).getString("uid", "");
        String url = null;
        Map<String, String> params = new HashMap<>();
        params.put("content", mContent.getText().toString());
        params.put("id", id);
        if (mData.size() > 0) {
            params.put("count", mData.size() + "");
            Map<String, File> fileParams = new HashMap<>();
            for (int i = 0; i < mData.size(); i++) {
                fileParams.put("img", FileUtils.getFileByUri(mData.get(i), this));
            }
            url = "upload_item_type2";
            params.put("type", "2");
            OkHttpUtils.filePostAsync(url, params, fileParams, callback);
        } else {
            url = "upload_item";
            params.put("type", "1");
            OkHttpUtils.basePostAsync(url, params, callback);
        }

    }

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CreateDataActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(CreateDataActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    };

    private static final int REQUEST_CODE_CHOOSE = 1;

    private void selectImages() {
        Matisse.from(CreateDataActivity.this)
                .choose(MimeType.allOf()) // 选择 mime 的类型
                .countable(true)
                .maxSelectable(9) // 图片选择的最多数量
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            Toast.makeText(this, mSelected.size() + "", Toast.LENGTH_SHORT).show();
            mData = mSelected;
            mAdapter.setmList(mData);
            mAdapter.notifyDataSetChanged();
        }
    }

    private static class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Uri> mList;
        private Context mContext;

        public GridAdapter(Context mContext, List<Uri> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        public void setmList(List<Uri> mList) {
            this.mList = mList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.grid_img_item, parent, false);
            return new GridViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((GridViewHolder) holder).img.setImageURI(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private static class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public GridViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }



}
