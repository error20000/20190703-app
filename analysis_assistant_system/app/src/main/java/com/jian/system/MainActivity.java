
package com.jian.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jian.system.fragment.MainFragment;
import com.jian.system.fragment.components.EquipDetailFragment;
import com.jian.system.gesture.GesturePwdCheckActivity;
import com.jian.system.gesture.util.GestureUtils;
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
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, GesturePwdCheckActivity.class);
        intent.putExtra(GestureUtils.LOCK_SCREEN, "lock");
        startActivity(intent);
    }
}
