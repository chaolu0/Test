package com.shxy.datasharedplatform;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shxy.datasharedplatform.utils.MainConfig;

/**
 * Created by shxy on 2018/12/1.
 */

public class PersonalFragment extends Fragment implements View.OnClickListener {
    private ImageView img;
    private TextView nickname;
    private TextView info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img = view.findViewById(R.id.img);
        nickname = view.findViewById(R.id.nickname);
        info = view.findViewById(R.id.personal_sign);
        img.setOnClickListener(this);
        nickname.setOnClickListener(this);
        info.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getProfile();
    }

    private void getProfile() {
        SharedPreferences sp = getActivity().getSharedPreferences(MainConfig.MAIN_SP_FILE, Context.MODE_PRIVATE);
        Glide.with(this)
                .load(MainConfig.MAIN_URL + sp.getString(MainConfig.IMG_KEY, ""))
                .error(R.mipmap.ic_launcher)
                .into(img);
        nickname.setText(sp.getString(MainConfig.NICK_NAME_KEY, getString(R.string.default_name)));
        info.setText(sp.getString(MainConfig.INFO_KEY, getString(R.string.default_info)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img:
                break;
            case R.id.nickname:
                Intent intent = ModifyProfile.genarateIntent(getActivity(), "", "", "");
                startActivity(intent);
                break;
            case R.id.info:
                break;
        }
    }
}
