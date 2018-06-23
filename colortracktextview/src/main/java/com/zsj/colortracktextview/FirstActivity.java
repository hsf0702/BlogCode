package com.zsj.colortracktextview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist);
    }

    public void demo(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onViewPager(View view) {
        startActivity(new Intent(this, ViewPagerActivity.class));
    }
}
