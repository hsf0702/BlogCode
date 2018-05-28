package com.zsj.recyclerviewsimple.comm;

/**
 * @author 朱胜军
 * @date 2018/5/27
 * 描述	      Item多条目的支持
 * 后台给的接口的数据.肯定有字段区分什么条目
 * 例如:聊天.chatContext,isMe(是不是自己的)
 * 多布局需要从对象下手.根据列表当前的条目确定布局
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public interface MultiTypeSupport<DATA> {
    /**
     * 根据当前的条目返回布局
     *
     * @param item
     * @return
     */
    int getLayout(DATA item);
}
