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
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private MultiTypeSupport<DATA> mMultiTypeSupport;

    public RecyclerViewComAdapter(Context context, ArrayList<DATA> data, int layoutId) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
        this.mLayoutId = layoutId;
    }

    public RecyclerViewComAdapter(Context context, ArrayList<DATA> data, MultiTypeSupport<DATA> multiTypeSupport) {
        this(context, data, -1);
        mMultiTypeSupport = multiTypeSupport;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mMultiTypeSupport != null) {
//            取出当当前的布局
            mLayoutId = viewType;
        }

        View view = mLayoutInflater.inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        if (mMultiTypeSupport != null) {
            //根据当前的条目返回布局
            return mMultiTypeSupport.getLayout(mData.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        onBind(holder, mData.get(position), position);

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onItemLongClick(position);
                }
            });
        }


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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }


}
