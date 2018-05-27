package com.zsj.recyclerviewsimple.comm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public abstract class RecyclerViewComAdapter<DATA> extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    /**
     * 参数通用只能用泛型
     */
    protected ArrayList<DATA> mData;

    /**
     * 条目id不一样只能通过参数传递
     */
    protected int mLayoutId;

    private final LayoutInflater mLayoutInflater;

    public RecyclerViewComAdapter(Context context, ArrayList<DATA> data, int layoutId) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
        this.mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onBind(holder, mData.get(position), position);
    }

    /**
     * 绑定
     *
     * @param holder
     * @param item
     * @param position
     */
    protected abstract void onBind(ViewHolder holder, DATA item, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
