
package com.jian.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.fragment.MainFragment;
import com.jian.system.fragment.components.EquipDetailFragment;
import com.jian.system.gesture.GesturePwdCheckActivity;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment;
import com.sonnyjack.library.qrcode.QrCodeUtils;

import java.lang.reflect.Field;


@DefaultFirstFragment(value = MainFragment.class)
public class MainActivity extends QMUIFragmentActivity {

    @Override
    protected int getContextViewId() {
        return R.id.main;
    }

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment) {
        return QMUIFragmentActivity.intentOf(context, MainActivity.class, firstFragment);
    }

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment,
                            @Nullable Bundle fragmentArgs) {
        return QMUIFragmentActivity.intentOf(context, MainActivity.class, firstFragment, fragmentArgs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("MainActivity", "dispatchTouchEvent ACTION_DOWN" );
                mHandler.removeCallbacksAndMessages(null);
                break;

            case MotionEvent.ACTION_UP:
                Log.d("MainActivity", "dispatchTouchEvent ACTION_UP" );
                Application.isBackLock = true;
                //获取锁屏时间
                String time = GestureUtils.get(this, GestureUtils.USER_LOCK_SCREEN_TIME);
                if(Utils.isNullOrEmpty(time)){
                    time = GestureUtils.USER_LOCK_SCREEN_TIME_DEF;
                }
                if(!"never".equals(time)){
                    mHandler.sendEmptyMessageDelayed(0, Integer.parseInt(time) * 1000);
                }
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
        Log.d("MainActivity", "onPause");
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        fixInputMethodManagerLeak(this);
        super.onDestroy();
        Log.d("MainActivity", "onDestroy");
    }

    private void showCheckGesture(){
        Intent intent = new Intent(this, GesturePwdCheckActivity.class);
        intent.putExtra(GestureUtils.Gesture_Model_Type, GestureUtils.Gesture_Model_Type_Lock_Screen);
        startActivity(intent);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //处理数据
            switch (msg.what) {
                case 0:
                    showCheckGesture();
                    break;
                default:
                    break;
            }

        }
    };

    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String [] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0;i < arr.length;i ++) {
            String param = arr[i];
            try{
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }
}
