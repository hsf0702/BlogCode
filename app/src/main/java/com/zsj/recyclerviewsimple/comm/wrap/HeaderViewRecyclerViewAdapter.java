package com.zsj.recyclerviewsimple.comm.wrap;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 朱胜军
 * @date 2018/5/27
 * 描述	      可以添加头布局的底布局的Adapter
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class HeaderViewRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 为了能区分头部,底部和数据列表的Adapter.用Map,当key可以为int,value 为 object 可以用SparseArray 更省内存
     */
    private SparseArray<View> mHeaderViews, mFooterViews;

    /**
     * 数据类别的Adapter,不包含头部底部的
     */
    private RecyclerView.Adapter mAdapter;


    /**
     * 头部开始的key
     */
    private static int BASE_HEADER_KEY = 100000;
    /**
     * 底部开始的key
     */
    private static int BASE_FOOTER_KEY = 200000;

    public HeaderViewRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //是头部
        if (mHeaderViews.indexOfKey(viewType) >= 0) {
            return createHeaderFooterViewHolder(mHeaderViews.get(viewType));
        }
        //是底布局
        if (mFooterViews.indexOfKey(viewType) >= 0) {
            return createHeaderFooterViewHolder(mFooterViews.get(viewType));
        }

        //数据列表的Adapter
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    /**
     * 创建头布局和底部的ViewHolder
     *
     * @param view
     * @return
     */
    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {
        };
    }


    @Override
    public int getItemViewType(int position) {
        //头部
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            //返回头部的Key
            return mHeaderViews.keyAt(position);
        }
        //数据列表Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        //底部
        return mFooterViews.keyAt(adjPosition - adapterCount);
    }

    /**
     * 头部的数量
     *
     * @return
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //头部
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            //头布局,不做任何处理.也绑定数据
            return;
        }
        //数据列表Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, adjPosition);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + mHeaderViews.size() + mFooterViews.size();
    }

//    添加或者移除头部底部的方法,

    /**
     * 添加头部
     *
     * @param view
     */
    public void addHeaderView(View view) {
        //如果不包含
        if (mHeaderViews.indexOfValue(view) == -1) {
            mHeaderViews.put(BASE_HEADER_KEY++, view);
            //刷新列表
            notifyDataSetChanged();
        }
    }

    /**
     * 添加底部
     *
     * @param view
     */
    public void addFooterView(View view) {
        //如果不包含
        if (mFooterViews.indexOfValue(view) == -1) {
            mHeaderViews.put(BASE_FOOTER_KEY++, view);
            //刷新列表
            notifyDataSetChanged();
        }
    }

    /**
     * 移除头部
     *
     * @param view
     */
    public void removeHeaderView(View view) {
        //如果包含
        if (mHeaderViews.indexOfValue(view) >= 0) {
            mHeaderViews.removeAt(mHeaderViews.indexOfValue(view));
            //刷新列表
            notifyDataSetChanged();
        }
    }

    /**
     * 移除底部
     *
     * @param view
     */
    public void removeFooterView(View view) {
        //如果包含
        if (mFooterViews.indexOfValue(view) >= 0) {
            mFooterViews.removeAt(mFooterViews.indexOfValue(view));
            //刷新列表
            notifyDataSetChanged();
        }
    }
}
