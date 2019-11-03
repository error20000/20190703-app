
package com.jian.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

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
                mHandler.removeCallbacksAndMessages(null);
                break;

            case MotionEvent.ACTION_UP:
                //获取锁屏时间
                String time = GestureUtils.get(this, GestureUtils.USER_LOCK_SCREEN_TIME);
                if(Utils.isNullOrEmpty(time)){
                    time = GestureUtils.USER_LOCK_SCREEN_TIME_DEF;
                }
                mHandler.sendEmptyMessageDelayed(0, Integer.parseInt(time) * 1000);
                break;

        }
        return super.dispatchTouchEvent(ev);
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
}
