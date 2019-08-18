
package com.jian.system;

import android.content.Context;

import com.jian.system.entity.System;
import com.jian.system.gesture.util.GestureUtils;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.squareup.leakcanary.LeakCanary;

public class Application extends android.app.Application {

    private static Context context;
    private static String tokenStr;

    public static final int Scan_Search_Request_Code = 100;
    public static final int Scan_Add_Request_Code = 200;

    public static Context getContext() {
        return context;
    }

    public static String getTokenStr() {
        return tokenStr;
    }

    public static void setTokenStr(String tokenStr) {
        Application.tokenStr = tokenStr;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        //QDUpgradeManager.getInstance(this).check();

        tokenStr = GestureUtils.get(this, GestureUtils.USER_TOEKN);
        tokenStr = tokenStr == null ? "" : tokenStr;

        QMUISwipeBackActivityManager.init(this);
    }
}
