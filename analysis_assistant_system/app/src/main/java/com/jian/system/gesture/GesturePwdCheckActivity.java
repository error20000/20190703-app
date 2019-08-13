package com.jian.system.gesture;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jian.system.R;
import com.jian.system.gesture.base.GestureBaseActivity;
import com.jian.system.gesture.custom.EasyGestureLockLayout;


/**
 * 手势密码 登录校验界面
 */
public class GesturePwdCheckActivity extends GestureBaseActivity {

    TextView tv_go;
    EasyGestureLockLayout layout_parent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gesture_pwd_check);
        initView();
        initLayoutView();
    }

    private void initView() {
        tv_go = findViewById(R.id.tv_go);
        layout_parent = findViewById(R.id.layout_parent);
    }


    protected void initLayoutView() {
        EasyGestureLockLayout.GestureEventCallbackAdapter adapter = new EasyGestureLockLayout.GestureEventCallbackAdapter() {
            @Override
            public void onCheckFinish(boolean succeedOrFailed) {
                String str = succeedOrFailed ? "解锁成功" : "解锁失败";
                Toast.makeText(GesturePwdCheckActivity.this, str, Toast.LENGTH_SHORT).show();
                //跳转主页面
                onCheckSuccess();
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


}
