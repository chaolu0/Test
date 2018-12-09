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

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

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

    }

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
            mAdapter.setmList(mSelected);
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

    public static class RecyclerItemDecoration extends RecyclerView.ItemDecoration {
        private int itemSpace;
        private int itemNum;

        /**
         * @param itemSpace item间隔
         * @param itemNum   每行item的个数
         */
        public RecyclerItemDecoration(int itemSpace, int itemNum) {
            this.itemSpace = itemSpace;
            this.itemNum = itemNum;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = itemSpace;
            if (parent.getChildLayoutPosition(view) % itemNum == 0) {  //parent.getChildLayoutPosition(view) 获取view的下标
                outRect.left = 0;
            } else {
                outRect.left = itemSpace;
            }

        }
    }

}
