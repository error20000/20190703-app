package com.jian.system.gesture;


import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jian.system.LoginActivity;
import com.jian.system.MainActivity;
import com.jian.system.R;
import com.jian.system.gesture.base.GestureBaseActivity;
import com.jian.system.gesture.custom.EasyGestureLockLayout;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.Utils;

import java.util.List;


/**
 * 手势密码 登录校验界面
 */
public class GesturePwdCheckActivity extends GestureBaseActivity {

    TextView tv_go;
    EasyGestureLockLayout layout_parent;
    TextView go_login;

    String modelType = null;
    boolean canExit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gesture_pwd_check);
        initView();
        initLayoutView();

        Intent intent = getIntent();
        modelType = intent.getStringExtra(GestureUtils.Gesture_Model_Type);
    }

    @Override
    public void onBackPressed() {
        if(!Utils.isNullOrEmpty(modelType)){
            switch (modelType){
                case GestureUtils.Gesture_Model_Type_Lock_Screen:
                    exitApp();
                    break;
                case GestureUtils.Gesture_Model_Type_Change_Pwd:
                    break;
                case GestureUtils.Gesture_Model_Type_Reset_Pwd:
                    break;
            }
        }
        if(!canExit){
            super.onBackPressed();
        }
    }

    private void initView() {
        tv_go = findViewById(R.id.tv_go);
        layout_parent = findViewById(R.id.layout_parent);
        go_login =  findViewById(R.id.go_login);
        go_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清空登录信息
                //GestureUtils.clear(getApplication());
                //不显示手势登录
                LoginActivity.isShowGestureLogin = false;
                //跳转登录界面
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    protected void initLayoutView() {
        EasyGestureLockLayout.GestureEventCallbackAdapter adapter = new EasyGestureLockLayout.GestureEventCallbackAdapter() {
            @Override
            public void onCheckFinish(boolean succeedOrFailed) {
                String str = succeedOrFailed ? "解锁成功" : "解锁失败";
                Toast.makeText(GesturePwdCheckActivity.this, str, Toast.LENGTH_SHORT).show();
                //跳转主页面
                if(succeedOrFailed){
                    if(Utils.isNullOrEmpty(modelType)){
                        onCheckSuccess();
                    }else{
                        switch (modelType){
                            case GestureUtils.Gesture_Model_Type_Lock_Screen:
                                break;
                            case GestureUtils.Gesture_Model_Type_Change_Pwd:
                                break;
                            case GestureUtils.Gesture_Model_Type_Reset_Pwd:
                                break;
                        }
                        finish();
                    }

                }
            }

            @Override
            public void onSwipeMore() {
                //执行动画
                animate(tv_go);
            }

            @Override
            public void onToast(String s, int textColor) {
                tv_go.setText(s);
                if (textColor != 0)
                    tv_go.setTextColor(textColor);
                if (textColor == 0xffFF3232) {
                    animate(tv_go);
                }
            }
        };
        layout_parent.setGestureFinishedCallback(adapter);

        //使用check模式
        layout_parent.switchToCheckMode(parsePwdStr(getPwd()), 5);//校验密码
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void exitApp() {
        if (canExit) {
            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
            for (ActivityManager.AppTask appTask : appTaskList) {
                appTask.finishAndRemoveTask();
            }
        } else {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            canExit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    canExit = false;
                }
            }, 2000);
        }
    }
}
