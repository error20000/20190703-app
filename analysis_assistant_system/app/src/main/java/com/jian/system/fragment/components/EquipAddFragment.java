
package com.jian.system.fragment.components;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jian.system.R;
import com.jian.system.entity.Equip;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EquipAddFragment extends QMUIFragment {

    private final static String TAG = EquipAddFragment.class.getSimpleName();
    private String title = "新增器材";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

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

    private void initEquipInfo() {
        QMUICommonListItemView item2 = mGroupListView.createItemView("器材类型");
        item2.setTag(2);
        QMUICommonListItemView item3 = mGroupListView.createItemView("器材状态");
        item3.setTag(3);
        QMUICommonListItemView item4 = mGroupListView.createItemView("NFC标签ID");
        item4.setTag(4);
        QMUICommonListItemView item5 = mGroupListView.createItemView("航标ID");
        item5.setTag(5);
        QMUICommonListItemView item6 = mGroupListView.createItemView("创建日期");
        item6.setTag(6);
        QMUICommonListItemView item7 = mGroupListView.createItemView("器材编码");
        item7.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        EditText editText7 = new EditText(getActivity());
        //editText7.setWidth(QMUIDisplayHelper.dp2px(getActivity(), 300));
        editText7.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText7.setHint("请输入编码");
        editText7.setBackgroundDrawable(QMUIResHelper.getAttrDrawable(getActivity(), R.drawable.qmui_divider_bottom_bitmap));
        editText7.setTextColor(QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_gray_5));
        editText7.setTextSize(QMUIResHelper.getAttrDimen(getActivity(), R.attr.qmui_common_list_item_detail_h_text_size) );
        item7.addAccessoryCustomView(editText7);
        item7.setTag(7);

        QMUICommonListItemView item8 = mGroupListView.createItemView("器材名称");
        item8.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        EditText editText8 = new EditText(getActivity());
        editText8.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //editText7.setWidth(QMUIDisplayHelper.dp2px(getActivity(), 300));
        editText8.setHint("请输入名称");
        item8.addAccessoryCustomView(editText8);
        item8.setTag(8);

        QMUICommonListItemView item9 = mGroupListView.createItemView("一级仓库");
        item9.setTag(9);
        QMUICommonListItemView item10 = mGroupListView.createItemView("二级仓库");
        item10.setTag(10);
        QMUICommonListItemView item11 = mGroupListView.createItemView("三级仓库");
        item11.setTag(11);
        QMUICommonListItemView item12 = mGroupListView.createItemView("四级仓库");
        item12.setTag(12);

        QMUIGroupListView.newSection(getContext())
                .setTitle("基础信息")
                .addItemView(item7, mOnClickListenerGroup)
                .addItemView(item8, mOnClickListenerGroup)
                .addItemView(item2, mOnClickListenerGroup)
                .addItemView(item3, mOnClickListenerGroup)
                .addItemView(item4, mOnClickListenerGroup)
                .addItemView(item5, mOnClickListenerGroup)
                .addItemView(item6, mOnClickListenerGroup)
                .addItemView(item9, mOnClickListenerGroup)
                .addItemView(item10, mOnClickListenerGroup)
                .addItemView(item11, mOnClickListenerGroup)
                .addItemView(item12, mOnClickListenerGroup)
                .addTo(mGroupListView);

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
            //refreshData();
        }
    };

    private void initData(){

        initEquipInfo();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
                mHandler.sendMessage(mHandler.obtainMessage());
            }
        }).start();
    }

}
