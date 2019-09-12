
package com.jian.system;

import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.entity.System;
import com.jian.system.entity.User;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.NetworkUtils;
import com.jian.system.utils.SyncUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.squareup.leakcanary.LeakCanary;

public class Application extends MultiDexApplication {

    private static Context context;
    private static String tokenStr;
    public static boolean hasNetwork = true;

    public static final int Scan_Search_Request_Code = 100;
    public static final int Scan_Add_Request_Code = 200;
    public static final int Nfc_Search_Request_Code = 300;
    public static final int Nfc_Add_Request_Code = 400;
    public static final int Nfc_Scan_Request_Code = 500;

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

        hasNetwork = NetworkUtils.isNetworkConnected(this);
        Log.d("dddddddddddddddddd", hasNetwork+"");
        if(hasNetwork){
            //SyncUtils.baseData();
            /*String userInfo = GestureUtils.get(context, GestureUtils.USER_INFO);
            if(!Utils.isNullOrEmpty(userInfo)){
                User user = JSONObject.parseObject(userInfo, User.class);
                SyncUtils.loginData(user.getsUser_ID());
            }*/
        }

        tokenStr = GestureUtils.get(this, GestureUtils.USER_TOEKN);
        tokenStr = tokenStr == null ? "" : tokenStr;

        QMUISwipeBackActivityManager.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
