
package com.jian.system.fragment.components;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jian.system.R;
import com.jian.system.entity.Equip;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EquipAddFragment extends QMUIFragment {

    private final static String TAG = EquipAddFragment.class.getSimpleName();
    private String title = "新增器材";

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_equip_add, null);
        ButterKnife.bind(this, rootView);

        initTopBar();
        initData();

        return rootView;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle(title);
    }
    private void initEquipInfo(Equip equip) {

    }

    private View.OnClickListener mOnClickListenerGroup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            QMUICommonListItemView viewList = (QMUICommonListItemView) view;
            Log.d(TAG, "选项：" + viewList.getText().toString() + " 点击了");
            switch ((int)viewList.getTag()) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
            Toast.makeText(getActivity(),"选项：" +  viewList.getTag()+ " 点击了",Toast.LENGTH_SHORT).show();
        }
    };

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            refreshData();
        }
    };

    private Equip equip;

    private void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
                equip = new Equip();
                equip.setsEquip_ID("123");
                equip.setsEquip_NO("123");
                equip.setsEquip_Name("123");
                equip.setsEquip_Type("123");
                equip.setsEquip_Status("123");
                equip.setsEquip_NfcID("123");
                equip.setsEquip_AidID("123");
                equip.setsEquip_StoreLv1("123");
                equip.setsEquip_StoreLv2("123");
                equip.setsEquip_StoreLv3("123");
                equip.setsEquip_StoreLv4("123");
                equip.setdEquip_CreateDate(new Date());
                mHandler.sendMessage(mHandler.obtainMessage());
            }
        }).start();
    }

    private void refreshData(){
        initEquipInfo(equip);
    }
}
