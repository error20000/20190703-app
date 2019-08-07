
package com.jian.system.fragment;

import android.content.Context;
import android.view.LayoutInflater;

import com.jian.system.R;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeView extends QMUIWindowInsetLayout {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    private Listener mListener;

    public HomeView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.fragment_home, this);
        ButterKnife.bind(this);
        initTopBar();
    }

    public interface Listener {
        void startFragment(QMUIFragment fragment);
    }

    protected void startFragment(QMUIFragment fragment) {
        if (mListener != null) {
            mListener.startFragment(fragment);
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }


    private void initTopBar() {
        mTopBar.setTitle("首页");
    }

}
