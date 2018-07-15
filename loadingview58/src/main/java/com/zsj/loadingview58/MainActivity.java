package com.zsj.loadingview58;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingView = (LoadingView) findViewById(R.id.loading_view);
    }

    public void click(View view) {
        mLoadingView.setVisibility(View.GONE);
    }
}
