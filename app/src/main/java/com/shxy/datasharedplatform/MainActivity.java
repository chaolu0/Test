package com.shxy.datasharedplatform;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final int PAGE_COUNT = 3;
    private static final int DATA_FRAGMENT_INDEX = 0;
    private static final int CREATE_DATA_FRAGMENT_INDEX = 1;
    private static final int PERSONAL_FRAGMENT_INDEX = 2;
    private Fragment[] mFragments;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragments();
    }

    private void initFragments() {
        mFragments = new Fragment[PAGE_COUNT];
        mFragments[DATA_FRAGMENT_INDEX] = new DataFragment();
        mFragments[CREATE_DATA_FRAGMENT_INDEX] = new CreateDataFragment();
        mFragments[PERSONAL_FRAGMENT_INDEX] = new PersonalFragment();
        getSupportFragmentManager().beginTransaction()
                .hide(mFragments[CREATE_DATA_FRAGMENT_INDEX])
                .hide(mFragments[PERSONAL_FRAGMENT_INDEX])
                .show(mFragments[DATA_FRAGMENT_INDEX])
                .commit();
    }
}
