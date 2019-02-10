package com.shxy.datasharedplatform.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shxy.datasharedplatform.R;
import com.shxy.datasharedplatform.utils.MainConfig;

import java.util.List;

/**
 * Created by shxy on 2019/1/18.
 */

public class ImagesActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private int currentIndex = 0;
    private List<String> urls = null;
    private ImagesAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        resolveIntentData();
        mAdapter = new ImagesAdapter(this, urls);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(currentIndex);
    }

    private void resolveIntentData() {
        urls = (List<String>) getIntent().getSerializableExtra("images");
        currentIndex = getIntent().getIntExtra("currentIndex", 0);
    }


    private static class ImagesAdapter extends PagerAdapter {

        private Context mContext;
        private List<String> mList;

        public ImagesAdapter(Context mContext, List<String> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.image_layout, container, false);
            ImageView img = view.findViewById(R.id.img);
            Glide.with(mContext).load(MainConfig.MAIN_URL + mList.get(position)).into(img);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
