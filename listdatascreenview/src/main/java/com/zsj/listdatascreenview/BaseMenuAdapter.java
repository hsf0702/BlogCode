package com.zsj.listdatascreenview;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author 朱胜军
 * @date 2018/7/15
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public abstract class BaseMenuAdapter {
    MenuObserver mMenuObserver;
    public void registerDataSetObserver(MenuObserver observer) {
        mMenuObserver = observer;
    }

    public void unregisterDataSetObserver(MenuObserver observer) {
        mMenuObserver = null;
    }


    public void closeMenu(){
        if (mMenuObserver != null){
            mMenuObserver.closeMenu();
        }
    }


    /**
     * 获取Tab的个数
     *
     * @return
     */
    public abstract int getCount();

    /**
     * 获取TabView
     *
     * @param position
     * @param parent
     * @return
     */
    public abstract View getTabView(int position, ViewGroup parent);


    /**
     * 获取菜单内容
     *
     * @param position
     * @param parent
     * @return
     */
    public abstract View getMenuContentView(int position, ViewGroup parent);

    public abstract void openMenu(View tabView) ;

    public abstract void closeMenu(View tabView);
}
