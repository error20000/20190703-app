package com.jian.system.gesture.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;


import com.jian.system.LoginActivity;
import com.jian.system.MainActivity;
import com.jian.system.gesture.util.GestureUtils;

import java.util.ArrayList;
import java.util.List;

public class GestureBaseActivity extends Activity {

    protected Handler handler = new Handler(Looper.getMainLooper());

    private String splitTag = "-";

    /**
     * 读取密码，并且输出String
     *
     * @param tag
     * @param pwd
     * @return
     */
    protected String showPwd(String tag, List<Integer> pwd) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < pwd.size(); i++) {//并且输出密码
            res.append(pwd.get(i) + splitTag);
        }
        Log.d(tag, "" + res.toString());
        return res.toString();
    }

    /**
     * 解析密码，并且输出List
     *
     * @param pwd
     * @return
     */
    protected List<Integer> parsePwdStr(String pwd) {
        List<Integer> res = new ArrayList<>();
        String[] s = pwd.trim().split(splitTag);
        for (int i = 0; i < s.length; i++) {
            if (TextUtils.isEmpty(s[i]))
                break;
            res.add(Integer.parseInt(s[i].trim()));
        }
        return res;
    }

    /**
     * 保存密码 到本地文件
     *
     * @param pwd
     */
    protected void savePwd(String pwd) {
        GestureUtils.set(this, GestureUtils.USER_GESTURE, pwd);
    }

    /**
     * 读取密码
     *
     * @return
     */
    protected String getPwd() {
        String res = GestureUtils.get(this, GestureUtils.USER_GESTURE);
        return res;
    }

    /**
     * 文案的左右摇动动画
     */
    protected void animate(View tv_go) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tv_go, "translationX", -20, 20, -20, 0);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        objectAnimator.start();
    }

    /**
     * 如果解锁失败
     */
    protected void onCheckFailed() {
//        finish();
    }

    /**
     * 如果解锁成功
     */
    protected void onCheckSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
