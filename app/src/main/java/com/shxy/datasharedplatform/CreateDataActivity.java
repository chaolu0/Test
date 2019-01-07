package com.shxy.datasharedplatform;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shxy.datasharedplatform.utils.FileUtils;
import com.shxy.datasharedplatform.utils.Glide4Engine;
import com.shxy.datasharedplatform.utils.MainConfig;
import com.shxy.datasharedplatform.utils.OkHttpUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by shxy on 2018/12/8.
 */

public class CreateDataActivity extends BaseActivity implements View.OnClickListener {

    private View mSendButton;
    private EditText mContent;
    private RecyclerView mRecyclerView;
    private View mAddImageButton;
    private View mAddVideoButton;
    private View mBackView;
    private GridAdapter mAdapter;
    private List<Uri> mData = new ArrayList<>();
    private boolean subSuccess = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_data);
        mSendButton = findViewById(R.id.send);
        mContent = (EditText) findViewById(R.id.content);
        mRecyclerView = (RecyclerView) findViewById(R.id.img_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(10, 0, 3));
        mAdapter = new GridAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);
        mAddImageButton = findViewById(R.id.add_img);
        mAddVideoButton = findViewById(R.id.add_video);
        mBackView = findViewById(R.id.back);

        mBackView.setOnClickListener(this);
        mAddImageButton.setOnClickListener(this);
        mAddVideoButton.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
        initProgressDialog();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("正在上传...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
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
            case R.id.add_video:
                selectVideos();
                break;
        }
    }


    private void sendData() {
        if (isUploading)
            return;
        isUploading = true;
        String id = getSharedPreferences(MainConfig.MAIN_SP_FILE, MODE_PRIVATE).getString(MainConfig.UID_KEY, "");
        String url = null;
        Map<String, String> params = new HashMap<>();
        params.put("content", mContent.getText().toString());
        params.put("id", id);
        progressDialog.show();
        switch (requestType) {
            case REQUEST_CODE_IMAGE:
                params.put("count", mData.size() + "");
                params.put("type", "2");
                Map<String, File> fileParams = new HashMap<>();
                for (int i = 0; i < mData.size(); i++) {
                    fileParams.put("img", FileUtils.getFileByUri(mData.get(i), this));
                }
                url = "upload_item_type2";

                OkHttpUtils.filePostAsync(url, params, fileParams, callback);
                break;
            case REQUEST_CODE_VIDEO:
                params.put("type", "3");
                Map<String, File> fileParam = new HashMap<>();
                fileParam.put("video", FileUtils.getFileByUri(mData.get(0), this));
                url = "upload_item_type3";
                OkHttpUtils.filePostAsync(url, params, fileParam, callback);
                break;
            case REQUEST_CODE_TEXT:
                url = "upload_item";
                params.put("type", "1");
                OkHttpUtils.basePostAsync(url, params, callback);
                break;
        }
    }

    private boolean isUploading = false;
    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            isUploading = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
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
                        progressDialog.dismiss();
                        Toast.makeText(CreateDataActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                        isUploading = false;
                        subSuccess = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    };

    private static final int REQUEST_CODE_IMAGE = 1;
    private static final int REQUEST_CODE_VIDEO = 2;
    private static final int REQUEST_CODE_TEXT = -1;
    private int requestType = REQUEST_CODE_TEXT;

    private void selectImages() {
        Matisse.from(CreateDataActivity.this)
                .choose(MimeType.ofImage()) // 选择 mime 的类型
                .showSingleMediaType(true)
                .countable(true)
                .maxSelectable(9) // 图片选择的最多数量
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)// 缩略图的比例
                .imageEngine(new Glide4Engine())// 使用的图片加载引擎
                .forResult(REQUEST_CODE_IMAGE); // 设置作为标记的请求码

    }

    private void selectVideos() {
        Matisse.from(CreateDataActivity.this)
                .choose(MimeType.ofVideo()) // 选择 mime 的类型
                .countable(true)
                .showSingleMediaType(true)
                .maxSelectable(1) // 图片选择的最多数量
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)// 缩略图的比例
                .imageEngine(new Glide4Engine())// 使用的图片加载引擎
                .forResult(REQUEST_CODE_VIDEO); // 设置作为标记的请求码
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_IMAGE || requestCode == REQUEST_CODE_VIDEO) && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            Toast.makeText(this, mSelected.size() + "", Toast.LENGTH_SHORT).show();
            mData = mSelected;
            mAdapter.setmList(mData);
            mAdapter.notifyDataSetChanged();
            if (mSelected.size() == 0) {
                requestType = REQUEST_CODE_TEXT;
            } else {
                requestType = requestCode;
            }
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
            Glide.with(mContext).load(mList.get(position)).into(((GridViewHolder)holder).img);
//            ((GridViewHolder) holder).img.setImageURI(mList.get(position));
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


    @Override
    public void onBackPressed() {
        if (!subSuccess && (mContent.getText().toString().length() != 0 || mData.size() != 0))
            new AlertDialog.Builder(this).setTitle("放弃编辑")
                    .setMessage("退出后内容将不会保留")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CreateDataActivity.super.onBackPressed();
                        }
                    }).create().show();
        else
            super.onBackPressed();
    }
}
