
package com.jian.system;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.entity.System;
import com.jian.system.entity.User;
import com.jian.system.gesture.GesturePwdCheckActivity;
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
    private int activityCount = 0;
    private boolean isBackground  = false;

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

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                //等于0时就是从后台或者打开app
                if(0 == activityCount && (activity instanceof MainActivity)){
                    //从后台就进入解锁页面
                    if(isBackground){
                        Intent intent = new Intent(getContext(), GesturePwdCheckActivity.class);
                        intent.putExtra(GestureUtils.Gesture_Model_Type, GestureUtils.Gesture_Model_Type_Lock_Screen);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                activityCount++;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                activityCount--;
                if(0 == activityCount){
                    isBackground  = true;
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
