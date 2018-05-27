package com.zsj.recyclerviewsimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zsj.recyclerviewsimple.comm.OnItemClickListener;
import com.zsj.recyclerviewsimple.comm.OnItemLongClickListener;

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


        mMyAdapter = new ListAdapter(this, mList, R.layout.item_layout);

        mMyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getApplicationContext(), "点击" + mList.get(position), Toast.LENGTH_SHORT).show();
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
    }


    private ArrayList<String> mList = new ArrayList<>();

    private void initData() {
        for (int i = 0; i < 100; i++) {
            mList.add("item =" + i);
        }
    }
}
