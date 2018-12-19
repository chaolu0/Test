package com.shxy.datasharedplatform;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.shxy.datasharedplatform.bean.CommentBean;
import com.shxy.datasharedplatform.bean.CommentRecv;
import com.shxy.datasharedplatform.utils.MainConfig;
import com.shxy.datasharedplatform.utils.OkHttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by shxy on 2018/12/13.
 */

public class CommentActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TextView noCommentTextView;
    private EditText commentEditText;
    private Button btn;
    private List<CommentBean> mData = new LinkedList<>();
    private CommentAdapter mAdapter;
    private int page = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        final int itemId = getIntent().getExtras().getInt("item_id");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        noCommentTextView = (TextView) findViewById(R.id.no_comment);
        noCommentTextView.setVisibility(View.GONE);
        commentEditText = (EditText) findViewById(R.id.comment_edit);
        btn = (Button) findViewById(R.id.btn);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommentAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);

        fetchComment(itemId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpping)
                    return;
                isUpping = true;
                upComment(itemId);
            }
        });

    }

    private boolean isUpping = false;

    private void upComment(final int itemId) {
        Map<String, String> map = new HashMap<>(3);
        map.put("iid", itemId + "");
        map.put("uid", getSharedPreferences(MainConfig.MAIN_SP_FILE, MODE_PRIVATE).getString("uid", ""));
        map.put("comment", commentEditText.getText().toString());
        OkHttpUtils.basePostAsync("upload_comment", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isUpping = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                isUpping = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        commentEditText.setText("");
                        Toast.makeText(getApplicationContext(), "评论成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void fetchComment(int itemId) {
        page++;
        final Map<String, String> map = new HashMap<>(2);
        map.put("iid", itemId + "");
        map.put("page", page + "");
        OkHttpUtils.baseGetAsync("fetch_comments", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                page--;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final CommentRecv recv = new Gson().fromJson(response.body().string(), CommentRecv.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mData.addAll(recv.getData());
                        if (mData.size() == 0) {
                            noCommentTextView.setVisibility(View.VISIBLE);
                        } else {
                            noCommentTextView.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }


    private static class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private List<CommentBean> mData;

        public void setmData(List<CommentBean> mData) {
            this.mData = mData;
        }

        public CommentAdapter(Context mContext, List<CommentBean> mData) {
            this.mContext = mContext;
            this.mData = mData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
            CommentViewHolder holder = new CommentViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
            commentViewHolder.nick.setText(mData.get(position).getNickname());
            commentViewHolder.comment.setText(mData.get(position).getComment());
            Glide.with(mContext)
                    .load(MainConfig.MAIN_URL +mData.get(position).getPhoto_path())
                    .into(commentViewHolder.img);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView nick;
        TextView comment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            nick = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}
