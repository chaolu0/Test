package com.shxy.datasharedplatform;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.shxy.datasharedplatform.bean.LoginMessage;
import com.shxy.datasharedplatform.bean.UploadPhotoMessage;
import com.shxy.datasharedplatform.utils.FileUtils;
import com.shxy.datasharedplatform.utils.MainConfig;
import com.shxy.datasharedplatform.utils.OkHttpUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by shxy on 2018/12/1.
 */

public class PersonalFragment extends Fragment implements View.OnClickListener {
    private ImageView img;
    private TextView nickname;
    private TextView info;
    private Button logoutButton;

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

        logoutButton = view.findViewById(R.id.logout);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
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
        SharedPreferences sp = getActivity().getSharedPreferences(MainConfig.MAIN_SP_FILE, Context.MODE_PRIVATE);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.img:
                selectImages();
                break;
            case R.id.nickname:
                intent = ModifyProfile.genarateIntent(getActivity(), "昵称",
                        sp.getString(MainConfig.NICK_NAME_KEY, getString(R.string.default_name)), "一个好的名字，能够让大家记住你", ModifyProfile.NICKNAME_TYPE);
                startActivity(intent);
                break;
            case R.id.personal_sign:
                intent = ModifyProfile.genarateIntent(getActivity(), "个人签名",
                        sp.getString(MainConfig.INFO_KEY, getString(R.string.default_info)), "最长可以输入24个字的个人签名~", ModifyProfile.INFO_TYPE);
                startActivity(intent);
                break;
            case R.id.logout:
                ((BaseActivity) getActivity()).removeAllActivitys();
                clearLoginKey();
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void clearLoginKey() {
        SharedPreferences sp = getActivity().getSharedPreferences(MainConfig.MAIN_SP_FILE, Context.MODE_PRIVATE);
        sp.edit().remove(MainConfig.LOGIN_KEY).putBoolean(MainConfig.LOGIN_KEY, false).apply();
    }

    private final int REQUEST_CODE_CHOOSE = 2;

    private void selectImages() {
        Matisse.from(this)
                .choose(MimeType.allOf()) // 选择 mime 的类型
                .countable(true)
                .maxSelectable(1) // 图片选择的最多数量
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == getActivity().RESULT_OK) {
            final List<Uri> mSelected = Matisse.obtainResult(data);
            if (mSelected.size() == 1) {
                HashMap<String, String> params = new HashMap<>();
                SharedPreferences sp = getActivity().getSharedPreferences(MainConfig.MAIN_SP_FILE, Context.MODE_PRIVATE);
                params.put("uid", sp.getString(MainConfig.UID_KEY, ""));
                params.put("SK", sp.getString(MainConfig.SK_KEY, ""));
                HashMap<String, File> files = new HashMap<>();
                files.put("photo", FileUtils.getFileByUri(mSelected.get(0), getContext()));
                OkHttpUtils.filePostAsync("upload_photo", params, files, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String string = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UploadPhotoMessage msg = new Gson().fromJson(string, UploadPhotoMessage.class);
                                if (msg.getState() == 1) {
                                    SharedPreferences sp = getActivity().getSharedPreferences(MainConfig.MAIN_SP_FILE, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sp.edit();
                                    edit.putString(MainConfig.IMG_KEY, msg.getPhoto_path());
                                    edit.apply();
                                    Glide.with(PersonalFragment.this)
                                            .load(mSelected.get(0))
                                            .into(img);
                                    Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), msg.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        }
    }
}
