package com.zsj.recyclerviewsimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.zsj.recyclerviewsimple.comm.OnItemClickListener;
import com.zsj.recyclerviewsimple.comm.OnItemLongClickListener;
import com.zsj.recyclerviewsimple.comm.wrap.HeaderViewRecyclerView;

import java.util.ArrayList;

public class HeaderFooterActivity extends AppCompatActivity {

    private HeaderViewRecyclerView mRecyclerView;
    private ListAdapter mMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_footer);

        initData();
        initView();
    }


    private void initView() {
        mRecyclerView = (HeaderViewRecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
//        LinearItemDecoration linearItemDecoration = new LinearItemDecoration();
        LinearLayoutDecoration linearLayoutDecoration = new LinearLayoutDecoration(this, R.drawable.dividline_shape);
        mRecyclerView.addItemDecoration(linearLayoutDecoration);


        mMyAdapter = new ListAdapter(this, mList, R.layout.item_layout);
        mMyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getApplicationContext(), "删除" + mList.get(position), Toast.LENGTH_SHORT).show();
                mList.remove(position);
                mMyAdapter.notifyDataSetChanged();
            }
        });

        mMyAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {
                Toast.makeText(getApplicationContext(), "长按==" + mList.get(position), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mRecyclerView.setAdapter(mMyAdapter);
        final View headerView = LayoutInflater.from(this).inflate(R.layout.layout_header, mRecyclerView, false);
        mRecyclerView.addHeaderView(headerView);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.removeHeaderView(headerView);
            }
        });

    }


    private ArrayList<String> mList = new ArrayList<>();

    private void initData() {
        for (int i = 0; i < 100; i++) {
            mList.add("item =" + i);
        }
    }
}
