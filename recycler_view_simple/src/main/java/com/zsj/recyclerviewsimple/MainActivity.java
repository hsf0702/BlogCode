package com.zsj.recyclerviewsimple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 分割线
     *
     * @param view
     */
    public void listViewLine(View view) {
        startActivity(new Intent(this, ListViewLineActivity.class));
    }

    public void gridViewLine(View view) {
        startActivity(new Intent(this, GridViewLineActivity.class));
    }

    /**
     * 多布局
     *
     * @param view
     */
    public void mulitType(View view) {
        startActivity(new Intent(this, MulitTypeActivity.class));
    }

    /**
     * 头部底部
     *
     * @param view
     */
    public void headerFooter(View view) {
        startActivity(new Intent(this, HeaderFooterActivity.class));
    }
}
