package com.shxy.datasharedplatform.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.shxy.datasharedplatform.CommentActivity;
import com.shxy.datasharedplatform.DataFragment;
import com.shxy.datasharedplatform.R;
import com.shxy.datasharedplatform.bean.InformationBean;
import com.shxy.datasharedplatform.bean.RemarkMessage;
import com.shxy.datasharedplatform.utils.MainConfig;
import com.shxy.datasharedplatform.utils.OkHttpUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by shxy on 2018/12/19.
 * 这个类用于抽取dataFragment中adapter中的逻辑
 * 以便在CommentActivity中也加入DataItem
 * 只在dataFragment和CommentActivity中使用
 */
public class DataItemController {
    private ClickListener listener = new ClickListener();
    private Context mContext;

    public DataItemController(Context mContext) {
        this.mContext = mContext;
    }

    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_data_type2, parent, false);
        DataFragment.DataViewHolderType1 viewHolder = new DataFragment.DataViewHolderType1(view);
        System.out.println("create view holder");
        return viewHolder;
    }

    public void bindViewHolderType1AndType2(InformationBean bean, int position,
                                            RecyclerView.ViewHolder holder, int type) {
        DataFragment.DataViewHolderType1 holderType1 = (DataFragment.DataViewHolderType1) holder;
        Glide.with(mContext)
                .load(MainConfig.MAIN_URL + bean.getPhoto_path())
                .error(R.mipmap.ic_launcher)
                .into(holderType1.photoVIew);
        String nick = bean.getNickname();
        if (nick.length() == 0) {
            holderType1.nicknameView.setText(mContext.getString(R.string.default_name));
        } else {
            holderType1.nicknameView.setText(bean.getNickname());
        }
        if (bean.getContent().length() == 0) {//没内容就隐藏
            holderType1.contentView.setVisibility(View.GONE);
        } else {
            holderType1.contentView.setText(bean.getContent());
        }

        holderType1.comment_countView.setText(bean.getComment_count() + "");
        holderType1.up_countView.setText(bean.getUp_count() + "");
//                holderType1.titleView.setText(bean.getNickname());

        if (bean.getIsUp() == 1) {
            holderType1.upView.setImageResource(R.drawable.ic_thumb_up_pink_24dp);
        } else if (bean.getIsUp() == 0) {
            holderType1.downView.setImageResource(R.drawable.ic_thumb_down_pink_24dp);
        } else {
            holderType1.downView.setImageResource(R.drawable.ic_thumb_down_black_24dp);
            holderType1.upView.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        }
        holderType1.upView.setTag(R.id.position, position);
        holderType1.upView.setTag(R.id.view_holder, holderType1);
        holderType1.upView.setTag(R.id.bean, bean);
        holderType1.upView.setOnClickListener(listener);

        holderType1.downView.setTag(R.id.position, position);
        holderType1.downView.setTag(R.id.view_holder, holderType1);
        holderType1.downView.setTag(R.id.bean, bean);
        holderType1.downView.setOnClickListener(listener);

        holderType1.commentView.setTag(R.id.position, position);
        holderType1.commentView.setTag(R.id.bean, bean);
        holderType1.commentView.setOnClickListener(listener);
        if (type == 2) {
            String[] files = bean.getImages().split("-");
            GridLayoutManager manager = null;

            manager = new GridLayoutManager(mContext, 3);
            holderType1.recyclerView.setLayoutManager(manager);
//                    holderType1.recyclerView.addItemDecoration(new RecyclerItemDecoration(10, files.length));
            holderType1.recyclerView.setAdapter(new DataFragment.Image9Adapter(mContext, Arrays.asList(files)));
        }
    }

    private boolean disableComment = false;

    /**
     * 禁止开启CommentActivity功能
     */
    public void disableStartComment() {
        disableComment = true;
    }

    public class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.up:
                    sendItemUp(1, v);
                    break;
                case R.id.down:
                    sendItemUp(0, v);
                    break;
                case R.id.comment:
                    startComment(v);
                    break;
            }
        }

        private void sendItemUp(final int up, final View v) {
            Map<String, String> map = new HashMap<>();
            final int position = (int) v.getTag(R.id.position);
            final InformationBean bean = (InformationBean) v.getTag(R.id.bean);
            map.put("iid", bean.getId() + "");
            map.put("uid", mContext.getSharedPreferences(MainConfig.MAIN_SP_FILE, Context.MODE_PRIVATE).getString("uid", ""));
            map.put("up", up + "");
            OkHttpUtils.basePostAsync("item_up", map, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final RemarkMessage msg = new Gson().fromJson(response.body().string(), RemarkMessage.class);
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (msg.getState() == 1) {
                                        if (up == 0) {

                                            bean.setUp_count(bean.getUp_count() - 1);
                                            bean.setIsUp(0);
                                            setUpCount(v, -1);
//                                                notifyItemChanged(position);
                                            ((ImageView) v).setImageResource(R.drawable.ic_thumb_down_pink_24dp);
                                        } else {
                                            bean.setUp_count(bean.getUp_count() + 1);
//                                                notifyItemChanged(position);
                                            setUpCount(v, 1);
                                            bean.setIsUp(1);
                                            ((ImageView) v).setImageResource(R.drawable.ic_thumb_up_pink_24dp);
                                        }
                                    } else {
                                        if (msg.getUp() == 0) {
                                            bean.setIsUp(0);
                                            Toast.makeText(mContext, "您已经踩过", Toast.LENGTH_SHORT).show();
                                            ((DataFragment.DataViewHolderType1) v.getTag(R.id.view_holder)).downView.setImageResource(R.drawable.ic_thumb_down_pink_24dp);
                                        } else {
                                            bean.setIsUp(1);
                                            Toast.makeText(mContext, "您已经赞过", Toast.LENGTH_SHORT).show();
                                            ((DataFragment.DataViewHolderType1) v.getTag(R.id.view_holder)).upView.setImageResource(R.drawable.ic_thumb_up_pink_24dp);
                                        }
                                    }
                                }
                            });
                        }
                    }
            );
        }

        private void setUpCount(View v, int up) {
            TextView tv = ((DataFragment.DataViewHolderType1) v.getTag(R.id.view_holder)).up_countView;
            int old = Integer.parseInt(tv.getText().toString());
            tv.setText((old + up) + "");
        }

        private void startComment(View v) {
            if (disableComment)
                return;
            Bundle bundle = new Bundle();
            bundle.putInt("item_id", ((InformationBean) v.getTag(R.id.bean)).getId());
            bundle.putSerializable("information_bean", ((InformationBean) v.getTag(R.id.bean)));
            Intent intent = new Intent(mContext, CommentActivity.class);
            intent.putExtras(bundle);
            ((Activity) mContext).startActivityForResult(intent, 1);
        }
    }
}