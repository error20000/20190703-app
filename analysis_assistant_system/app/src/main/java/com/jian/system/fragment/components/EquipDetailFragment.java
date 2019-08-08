
package com.jian.system.fragment.components;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.R;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Equip;
import com.jian.system.utils.HttpUtils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class EquipDetailFragment extends QMUIFragment {

    private final static String TAG = EquipDetailFragment.class.getSimpleName();
    private String title = "器材详情";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private Equip equip;

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_equip_detail, null);
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

        // 切换其他情况的按钮
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetList();
            }
        });
        mTopBar.setTitle(title);
    }
    private void showBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("出库")
                .addItem("拆除")
                .addItem("运输")
                .addItem("待检测")
                .addItem("检测")
                .addItem("维修")
                .addItem("入库")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        showEditTextDialog(position);
                    }
                })
                .build()
                .show();
    }

    private void showEditTextDialog(int position) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("备注")
                .setPlaceholder("非必填")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        dialog.dismiss();
                        switch (position) {
                            case 0:

                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void initEquipInfo() {
        QMUICommonListItemView item1 = mGroupListView.createItemView("ID");
        item1.setDetailText(equip.getsEquip_ID());
        item1.setTag(1);
        QMUICommonListItemView item2 = mGroupListView.createItemView("器材类型");
        item2.setDetailText(equip.getsEquip_Type());
        item2.setTag(2);
        QMUICommonListItemView item3 = mGroupListView.createItemView("器材状态");
        item3.setDetailText(equip.getsEquip_Status());
        item3.setTag(3);
        QMUICommonListItemView item4 = mGroupListView.createItemView("NFC标签ID");
        item4.setDetailText(equip.getsEquip_NfcID());
        item4.setTag(4);
        QMUICommonListItemView item5 = mGroupListView.createItemView("航标ID");
        item5.setDetailText(equip.getsEquip_AidID());
        item5.setTag(5);
        QMUICommonListItemView item6 = mGroupListView.createItemView("创建日期");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(equip.getdEquip_CreateDate());
        item6.setDetailText(date);
        item6.setTag(6);
        QMUICommonListItemView item7 = mGroupListView.createItemView("器材编码");
        item7.setDetailText(equip.getsEquip_NO());
        item7.setTag(7);
        QMUICommonListItemView item8 = mGroupListView.createItemView("器材名称");
        item8.setDetailText(equip.getsEquip_Name());
        item8.setTag(8);
        QMUICommonListItemView item9 = mGroupListView.createItemView("一级仓库");
        item9.setDetailText(equip.getsEquip_StoreLv1());
        item9.setTag(9);
        QMUICommonListItemView item10 = mGroupListView.createItemView("二级仓库");
        item10.setDetailText(equip.getsEquip_StoreLv2());
        item10.setTag(10);
        QMUICommonListItemView item11 = mGroupListView.createItemView("三级仓库");
        item11.setDetailText(equip.getsEquip_StoreLv3());
        item11.setTag(11);
        QMUICommonListItemView item12 = mGroupListView.createItemView("四级仓库");
        item12.setDetailText(equip.getsEquip_StoreLv4());
        item12.setTag(12);

        QMUIGroupListView.newSection(getContext())
                .setTitle("基础信息")
                .addItemView(item1, mOnClickListenerGroup)
                .addItemView(item2, mOnClickListenerGroup)
                .addItemView(item3, mOnClickListenerGroup)
                .addItemView(item4, mOnClickListenerGroup)
                .addItemView(item5, mOnClickListenerGroup)
                .addItemView(item6, mOnClickListenerGroup)
                .addItemView(item7, mOnClickListenerGroup)
                .addItemView(item8, mOnClickListenerGroup)
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
            JSONObject resObj = (JSONObject) msg.obj;
            if(resObj.getInteger("code") < 0){
                QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(resObj.getString("msg"))
                        .create();
                tipDialog.show();
                return;
            }
            equip = resObj.getObject("data", Equip.class);
            refreshData();
        }
    };


    private void initData(){
        Bundle bundle = this.getArguments();
        String id = bundle.getString("data");
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody body = new FormBody.Builder()
                        .add("sEquip_ID", id)
                        .build();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipQueryUrl, body);
                if(res == null || !"".equals(res)){
                    Log.d(TAG, UrlConfig.equipQueryUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                Message msg = mHandler.obtainMessage();
                msg.obj = resObj;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private void refreshData(){
        mGroupListView.getSection(0);
        Log.d(TAG, mGroupListView.getSectionCount()+"");
        initEquipInfo();
    }
}
