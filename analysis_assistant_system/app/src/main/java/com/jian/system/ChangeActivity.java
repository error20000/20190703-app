
package com.jian.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jian.system.config.UrlConfig;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class ChangeActivity extends AppCompatActivity {

    private final static String TAG = ChangeActivity.class.getSimpleName();
    private final int MsgType_Login = 1;
    private final int MsgType_ChangeUrl = 2;
    private QMUITipDialog tipDialog;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    public static String isFromSet = "isFromSet"; //传参名称
    private boolean fromSet = false; //标记是否来自设置
    private boolean reset = false; //恢复默认

    EditText editText;
    TextView textView;
    QMUITopBar mTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        QMUIStatusBarHelper.translucent(this);

        mTopBar = findViewById(R.id.topbar);
        mTopBar.setTitle("切换服务器地址");
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fromSet = this.getIntent().getBooleanExtra(isFromSet, false);

        initData();

    }

    private void initData() {
        Context context = this;
        editText = findViewById(R.id.base_url);
        textView = findViewById(R.id.base_url_label);
        textView.setText("当前服务器：" + UrlConfig.baseUrl);
        textView.setVisibility(fromSet ? View.VISIBLE : View.GONE);
        //读取缓存
        String baseUrl = GestureUtils.get(context, GestureUtils.TEST_BASE_URL);
        if(!Utils.isNullOrEmpty(baseUrl)){
            editText.setText(baseUrl);
        }

        findViewById(R.id.button_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //测试
                testNetwork();
            }
        });

        //恢复默认
        /*Button resetButton = findViewById(R.id.button_reset);
        resetButton.setVisibility(fromSet ? View.VISIBLE : View.GONE);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset = true;
                //测试
                testNetwork();
            }
        });*/
    }

    private void testNetwork(){

        String baseUrl = editText.getText().toString();
        //判断格式
        if(baseUrl.endsWith("/")){
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        if(!(baseUrl.startsWith("http://") || baseUrl.startsWith("https://"))){
            baseUrl = "http://" + baseUrl;
        }
        String newBaseUrl = reset ? UrlConfig.defUrl : baseUrl;

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
                    URL url = new URL(UrlConfig.userIsLoginUrl.replace(UrlConfig.baseUrl, newBaseUrl));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false);
                    //超时时间为30秒
                    connection.setConnectTimeout(30 * 1000);
                    //获取返回码
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        Message msg = mHandler.obtainMessage(MsgType_Login);
                        msg.obj = newBaseUrl;
                        mHandler.sendMessage(msg);
                    }else {
                        Message msg = mHandler.obtainMessage(MsgType_ChangeUrl);
                        msg.obj = newBaseUrl;
                        mHandler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //网络连接错误
                    Message msg = mHandler.obtainMessage(MsgType_ChangeUrl);
                    msg.obj = newBaseUrl;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    private void handleChangeUrl(){
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("")
                .setMessage("服务器连接失败。")
                .addAction("重试", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        testNetwork();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void handleLogin(String baseUrl){
        //更新
        UrlConfig.updateBaseUrl(baseUrl);
        //缓存
        if(!reset){
            GestureUtils.set(this, GestureUtils.TEST_BASE_URL, baseUrl);
        }
        //跳转登录
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            tipDialog.dismiss();

            //处理数据
            String newBaseUrl = (String) msg.obj;
            switch (msg.what) {
                case MsgType_Login:
                    handleLogin(newBaseUrl);
                    break;
                case MsgType_ChangeUrl:
                    handleChangeUrl();
                    break;
                default:
                    break;
            }
        }
    };

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
