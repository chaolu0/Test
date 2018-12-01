package com.shxy.datasharedplatform;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
        mTabLayout.addTab((mTabLayout.newTab().setText("首页")));
        mTabLayout.addTab((mTabLayout.newTab().setText("新建")));
        mTabLayout.addTab((mTabLayout.newTab().setText("个人")));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showFragmentByIndex(tab.getPosition());
                System.out.println(getSupportFragmentManager().getFragments());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
