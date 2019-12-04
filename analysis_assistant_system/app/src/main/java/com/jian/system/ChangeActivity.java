
package com.jian.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.jian.system.config.UrlConfig;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;


public class ChangeActivity extends AppCompatActivity {

    private final static String TAG = ChangeActivity.class.getSimpleName();

    EditText editText;
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
                //判断格式
                if(baseUrl.endsWith("/")){
                    baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
                }
                if(!(baseUrl.startsWith("http://") || baseUrl.startsWith("https://"))){
                    baseUrl = "http://" + baseUrl;
                }
                //更新
                Log.e("dddddddddddddddd before", UrlConfig.baseUrl);
                Log.e("dddddddddddddddd before", UrlConfig.msgAddUrl);
                UrlConfig.updateBaseUrl(baseUrl);
                Log.e("dddddddddddddddd after", UrlConfig.baseUrl);
                Log.e("dddddddddddddddd after", UrlConfig.msgAddUrl);
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
