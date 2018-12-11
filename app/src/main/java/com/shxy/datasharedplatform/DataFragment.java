package com.shxy.datasharedplatform;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.shxy.datasharedplatform.utils.MainConfig;
import com.shxy.datasharedplatform.utils.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
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
            View view = inflater.inflate(R.layout.data_item_type1, parent, false);
            DataViewHolderType1 viewHolder = new DataViewHolderType1(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            InformationBean bean = mData.get(position);
            if (getItemViewType(position) == 1) {
                DataViewHolderType1 holderType1 = (DataViewHolderType1) holder;
                Glide.with(mContext)
                        .load(MainConfig.MAIN_URL + bean.getPhoto_path())
                        .into(holderType1.photoVIew);
                holderType1.nicknameView.setText(bean.getNickname());
                holderType1.contentView.setText(bean.getContent());
                holderType1.comment_countView.setText(bean.getComment_count() + "");
                holderType1.up_countView.setText(bean.getUp_count() + "");
//                holderType1.titleView.setText(bean.getNickname());

            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    public static class DataViewHolderType1 extends RecyclerView.ViewHolder {
        ImageView photoVIew;
        TextView nicknameView;
        //        TextView titleView;
        TextView contentView;
        TextView comment_countView;
        TextView up_countView;

        public DataViewHolderType1(View itemView) {
            super(itemView);
            photoVIew = itemView.findViewById(R.id.img);
            nicknameView = itemView.findViewById(R.id.name);
//            titleView = itemView.findViewById(R.id.title);
            contentView = itemView.findViewById(R.id.content);
            comment_countView = itemView.findViewById(R.id.comment_count);
            up_countView = itemView.findViewById(R.id.up_count);
        }
    }

}
