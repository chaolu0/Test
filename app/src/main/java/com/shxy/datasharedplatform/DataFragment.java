package com.shxy.datasharedplatform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.shxy.datasharedplatform.controller.DataItemController;
import com.shxy.datasharedplatform.utils.MainConfig;
import com.shxy.datasharedplatform.utils.OkHttpUtils;
import com.shxy.datasharedplatform.widget.TopToast;

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
                mRefreshLayout.setRefreshing(true);
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
                if (newSize != 0) {
                    recv.getData().addAll(mData);//交换，让新数据显示在上面
                    mData = recv.getData();
                    mAdapter.setData(mData);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTip(newSize);
                        mRefreshLayout.setRefreshing(false);
                        if (newSize != 0) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                response.body().close();
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


    private void setTip(int n) {
        String text;
        if (n == 0) {
            text = "暂时没有新帖";
        } else {
            text = "获取" + n + "个新帖";
        }
        TopToast.setText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context mContext;
        private List<InformationBean> mData;
        private DataItemController controller;

        public DataAdapter(Context mContext, List<InformationBean> mData) {
            this.mContext = mContext;
            this.mData = mData;
            this.controller = new DataItemController(mContext);
        }

        public void setData(List<InformationBean> mData) {
            this.mData = mData;
        }

        @Override
        public int getItemViewType(int position) {
            return mData.get(position).getType();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return controller.createViewHolder(parent,viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            InformationBean bean = mData.get(position);
            controller.bindViewHolderType1AndType2(bean, position, holder, getItemViewType(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    public static class DataViewHolderType1 extends RecyclerView.ViewHolder {
        public ImageView photoVIew;
        public TextView nicknameView;
        //        TextView titleView;
        public TextView contentView;
        public TextView comment_countView;
        public TextView up_countView;
        public RecyclerView recyclerView;

        public ImageView upView;
        public ImageView downView;
        public ImageView commentView;

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
    public static class Image9Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
