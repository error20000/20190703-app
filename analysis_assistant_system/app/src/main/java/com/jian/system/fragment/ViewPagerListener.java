
package com.jian.system.fragment;


import android.content.Intent;

import com.qmuiteam.qmui.arch.QMUIFragment;

public interface ViewPagerListener {
    void startFragment(QMUIFragment fragment);
    void startFragmentForResult(QMUIFragment fragment, int requestCode);
    void startActivity(Intent intent);
}
