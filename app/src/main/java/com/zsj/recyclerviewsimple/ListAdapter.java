package com.zsj.recyclerviewsimple;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author 朱胜军
 * @date 2018/5/26
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private OnClickListener mOnClickListener;
    private onLongClickListener mOnLongClickListener;

    interface OnClickListener {
        void onClick(int position);
    }

    interface onLongClickListener {
        void onLongClick(int position);
    }

    public void setonLongClickListener(onLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    private Context mContext;
    private ArrayList<String> mList;

    ListAdapter(Context context, ArrayList<String> list) {
        mContext = context;

        mList = list;
    }

    @NonNull
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MyViewHolder holder, final int position) {
        holder.mTvName.setText(mList.get(position));
        if (mOnClickListener != null) {
            holder.mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClick(position);
                }
            });
        }

        if (mOnLongClickListener != null) {
            holder.mRootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnLongClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvName;
        private final ConstraintLayout mRootView;

        MyViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView.findViewById(R.id.root_view);
            mTvName = itemView.findViewById(R.id.tv_name);
        }
    }
}