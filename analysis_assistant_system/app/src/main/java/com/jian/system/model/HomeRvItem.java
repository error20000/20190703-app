
package com.jian.system.model;


import com.qmuiteam.qmui.arch.QMUIFragment;

public class HomeRvItem {

    private Class<? extends QMUIFragment> fragment;
    private String name;
    private int iconRes;

    public HomeRvItem(Class<? extends QMUIFragment> fragment, String name, int iconRes){
        this.fragment = fragment;
        this.name = name;
        this.iconRes = iconRes;
    }


    public Class<? extends QMUIFragment> getFragment() {
        return fragment;
    }

    public String getName() {
        return name;
    }

    public int getIconRes() {
        return iconRes;
    }


}
