package com.zsj.taglayout;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author 朱胜军
 * @date 2018/7/2
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public abstract class BaseAdapter {
    /**
     * @return 条目数
     */
    public abstract int getCount();


    /**
     * @param position 坐标
     * @param parents  父类
     * @return 子View
     */
    public abstract View getView(int position, ViewGroup parents);

}
