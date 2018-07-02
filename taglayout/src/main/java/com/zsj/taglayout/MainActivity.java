package com.zsj.taglayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TagLayout mTagLayout;

    private List<String> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTagLayout = (TagLayout) findViewById(R.id.tagLayout);

        //假设从网络获取的数据
        mItems.add("1111");
        mItems.add("222222");
        mItems.add("33333333");
        mItems.add("444");
        mItems.add("5555555555");
        mItems.add("6666");
        mItems.add("77777");


        mTagLayout.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public View getView(int position, ViewGroup parents) {
                TextView tvTag = (TextView) LayoutInflater.from(MainActivity.this).inflate(R.layout.tag_layout, parents, false);
                tvTag.setText(mItems.get(position));
                return tvTag;
            }
        });
    }
}
