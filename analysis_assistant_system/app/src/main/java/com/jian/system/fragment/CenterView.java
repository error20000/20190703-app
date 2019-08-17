
package com.jian.system.fragment;

import android.content.Context;
import android.view.LayoutInflater;

import com.jian.system.R;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CenterView extends QMUIWindowInsetLayout {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    private ViewPagerListener mListener;

    public CenterView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_map, this);
        ButterKnife.bind(this);
        initTopBar();
    }

    protected void startFragment(QMUIFragment fragment) {
        if (mListener != null) {
            mListener.startFragment(fragment);
        }
    }

    public void setMainListener(ViewPagerListener listener) {
        mListener = listener;
    }


    private void initTopBar() {
        mTopBar.setTitle("地图");
    }

}
