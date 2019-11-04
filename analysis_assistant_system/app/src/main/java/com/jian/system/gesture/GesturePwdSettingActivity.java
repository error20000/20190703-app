package com.jian.system.gesture;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jian.system.R;
import com.jian.system.gesture.base.GestureBaseActivity;
import com.jian.system.gesture.custom.EasyGestureLockLayout;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.Utils;

import java.util.List;

/**
 * 手势密码 设置界面
 */
public class GesturePwdSettingActivity extends GestureBaseActivity {

    EasyGestureLockLayout layout_small;
    TextView tv_go;
    TextView tv_redraw;
    EasyGestureLockLayout layout_parent;
    String modelType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gesture_pwd_setting);
        initView();
        initLayoutView();

        Intent intent = getIntent();
        modelType = intent.getStringExtra(GestureUtils.Gesture_Model_Type);
    }

    private void initView() {
        tv_go = findViewById(R.id.tv_go);
        layout_parent = findViewById(R.id.layout_parent);
        layout_small = findViewById(R.id.layout_small);
        tv_redraw = findViewById(R.id.tv_redraw);
    }


    protected void initLayoutView() {

        EasyGestureLockLayout.GestureEventCallbackAdapter adapter = new EasyGestureLockLayout.GestureEventCallbackAdapter() {
            @Override
            public void onSwipeFinish(List<Integer> pwd) {
                layout_small.refreshPwdKeyboard(pwd);//通知另一个小密码盘，将密码点展示出来，但是不展示轨迹线
                tv_redraw.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResetFinish(List<Integer> pwd) {// 当密码设置完成
                savePwd(showPwd("showGesturePwdInt", pwd));//保存密码到本地
                //跳转主页面
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

            @Override
            public void onCheckFinish(boolean succeedOrFailed) {
                String str = succeedOrFailed ? "解锁成功" : "解锁失败";
                Toast.makeText(GesturePwdSettingActivity.this, str, Toast.LENGTH_SHORT).show();
                if (succeedOrFailed) {//如果解锁成功，则切换到set模式
                    layout_parent.switchToResetMode();
                } else {
                    onCheckFailed();
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

        //使用rest模式
        layout_parent.switchToResetMode();

        tv_redraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_parent.initCurrentTimes();
                tv_redraw.setVisibility(View.INVISIBLE);
                layout_small.refreshPwdKeyboard(null);
                tv_go.setText("请重新绘制");
            }
        });
    }



}
