package com.shxy.datasharedplatform;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by shxy on 2018/12/13.
 */

public class CommentActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TextView noComentTextView;
    private EditText commentEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        int itemId = getIntent().getExtras().getInt("item_id");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        noComentTextView = (TextView) findViewById(R.id.no_comment);
        commentEditText = (EditText) findViewById(R.id.comment_edit);
    }


    private static class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
            CommentViewHolder holder = new CommentViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public CommentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
