package com.zsj.lib.androidlib.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shengjun.zhu1 on 2017/9/25.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        initVariables();
        initViews();
        loadData();
    }

    protected abstract int setLayout();

    public abstract void initVariables();

    protected abstract void initViews();

    protected abstract void loadData();
}
