
package com.jian.system;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.System;
import com.jian.system.entity.User;
import com.jian.system.gesture.GesturePwdCheckActivity;
import com.jian.system.gesture.GesturePwdResetActivity;
import com.jian.system.gesture.GesturePwdSettingActivity;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.DataCacheUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private final static String TAG = LoginActivity.class.getSimpleName();

    private User user;
    private String tokenStr;
    private QMUITipDialog tipDialog;
    private boolean isFirstLogin = true;
    private boolean isShowPwdLogin = false;
    private final int MsgType_Login = 0;
    private final int MsgType_Logout = 1;
    private final int MsgType_IsLogin = 2;

    EditText editText1;
    EditText editText2;
    QMUITopBar mTopBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        QMUIStatusBarHelper.translucent(this);

        mTopBar = findViewById(R.id.topbar);
        mTopBar.setTitle("用户登录");

        initLogin();

        //判断有无手势密码
        String restureStr = GestureUtils.get(this, GestureUtils.USER_GESTURE);
        if (!Utils.isNullOrEmpty(restureStr)) {
            isFirstLogin = false;
            autoLogin();
        }

    }

    private void initLogin() {
        editText1 = findViewById(R.id.login_username);
        editText2 = findViewById(R.id.login_password);

        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editText1.getText().toString();
                String password = editText2.getText().toString();
                //登录
                sendLogin(username, Utils.md5(password));
            }
        });
    }

    private void autoLogin() {
        //判断用户是否已登录
        sendIsLogin();
    }

    private void sendLogin(String username, String password) {

        tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("登录中")
                .create();
        tipDialog.show();

        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.userLoginUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Login);
                message.obj = res;
                mHandler.sendMessage(message);
            }
        });
    }

    private void sendLogout() {

        tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("退出中")
                .create();
        tipDialog.show();

        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.userLogoutUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Logout);
                message.obj = res;
                mHandler.sendMessage(message);
            }
        });
    }

    private void sendIsLogin() {

        tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在查询登录")
                .create();
        tipDialog.show();

        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.userIsLoginUrl, params);
                Message message = mHandler.obtainMessage(MsgType_IsLogin);
                message.obj = res;
                mHandler.sendMessage(message);
            }
        });
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //处理结果
            hideTips();
            String str = (String) msg.obj;
            if (Utils.isNullOrEmpty(str)) {
                showToast("网络异常，请检查网络。");
                return;
            }
            JSONObject resData = JSONObject.parseObject(str);
            //处理数据
            switch (msg.what) {
                case MsgType_Login:
                    loginMsg(resData);
                    break;
                case MsgType_Logout:
                    logoutMsg(resData);
                    break;
                case MsgType_IsLogin:
                    isLoginMsg(resData);
                    break;
                default:
                    break;
            }

        }
    };

    private void hideTips() {
        tipDialog.dismiss();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void loginMsg(JSONObject resObj) {
        if (resObj.getInteger("code") <= 0) {
            showErrorMsg(resObj.getString("msg"));
            if (isShowPwdLogin) {
                //清空本地密码
                GestureUtils.clear(this);
            }
            return;
        }
        JSONObject data = resObj.getJSONObject("data");
        tokenStr = data.getString("token");
        user = data.getObject("user", User.class);

        //保存用户信息
        Application.setTokenStr(tokenStr);
        GestureUtils.set(this, GestureUtils.USER_TOEKN, tokenStr);
        GestureUtils.set(this, GestureUtils.USER_INFO, data.getString("user"));
        GestureUtils.set(this, GestureUtils.USER_USERNAME, editText1.getText().toString());
        GestureUtils.set(this, GestureUtils.USER_PASSWORD, Utils.md5(editText2.getText().toString()));

        //跳转手势界面
        if (isFirstLogin) {
            gesturePwdSetting();
        } else {
            gesturePwdCheck();
        }
    }

    private void logoutMsg(JSONObject resObj) {
        if (resObj.getInteger("code") <= 0) {
            showErrorMsg(resObj.getString("msg"));
            return;
        }
        //清空本地密码
        GestureUtils.clear(this);
    }

    private void isLoginMsg(JSONObject resObj) {
        if (resObj.getInteger("code") > 0) {
            //跳转手势界面
            gesturePwdCheck();
        } else {
            //自动登录后，跳转手势界面
            String username = GestureUtils.get(this, GestureUtils.USER_USERNAME);
            String password = GestureUtils.get(this, GestureUtils.USER_PASSWORD);
            sendLogin(username, password);
            //自动登录错误，跳转登录界面
            isShowPwdLogin = true;
        }
    }

    private void showErrorMsg(String msg) {
        QMUITipDialog tipDialogE = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(msg)
                .create();
        tipDialogE.show();

        mTopBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialogE.dismiss();
            }
        }, 1500);

        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void gesturePwdSetting() {
        Intent intent = new Intent(this, GesturePwdSettingActivity.class);
        startActivity(intent);
        finish();
    }

    private void gesturePwdCheck() {
        Intent intent = new Intent(this, GesturePwdCheckActivity.class);
        startActivity(intent);
        finish();
    }

    private void gesturePwdReset() {
        Intent intent = new Intent(this, GesturePwdResetActivity.class);
        startActivity(intent);
        finish();
    }
}
