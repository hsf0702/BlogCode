package com.zsj.recyclerviewsimple.comm.wrap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author 朱胜军
 * @date 2018/5/27
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class HeaderViewRecyclerView extends RecyclerView {
    private HeaderViewRecyclerViewAdapter mAdapter;
    private AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            mAdapter.notifyItemRangeChanged(positionStart,itemCount,payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeInserted(positionStart,itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeRemoved(positionStart,itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mAdapter.notifyItemRangeRemoved(fromPosition,itemCount);
        }
    };

    public HeaderViewRecyclerView(Context context) {
        this(context, null);
    }

    public HeaderViewRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderViewRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof HeaderViewRecyclerViewAdapter) {
            mAdapter = (HeaderViewRecyclerViewAdapter) adapter;
        } else {
            mAdapter = new HeaderViewRecyclerViewAdapter(adapter);
            //删除的bug是因为列表的Adapter改变,但是HeadViewRecyclerViewAdapter没有改变.需要关联,观察者模式
            adapter.registerAdapterDataObserver(mAdapterDataObserver);
        }
        super.setAdapter(mAdapter);
    }


    /**
     * 添加头部
     *
     * @param view
     */
    public void addHeaderView(View view) {
        if (mAdapter != null) {
            mAdapter.addHeaderView(view);
        }
    }

    /**
     * 添加底部
     *
     * @param view
     */
    public void addFooterView(View view) {
        if (mAdapter != null) {
            mAdapter.addFooterView(view);
        }
    }

    /**
     * 移除头部
     *
     * @param view
     */
    public void removeHeaderView(View view) {
        if (mAdapter != null) {
            mAdapter.removeHeaderView(view);
        }
    }

    /**
     * 移除底部
     *
     * @param view
     */
    public void removeFooterView(View view) {
        if (mAdapter != null) {
            mAdapter.removeFooterView(view);
        }
    }
}
