package com.shxy.datasharedplatform;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int PAGE_COUNT = 3;
    private static final int DATA_FRAGMENT_INDEX = 0;
    private static final int CREATE_DATA_FRAGMENT_INDEX = 1;
    private static final int PERSONAL_FRAGMENT_INDEX = 2;
    private Fragment[] mFragments;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {//不保存Fragment
            savedInstanceState.putParcelable("android:support:fragments", null);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActivityFlags();
        initFragments();
        initTabLayout();
    }

    private void setActivityFlags() {
        int options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(options);
        getSupportActionBar().hide();
    }

    private void initTabLayout() {
        //need add -> 图片，点击
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createViewByPositionAndSelected(0, true)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createViewByPositionAndSelected(1, false)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createViewByPositionAndSelected(2, false)));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showFragmentByIndex(tab.getPosition());
                setViewByPositionAndSelected(tab.getPosition(), true, tab.getCustomView());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setViewByPositionAndSelected(tab.getPosition(), false, tab.getCustomView());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private static final int[] TAB_IMG_NORMAL = {R.drawable.ic_home_black_24dp
            , R.drawable.ic_add_box_black_24dp, R.drawable.ic_person_black_24dp};
    private static final int[] TAB_IMG_SELECTED = {R.drawable.ic_home_black_selected_24dp
            , R.drawable.ic_add_box_black_selected_24dp, R.drawable.ic_person_black_selected_24dp};
    private static final String[] TAB_TEXT = {"首页", "", "个人"};
    private static final int[] TAB_TEXT_COLOR = {Color.parseColor("#383838"), Color.parseColor("#003cff")};

    private View createViewByPositionAndSelected(int position, boolean selected) {

        View v = LayoutInflater.from(this).inflate(R.layout.tab_view, null, false);
        setViewByPositionAndSelected(position, selected, v);
        return v;
    }

    private void setViewByPositionAndSelected(int position, boolean selected, View v) {
        ImageView img = v.findViewById(R.id.img);
        TextView tv = v.findViewById(R.id.text);
        img.setImageResource(selected ? TAB_IMG_SELECTED[position] : TAB_IMG_NORMAL[position]);
        tv.setText(TAB_TEXT[position]);
        tv.setTextColor(selected ? TAB_TEXT_COLOR[1] : TAB_TEXT_COLOR[0]);
    }

    private void initFragments() {
        mFragments = new Fragment[PAGE_COUNT];
        mFragments[DATA_FRAGMENT_INDEX] = new DataFragment();
        mFragments[CREATE_DATA_FRAGMENT_INDEX] = new CreateDataFragment();
        mFragments[PERSONAL_FRAGMENT_INDEX] = new PersonalFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, mFragments[DATA_FRAGMENT_INDEX])
                .add(R.id.frame_layout, mFragments[CREATE_DATA_FRAGMENT_INDEX])
                .add(R.id.frame_layout, mFragments[PERSONAL_FRAGMENT_INDEX])
                .commit();
        showFragmentByIndex(DATA_FRAGMENT_INDEX);
    }

    private void showFragmentByIndex(int index) {
        getSupportFragmentManager().beginTransaction()
                .hide(mFragments[DATA_FRAGMENT_INDEX])
                .hide(mFragments[CREATE_DATA_FRAGMENT_INDEX])
                .hide(mFragments[PERSONAL_FRAGMENT_INDEX])
                .show(mFragments[index])
                .commit();
    }
}
