
package com.jian.system.fragment.components;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baoyachi.stepview.VerticalStepView;
import com.jian.system.R;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Equip;
import com.jian.system.entity.EquipLog;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListSectionHeaderFooterView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EquipDetailFragment extends QMUIFragment {

    private final static String TAG = EquipDetailFragment.class.getSimpleName();
    private String title = "器材详情";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private QMUITipDialog tipDialog;
    private final int MsgType_Detail = 0;
    private final int MsgType_History = 1;

    private Equip equip;
    private List<Dict> equipTypeData = new ArrayList<>();
    private List<Dict> equipStatusData = new ArrayList<>();
    private List<StoreType> storeTypeData = new ArrayList<>();
    private List<Store> storeData = new ArrayList<>();
    private List<EquipLog> historyData = new ArrayList<>();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;
    @BindView(R.id.equip_history_label)
    QMUIGroupListSectionHeaderFooterView mEquipHistoryLabelView;
    @BindView(R.id.equip_history)
    VerticalStepView mVerticalStepView;
    @BindView(R.id.emptyView)
    QMUIEmptyView mEmptyView;

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
        String typeName = equip.getsEquip_Type();
        for(int i = 0; i < equipTypeData.size(); i++){
            Dict node = equipTypeData.get(i);
            if(node.getsDict_NO().equals(equip.getsEquip_Type())){
                typeName = node.getsDict_Name();
                break;
            }
        }
        item2.setDetailText(typeName);
        item2.setTag(2);

        QMUICommonListItemView item3 = mGroupListView.createItemView("器材状态");
        String statusName = equip.getsEquip_Status();
        for(int i = 0; i < equipStatusData.size(); i++){
            Dict node = equipStatusData.get(i);
            if(node.getsDict_NO().equals(equip.getsEquip_Status())){
                statusName = node.getsDict_Name();
                break;
            }
        }
        item3.setDetailText(statusName);
        item3.setTag(3);

        QMUICommonListItemView item4 = mGroupListView.createItemView("NFC标签ID");
        item4.setDetailText(equip.getsEquip_NfcID());
        item4.setTag(4);

        QMUICommonListItemView item5 = mGroupListView.createItemView("航标ID");
        item5.setDetailText(equip.getsEquip_AidID());
        item5.setTag(5);

        QMUICommonListItemView item6 = mGroupListView.createItemView("创建日期");
        if(equip.getdEquip_CreateDate() != null){
            String date = new SimpleDateFormat("yyyy-MM-dd").format(equip.getdEquip_CreateDate());
            item6.setDetailText(date);
        }
        item6.setTag(6);

        QMUICommonListItemView item7 = mGroupListView.createItemView("器材编码");
        item7.setDetailText(equip.getsEquip_NO());
        item7.setTag(7);

        QMUICommonListItemView item8 = mGroupListView.createItemView("器材名称");
        item8.setDetailText(equip.getsEquip_Name());
        item8.setTag(8);

        /*QMUICommonListItemView item9 = mGroupListView.createItemView("一级仓库");
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
        item12.setTag(12);*/

        QMUICommonListItemView item13 = mGroupListView.createItemView("仓库");
        String storeTypeName = "";
        String storeName2 = "";
        String storeName3 = "";
        String storeName4 = "";
        for(int i = 0; i < storeTypeData.size(); i++){
            StoreType node = storeTypeData.get(i);
            if(node.getsStoreType_ID().equals(equip.getsEquip_StoreLv1())){
                storeTypeName = node.getsStoreType_Name();
                break;
            }
        }
        for(int i = 0; i < storeData.size(); i++){
            Store node = storeData.get(i);
            if(node.getsStore_ID().equals(equip.getsEquip_StoreLv2())){
                storeName2 = node.getsStore_Name();
            }
            if(node.getsStore_ID().equals(equip.getsEquip_StoreLv3())){
                storeName3 = node.getsStore_Name();
            }
            if(node.getsStore_ID().equals(equip.getsEquip_StoreLv4())){
                storeName4 = node.getsStore_Name();
            }
        }
        String storeName = "";
        storeName += Utils.isNullOrEmpty(storeTypeName) ? "": storeTypeName;
        storeName += Utils.isNullOrEmpty(storeName2) ? "": "/"+storeName2;
        storeName += Utils.isNullOrEmpty(storeName3) ? "": "/"+storeName3;
        storeName += Utils.isNullOrEmpty(storeName4) ? "": "/"+storeName4;
        item13.setDetailText(storeName);
        item13.setTag(13);

        QMUIGroupListView.newSection(getContext())
                .setTitle("基础信息")
                .addItemView(item1, null)
                .addItemView(item7, null)
                .addItemView(item8, null)
                .addItemView(item2, null)
                .addItemView(item3, null)
                .addItemView(item4, null)
                //.addItemView(item5, null)
                .addItemView(item6, null)
                //.addItemView(item9, null)
                //.addItemView(item10, null)
                //.addItemView(item11, null)
                //.addItemView(item12, null)
                .addItemView(item13, null)
                .addTo(mGroupListView);

        //设置历史信息
        mEquipHistoryLabelView.setText("历史记录");
        initEquipHistoryInfo();
    }

    @SuppressLint("NewApi")
    private  void initEquipHistoryInfo(){
        if(historyData.size() == 0){
            mEmptyView.setDetailText("抱歉，没有更多数据");
            mEmptyView.setVisibility(View.VISIBLE);
            mVerticalStepView.setVisibility(View.GONE);
            return;
        }
        mEmptyView.setVisibility(View.GONE);
        mVerticalStepView.setVisibility(View.VISIBLE);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < historyData.size(); i++) {
            list.add(historyData.get(i).getsELog_Describe());
        }

        Drawable compltedIcon = ContextCompat.getDrawable(getActivity(), R.drawable.complted);
        compltedIcon.setTint(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_5));

        Drawable defaultIcon = ContextCompat.getDrawable(getActivity(), R.drawable.default_icon);
        defaultIcon.setTint(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_5));

        mVerticalStepView.setStepsViewIndicatorComplectingPosition(list.size() - 1)//设置完成的步数
                .reverseDraw(true)//default is true
                .setStepViewTexts(list)//总步骤
                .setLinePaddingProportion(0.85f)//设置indicator线与线间距的比例系数
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_5))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_5))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_5))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_5))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(defaultIcon)//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(defaultIcon)//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(defaultIcon);//设置StepsViewIndicator AttentionIcon
    }


    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            JSONObject resObj = (JSONObject) msg.obj;
            if(resObj.getInteger("code") < 0){
                QMUITipDialog tipDialog2 = new QMUITipDialog.Builder(getContext())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(resObj.getString("msg"))
                        .create();
                tipDialog2.show();
                mGroupListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog2.dismiss();
                    }
                }, 1500);
                return;
            }

            //处理数据
            switch (msg.what){
                case MsgType_Detail:
                    equip = resObj.getObject("data", Equip.class);
                    initEquipInfo();
                    tipDialog.dismiss();
                    break;
                case MsgType_History:
                    JSONArray resData = resObj.getJSONArray("data");
                    for (int i = 0; i < resData.size(); i++) {
                        historyData.add(resData.getObject(i, EquipLog.class));
                    }
                    initEquipHistoryInfo();
                    break;
                default:
                    break;
            }
        }
    };


    private void initData(){
        //查询数据 -- 判断网络

        tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        //查询器材种类
        DataUtils.getDictData("EquipType", equipTypeData);
        //查询器材状态
        DataUtils.getDictData("EquipStatus", equipStatusData);
        //查询仓库
        DataUtils.getStoreTypeData(storeTypeData);
        DataUtils.getStoreData(storeData);
        //查询器材详情
        Bundle bundle = this.getArguments();
        String id = bundle.getString("id");
        Map<String, Object> params = new HashMap<>();
        params.put("sEquip_ID", id);
        queryDetail(params);
        queryHistory(params);
    }

    private void queryDetail(Map<String, Object> params){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipQueryDetailUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.equipQueryDetailUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_Detail;
                msg.obj = resObj;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void queryHistory(Map<String, Object> params){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipQueryHistoryUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.equipQueryHistoryUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_History;
                msg.obj = resObj;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void refreshData(){
        mGroupListView.getSection(0);
        Log.d(TAG, mGroupListView.getSectionCount()+"");
        initEquipInfo();
    }

}
