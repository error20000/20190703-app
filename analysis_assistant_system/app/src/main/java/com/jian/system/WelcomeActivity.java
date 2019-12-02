
package com.jian.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.jian.system.config.UrlConfig;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class WelcomeActivity extends Activity {

    private final int MsgType_Login = 1;
    private final int MsgType_TestUrl = 2;
    private QMUITipDialog tipDialog;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        testNetwork();
    }

    private void testNetwork(){

        if(!UrlConfig.debug){
            handleLogin();
            return;
        }
        //测试服务器连接是否可用
        tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在测试服务器连接，请稍后。。。")
                .create();
        tipDialog.show();

        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(UrlConfig.userIsLoginUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false);
                    //超时时间为30秒
                    connection.setConnectTimeout(30 * 1000);
                    //获取返回码
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        mHandler.sendEmptyMessage(MsgType_Login);
                    }else {
                        mHandler.sendEmptyMessage(MsgType_TestUrl);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //网络连接错误
                    mHandler.sendEmptyMessage(MsgType_TestUrl);
                }
            }
        }.start();
    }

    private void handleTestUrl(){
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("")
                .setMessage("当前服务器连接失败，是否切换？")
                .addAction(0, "取消", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("重试", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        testNetwork();
                    }
                })
                .addAction("切换",  new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        gotoChangeUrl();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void gotoChangeUrl(){
        Intent intent = new Intent(this, ChangeActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            tipDialog.dismiss();

            //处理数据
            switch (msg.what) {
                case MsgType_Login:
                    handleLogin();
                    break;
                case MsgType_TestUrl:
                    handleTestUrl();
                    break;
                default:
                    break;
            }
        }
    };

}
