package com.shxy.datasharedplatform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.shxy.datasharedplatform.bean.InformationBean;
import com.shxy.datasharedplatform.bean.InformationRecv;
import com.shxy.datasharedplatform.bean.RemarkMessage;
import com.shxy.datasharedplatform.utils.MainConfig;
import com.shxy.datasharedplatform.utils.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by shxy on 2018/12/1.
 */

public class DataFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mRefreshButton;
    private DataAdapter mAdapter;
    private List<InformationBean> mData = new ArrayList<>();
    private SwipeRefreshLayout mRefreshLayout;
    private int page = 0;
    private TextView tip;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("onCreateView");
        return inflater.inflate(R.layout.fragment_data, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRefreshButton = view.findViewById(R.id.floating_action_button);
        mRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        tip = view.findViewById(R.id.tip);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new DataAdapter(getContext(), mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchItems();
            }
        });
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.scrollToPosition(0);
                fetchItems();
            }
        });
        fetchItems();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void fetchItems() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        page++;
        OkHttpUtils.baseGetAsync("fetch_items", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                page--;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                InformationRecv recv = new Gson().fromJson(s, InformationRecv.class);
                final int newSize = recv.getData().size();//新获取到的数据，用于显示tip
                recv.getData().addAll(mData);//交换，让新数据显示在上面
                mData = recv.getData();
                mAdapter.setmData(mData);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTip(newSize);
                        mRefreshLayout.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop");
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("onDetach");
    }

    private static final int MSG_TIP_HIDE = 0x01;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_TIP_HIDE)
                tip.setVisibility(View.GONE);
        }
    };

    private void setTip(int n) {
        tip.setVisibility(View.VISIBLE);
        if (n == 0) {
            tip.setText("暂时没有新帖");
        } else {
            tip.setText("获取" + n + "个新帖");
        }
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TIP_HIDE), 2000);
    }

    public static class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context mContext;
        private List<InformationBean> mData;

        public DataAdapter(Context mContext, List<InformationBean> mData) {
            this.mContext = mContext;
            this.mData = mData;
        }

        public void setmData(List<InformationBean> mData) {
            this.mData = mData;
        }

        @Override
        public int getItemViewType(int position) {
            return mData.get(position).getType();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_data_type2, parent, false);
            DataViewHolderType1 viewHolder = new DataViewHolderType1(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            InformationBean bean = mData.get(position);
            if (getItemViewType(position) == 1 || getItemViewType(position) == 2) {
                DataViewHolderType1 holderType1 = (DataViewHolderType1) holder;
                Glide.with(mContext)
                        .load(MainConfig.MAIN_URL + bean.getPhoto_path())
                        .into(holderType1.photoVIew);
                holderType1.nicknameView.setText(bean.getNickname());
                if (bean.getContent().length() == 0) {//没内容就隐藏
                    holderType1.contentView.setVisibility(View.GONE);
                } else {
                    holderType1.contentView.setText(bean.getContent());
                }

                holderType1.comment_countView.setText(bean.getComment_count() + "");
                holderType1.up_countView.setText(bean.getUp_count() + "");
//                holderType1.titleView.setText(bean.getNickname());
                if (getItemViewType(position) == 2) {
                    String[] files = bean.getImages().split("-");
                    GridLayoutManager manager = null;

                    manager = new GridLayoutManager(mContext, 3);
                    holderType1.upView.setTag(R.id.postion, position);
                    holderType1.upView.setTag(R.id.view_holder, holderType1);
                    holderType1.upView.setOnClickListener(listener);

                    holderType1.downView.setTag(R.id.postion, position);
                    holderType1.downView.setTag(R.id.view_holder, holderType1);
                    holderType1.downView.setOnClickListener(listener);

                    holderType1.commentView.setTag(R.id.postion, position);
                    holderType1.commentView.setOnClickListener(listener);

                    holderType1.recyclerView.setLayoutManager(manager);
//                    holderType1.recyclerView.addItemDecoration(new RecyclerItemDecoration(10, files.length));
                    holderType1.recyclerView.setAdapter(new Image9Adapter(mContext, Arrays.asList(files)));
                }
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        private ClickListener listener = new ClickListener();

        private class ClickListener implements View.OnClickListener {

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
                map.put("iid", mData.get((Integer) v.getTag(R.id.postion)).getId() + "");
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
                                                setUpCount(v, -1);
                                                ((ImageView) v).setImageResource(R.drawable.ic_thumb_down_pink_24dp);
                                            } else {
                                                setUpCount(v, 1);
                                                ((ImageView) v).setImageResource(R.drawable.ic_thumb_up_pink_24dp);
                                            }
                                        } else {
                                            if (msg.getUp() == 0) {
                                                Toast.makeText(mContext, "您已经踩过", Toast.LENGTH_SHORT).show();
                                                ((DataViewHolderType1) v.getTag(R.id.view_holder)).downView.setImageResource(R.drawable.ic_thumb_down_pink_24dp);
                                            } else {
                                                Toast.makeText(mContext, "您已经赞过", Toast.LENGTH_SHORT).show();
                                                ((DataViewHolderType1) v.getTag(R.id.view_holder)).upView.setImageResource(R.drawable.ic_thumb_up_pink_24dp);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                );
            }

            private void setUpCount(View v, int up) {
                TextView tv = ((DataViewHolderType1) v.getTag(R.id.view_holder)).up_countView;
                int old = Integer.parseInt(tv.getText().toString());
                tv.setText((old + up) + "");
            }

            private void startComment(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("item_id", (Integer) v.getTag(R.id.postion));
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtras(bundle);
                ((Activity) mContext).startActivityForResult(intent, 1);
            }
        }


    }

    public static class DataViewHolderType1 extends RecyclerView.ViewHolder {
        ImageView photoVIew;
        TextView nicknameView;
        //        TextView titleView;
        TextView contentView;
        TextView comment_countView;
        TextView up_countView;
        RecyclerView recyclerView;

        ImageView upView;
        ImageView downView;
        ImageView commentView;

        public DataViewHolderType1(View itemView) {
            super(itemView);
            photoVIew = itemView.findViewById(R.id.img);
            nicknameView = itemView.findViewById(R.id.name);
//            titleView = itemView.findViewById(R.id.title);
            contentView = itemView.findViewById(R.id.content);
            comment_countView = itemView.findViewById(R.id.comment_count);
            up_countView = itemView.findViewById(R.id.up_count);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            upView = itemView.findViewById(R.id.up);
            downView = itemView.findViewById(R.id.down);
            commentView = itemView.findViewById(R.id.comment);
        }
    }


    //九宫格图片适配器
    private static class Image9Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<String> mList;
        private Context mContext;

        public Image9Adapter(Context mContext, List<String> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        public void setmList(List<String> mList) {
            this.mList = mList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.grid_img_item, parent, false);
            return new Image9ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Glide.with(mContext)
                    .load(MainConfig.MAIN_URL + mList.get(position))
                    .into(((Image9ViewHolder) holder).img);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    //九宫格图片viewHolder
    private static class Image9ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public Image9ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }


}
