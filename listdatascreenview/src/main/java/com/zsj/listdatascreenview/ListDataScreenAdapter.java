package com.zsj.listdatascreenview;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author 朱胜军
 * @date 2018/7/15
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */

public class ListDataScreenAdapter extends BaseMenuAdapter {

    private String[] mItems = new String[]{"品牌", "价格", "保障", "更多"};

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public View getTabView(int position, ViewGroup parent) {
        TextView tabView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_data_screen_tab, parent, false);
        tabView.setText(mItems[position]);
        tabView.setTextColor(Color.BLACK);
        return tabView;
    }

    @Override
    public View getMenuContentView(int position, ViewGroup parent) {
        TextView menuContent = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_data_screen_menu_content, parent, false);
        menuContent.setText(mItems[position]);
        menuContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        return menuContent;
    }

    @Override
    public void openMenu(View tabView) {
        TextView tv = (TextView) tabView;
        tv.setTextColor(Color.RED);
    }

    @Override
    public void closeMenu(View tabView) {
        TextView textView = (TextView) tabView;
        textView.setTextColor(Color.BLACK);
    }
}
