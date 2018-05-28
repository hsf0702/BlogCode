package com.zsj.recyclerviewsimple;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author zsj
 */
public class GridViewLineActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_line);
        initData();
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        GridItemDecoration gridItemDecoration = new GridItemDecoration(this,R.drawable.dividline_shape);
        mRecyclerView.addItemDecoration(gridItemDecoration);
        mAdapter = new MyAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private ArrayList<String> mList;

        MyAdapter(ArrayList<String> list) {
            mList = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.mTvName.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTvName;

            MyViewHolder(View itemView) {
                super(itemView);

                mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            }
        }
    }

    private ArrayList<String> mList = new ArrayList<>();

    private void initData() {
        for (int i = 0; i < 100; i++) {
            mList.add("item =" + i);
        }
    }
}
