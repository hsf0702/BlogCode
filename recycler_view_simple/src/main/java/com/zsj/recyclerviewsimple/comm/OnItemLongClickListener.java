package com.zsj.recyclerviewsimple.comm;

/**
 * @author 朱胜军
 * @date 2018/5/27
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public interface OnItemLongClickListener {
    /**
     * 条目长按事件
     *
     * @param position
     * @return
     */
    public boolean onItemLongClick(int position);
}
