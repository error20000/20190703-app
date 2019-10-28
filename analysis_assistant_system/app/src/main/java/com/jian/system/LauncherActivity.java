
package com.jian.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jian.system.service.UserService;
import com.jian.system.utils.NetworkUtils;
import com.jian.system.utils.SyncUtils;


public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        /*Application.hasNetwork = NetworkUtils.isNetworkConnected(this);
        Log.d("dddddddddddddddddd", Application.hasNetwork+"");
        if(Application.hasNetwork){
            SyncUtils.baseData();
            *//*String userInfo = GestureUtils.get(context, GestureUtils.USER_INFO);
            if(!Utils.isNullOrEmpty(userInfo)){
                User user = JSONObject.parseObject(userInfo, User.class);
                SyncUtils.loginData(user.getsUser_ID());
            }*//*
        }*/

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
