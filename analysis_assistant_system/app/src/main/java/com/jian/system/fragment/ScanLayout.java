
package com.jian.system.fragment;

import android.content.Context;
import android.view.LayoutInflater;

import com.jian.system.R;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanLayout extends QMUIWindowInsetLayout {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    private ViewPagerListener mListener;
    private Context context;

    public ScanLayout(Context context) {
        super(context);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.layout_scan, this);
        ButterKnife.bind(this);

        initTopBar();


    }


    private void initTopBar() {
        mTopBar.setTitle("扫一扫");
    }


    protected void startFragment(QMUIFragment fragment) {
        if (mListener != null) {
            mListener.startFragment(fragment);
        }
    }

    public void setViewPagerListener(ViewPagerListener listener) {
        mListener = listener;
    }



}
