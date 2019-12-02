
package com.jian.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.User;
import com.jian.system.gesture.GesturePwdCheckActivity;
import com.jian.system.gesture.GesturePwdResetActivity;
import com.jian.system.gesture.GesturePwdSettingActivity;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.HashMap;
import java.util.Map;


public class ChangeActivity extends AppCompatActivity {

    private final static String TAG = ChangeActivity.class.getSimpleName();

    private User user;
    private String tokenStr;
    private QMUITipDialog tipDialog;
    private boolean isFirstLogin = true;
    private boolean isShowPwdLogin = false;
    public static boolean isShowGestureLogin = true;
    private final int MsgType_Login = 0;
    private final int MsgType_Logout = 1;
    private final int MsgType_IsLogin = 2;

    EditText editText;
    QMUITopBar mTopBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        QMUIStatusBarHelper.translucent(this);

        mTopBar = findViewById(R.id.topbar);
        mTopBar.setTitle("切换服务器地址");

        initData();

    }

    private void initData() {
        Context context = this;
        editText = findViewById(R.id.base_url);
        //读取缓存
        String baseUrl = GestureUtils.get(context, GestureUtils.TEST_BASE_URL);
        if(Utils.isNullOrEmpty(baseUrl)){
            editText.setText(baseUrl);
        }

        findViewById(R.id.button_base_url).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baseUrl = editText.getText().toString();
                if(baseUrl.endsWith("/")){
                    baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
                }
                UrlConfig.baseUrl = baseUrl;
                //缓存
                GestureUtils.set(context, GestureUtils.TEST_BASE_URL, baseUrl);
                //跳转登录
                goLoginActivity();
            }
        });
    }


    private void goLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
