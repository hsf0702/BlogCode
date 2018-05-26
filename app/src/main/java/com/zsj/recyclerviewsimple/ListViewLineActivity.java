package com.zsj.recyclerviewsimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author zsj
 */
public class ListViewLineActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ListAdapter mMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);


        initData();
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
//        LinearItemDecoration linearItemDecoration = new LinearItemDecoration();
        LinearLayoutDecoration linearLayoutDecoration = new LinearLayoutDecoration(this, R.drawable.dividline_shape);
        mRecyclerView.addItemDecoration(linearLayoutDecoration);


        mMyAdapter = new ListAdapter(this, mList);
        mMyAdapter.setOnClickListener(new ListAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(ListViewLineActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });

        mMyAdapter.setonLongClickListener(new ListAdapter.onLongClickListener() {
            @Override
            public void onLongClick(int position) {
                Toast.makeText(ListViewLineActivity.this, "长按" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mMyAdapter);
    }


    private ArrayList<String> mList = new ArrayList<>();

    private void initData() {
        for (int i = 0; i < 100; i++) {
            mList.add("item =" + i);
        }
    }
}
