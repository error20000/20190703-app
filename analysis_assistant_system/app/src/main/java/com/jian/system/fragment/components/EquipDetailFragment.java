
package com.jian.system.fragment.components;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baoyachi.stepview.VerticalStepView;
import com.jian.system.R;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Aid;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Equip;
import com.jian.system.entity.EquipLog;
import com.jian.system.entity.Nfc;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.jian.system.view.SearchDialogBuilder;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.DataProvider;
import chihane.jdaddressselector.ISelectAble;
import chihane.jdaddressselector.SelectedListener;
import chihane.jdaddressselector.Selector;

public class EquipDetailFragment extends QMUIFragment {

    private final static String TAG = EquipDetailFragment.class.getSimpleName();
    private String title = "器材详情";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private QMUITipDialog tipDialog;
    private BottomDialog selectorDialog;
    private final int MsgType_Detail = 0;
    private final int MsgType_History = 1;

    private final int MsgType_InStore = 100;
    private final int MsgType_OutStore = 101;
    private final int MsgType_UseToAid = 102;
    private final int MsgType_Remove = 103;
    private final int MsgType_Transport = 104;
    private final int MsgType_ToBeTest = 105;
    private final int MsgType_Check = 106;
    private final int MsgType_Repair = 107;
    private final int MsgType_Dump = 108;


    private String from;
    private String remarks = "";
    private Equip equip;
    private String sAid_ID = "";
    private List<Dict> equipTypeData = new ArrayList<>();
    private List<Dict> equipStatusData = new ArrayList<>();
    private List<StoreType> storeTypeData = new ArrayList<>();
    private List<Store> storeData = new ArrayList<>();
    private List<Nfc> nfcData = new ArrayList<>();
    private List<EquipLog> historyData = new ArrayList<>();
    private List<JSONObject> aidAllData = new ArrayList<>();
    private List<JSONObject> userAidData = new ArrayList<>();

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

        Bundle bundle = this.getArguments();
        from =  bundle.getString("from");

        initTopBar();
        initData();

        return rootView;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("nfc".equals(from) || "scan".equals(from)){
                    getFragmentManager().popBackStack();
                }else{
                    popBackStack();
                }
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
                .addItem("入库", "inStore")
                .addItem("出库", "outStore")
                .addItem("使用", "useToAid")
                .addItem("拆除", "remove")
                .addItem("运输", "transport")
                .addItem("待检测", "toBeTest")
                .addItem("检测", "check")
                .addItem("维修", "repair")
                .addItem("报废", "dump")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        if("inStore".equals(tag)){
                            showStore(tag);
                        }else if("useToAid".equals(tag)){
                            showAid(tag);
                        }else{
                            showEditTextDialog(tag);
                        }
                    }
                })
                .build()
                .show();
    }

    private void showEditTextDialog(String tag) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("备注")
                .setPlaceholder("请填写备注，非必填！")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction(0, "取消", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener(){
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        //二次确认
                        new QMUIDialog.MessageDialogBuilder(getActivity())
                                .setTitle("")
                                .setMessage("确定要取消本次操作吗？")
                                .addAction("取消", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog2, int index) {
                                        dialog2.dismiss();
                                    }
                                })
                                .addAction("确定",  new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog2, int index) {
                                        dialog2.dismiss();
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                    }
                })
                .addAction("提交", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        remarks = builder.getEditText().getText().toString();
                        dialog.dismiss();
                        switch (tag) {
                            case "inStore": //入库
                                itemInStore();
                                break;
                            case "outStore": //出库
                                itemOutStore();
                                break;
                            case "useToAid": //使用
                                itemUseToAid();
                                break;
                            case "remove": //拆除
                                itemRemove();
                                break;
                            case "transport": //运输
                                itemTransport();
                                break;
                            case "toBeTest": //待检测
                                itemToBeTest();
                                break;
                            case "check": //检测
                                itemCheck();
                                break;
                            case "repair": //维修
                                itemRepair();
                                break;
                            case "dump": //报废
                                itemDump();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void initEquipInfo() {

        if(equip == null){
            return;
        }

        QMUICommonListItemView equipId = mGroupListView.createItemView("ID");
        equipId.setDetailText(equip.getsEquip_ID());

        QMUICommonListItemView equipNo = mGroupListView.createItemView("器材编码");
        equipNo.setDetailText(equip.getsEquip_NO());

        QMUICommonListItemView equipName = mGroupListView.createItemView("器材名称");
        equipName.setDetailText(equip.getsEquip_Name());

        QMUICommonListItemView equipType = mGroupListView.createItemView("器材类型");
        String typeName = FormatUtils.formatDict(equip.getsEquip_Type(), equipTypeData);
        equipType.setDetailText(typeName);

        QMUICommonListItemView equipStatus = mGroupListView.createItemView("器材状态");
        String statusName = FormatUtils.formatDict(equip.getsEquip_Status(), equipStatusData);
        equipStatus.setDetailText(statusName);

        QMUICommonListItemView equipDate = mGroupListView.createItemView("创建日期");
        String date = FormatUtils.formatDate("yyyy-MM-dd", equip.getdEquip_CreateDate());
        equipDate.setDetailText(date);

        QMUICommonListItemView equipNfc = mGroupListView.createItemView("所属NFC标签");
        String nfcName = FormatUtils.formatNFC(equip.getsEquip_NfcID(), nfcData);
        equipNfc.setDetailText(nfcName);

        QMUICommonListItemView equipAid = mGroupListView.createItemView("所属航标");
        String aidName = FormatUtils.formatAidJSONObject(equip.getsEquip_AidID(), aidAllData);
        equipAid.setDetailText(aidName);

        QMUICommonListItemView equipStore = mGroupListView.createItemView("所属仓库");
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
        equipStore.setDetailText(storeName);

        QMUIGroupListView.newSection(getContext())
                .setTitle("基础信息")
                .addItemView(equipId, null)
                .addItemView(equipNo, null)
                .addItemView(equipName, null)
                .addItemView(equipType, null)
                .addItemView(equipStatus, null)
                .addItemView(equipDate, null)
                .addItemView(equipNfc, null)
                .addItemView(equipAid, null)
                .addItemView(equipStore, null)
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
            String str = "";
            str += FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", historyData.get(i).getdELog_CreateDate());
            str +=" 【" + historyData.get(i).getsELog_Describe();
            str +="】    " + (historyData.get(i).getsELog_Remarks() == null ? "" : historyData.get(i).getsELog_Remarks());
            list.add(str);
        }

        Drawable compltedIcon = ContextCompat.getDrawable(getActivity(), R.drawable.complted);
        compltedIcon.setTint(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_5));

        Drawable defaultIcon = ContextCompat.getDrawable(getActivity(), R.drawable.default_icon);
        defaultIcon.setTint(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_5));

        Drawable attentionIcon = ContextCompat.getDrawable(getActivity(), R.drawable.attention);
        attentionIcon.setTint(ContextCompat.getColor(getActivity(), R.color.qmui_config_color_gray_5));

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
                .setStepsViewIndicatorAttentionIcon(attentionIcon);//设置StepsViewIndicator AttentionIcon
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

    Handler mItemHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            hideTips();
            JSONObject resData = (JSONObject) msg.obj;
            //处理数据
            switch (msg.what) {
                case MsgType_InStore: //入库
                    handleInStore(resData);
                    break;
                case MsgType_OutStore: //出库
                    handleOutStore(resData);
                    break;
                case MsgType_UseToAid: //使用
                    handleUseToAid(resData);
                    break;
                case MsgType_Remove: //拆除
                    handleRemove(resData);
                    break;
                case MsgType_Transport: //运输
                    handleTransport(resData);
                    break;
                case MsgType_ToBeTest: //待检测
                    handleToBeTest(resData);
                    break;
                case MsgType_Check: //检测
                    handleCheck(resData);
                    break;
                case MsgType_Repair: //维修
                    handleRepair(resData);
                    break;
                case MsgType_Dump: //报废
                    handleDump(resData);
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
        DataUtils.getDictData(Constant.equipTypeDict, equipTypeData);
        //查询器材状态
        DataUtils.getDictData(Constant.equipStatusDict, equipStatusData);
        //查询仓库
        DataUtils.getStoreTypeData(storeTypeData);
        DataUtils.getStoreData(storeData);
        //查询NFC
        DataUtils.getNfcAllData(nfcData);
        //查询所有航标
        DataUtils.getAidAllData(aidAllData);
        //查询器材详情
        Bundle bundle = this.getArguments();
        String id = bundle.getString("id");
        Map<String, Object> params = new HashMap<>();
        params.put("sEquip_ID", id);
        queryDetail(params);
        queryHistory(params);
        //查询用户航标
        DataUtils.getAidUserData(userAidData);
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

    private void showStore(String tag){
        Selector selector = new Selector(getActivity(), 4);
        selector.setDataProvider(new DataProvider() {
            @Override
            public void provideData(int currentDeep, String preId, DataReceiver receiver) {
                //根据tab的深度和前一项选择的id，获取下一级菜单项
                Log.i(TAG, "provideData: currentDeep >>> "+currentDeep+" preId >>> "+preId);
                List<ISelectAble> data = new ArrayList<>();
                if(currentDeep == 0){
                    for (int j = 0; j < storeTypeData.size(); j++) {
                        StoreType type = storeTypeData.get(j);
                        data.add(new ISelectAble() {
                            @Override
                            public String getName() {
                                return type.getsStoreType_Name();
                            }
                            @Override
                            public String getId() {
                                return type.getsStoreType_ID();
                            }

                            @Override
                            public Object getArg() {
                                return type;
                            }
                        });
                    }
                }else{
                    for (int j = 0; j < storeData.size(); j++) {
                        Store store = storeData.get(j);
                        if(!preId.equals(store.getsStore_Parent())){
                            continue;
                        }
                        data.add(new ISelectAble() {
                            @Override
                            public String getName() {
                                return store.getsStore_Name();
                            }
                            @Override
                            public String getId() {
                                return store.getsStore_ID();
                            }

                            @Override
                            public Object getArg() {
                                return store;
                            }
                        });
                    }
                    data = data.size() == 0 ? null : data;
                }
                receiver.send(data);
            }
        });
        selector.setSelectedListener(new SelectedListener() {
            @Override
            public void onAddressSelected(ArrayList<ISelectAble> selectAbles) {
                String result = "";
                for (int i = 0; i < selectAbles.size(); i++) {
                    ISelectAble selectAble = selectAbles.get(i);
                    if(selectAble == null){
                        continue;
                    }
                    switch (i){
                        case 0:
                            equip.setsEquip_StoreLv1(selectAble.getId());
                            break;
                        case 1:
                            equip.setsEquip_StoreLv2(selectAble.getId());
                            break;
                        case 2:
                            equip.setsEquip_StoreLv3(selectAble.getId());
                            break;
                        case 3:
                            equip.setsEquip_StoreLv4(selectAble.getId());
                            break;
                    }
                    result += " / " + selectAble.getName();
                }
                result = "".equals(result) ? result : result.substring(" / ".length());
                Log.d(TAG, "result : "+ result);
                selectorDialog.dismiss();
                //显示保存
                showEditTextDialog(tag);
            }
        });
        selectorDialog = new BottomDialog(getActivity());
        selectorDialog.init(getActivity(), selector);
        selectorDialog.show();
    }

    private void showAid(String tag){
        String[] items = new String[userAidData.size()];
        for (int i = 0; i < userAidData.size(); i++) {
            items[i] = userAidData.get(i).getString("sAid_Name");
        }
        new SearchDialogBuilder(getActivity())
                .setTitle("请选择")
                .setHint("请输入航标名称")
                .addItems(items, new SearchDialogBuilder.OnSelectedListiner() {
                    @Override
                    public void onSelected(DialogInterface dialog, SearchDialogBuilder.ItemEntity item) {
                        sAid_ID = userAidData.get(item.getIndex()).getString("sAid_ID");
                        dialog.dismiss();
                        //显示保存
                        showEditTextDialog(tag);
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    //TODO --------------------------------------------------------------------------- handle

    private void showTips(String msg){
        tipDialog = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create();
        tipDialog.show();
    }

    private void hideTips(){
        tipDialog.dismiss();
    }
    private void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    private boolean handleErrorCode(JSONObject resObj){
        if (resObj.getInteger("code") <= 0) {
            showToast(resObj.getString("msg"));
            return true;
        }
        return false;
    }
    private void itemInStore(){
        showTips("入库中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = JSONObject.parseObject(JSONObject.toJSONString(equip), new TypeReference<Map<String, Object>>(){});
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipInStoreUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);

                Message message = mHandler.obtainMessage(MsgType_InStore);
                message.obj = resObj;
                mItemHandler.sendMessage(message);
            }
        });
    }

    private void handleInStore(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("入库成功");
        popBackStack();
    }

    private void itemOutStore(){
        showTips("正在出库");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sEquip_ID", equip.getsEquip_ID());
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipOutStoreUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);

                Message message = mHandler.obtainMessage(MsgType_OutStore);
                message.obj = resObj;
                mItemHandler.sendMessage(message);
            }
        });
    }

    private void handleOutStore(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("出库成功");
        popBackStack();
    }

    private void itemUseToAid(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sEquip_ID", equip.getsEquip_ID());
                params.put("remarks", remarks);
                params.put("sAid_ID", sAid_ID);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipUseToAidUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);

                Message message = mHandler.obtainMessage(MsgType_UseToAid);
                message.obj = resObj;
                mItemHandler.sendMessage(message);
            }
        });
    }

    private void handleUseToAid(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");
        popBackStack();
    }

    private void itemRemove(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sEquip_ID", equip.getsEquip_ID());
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipRemoveUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);

                Message message = mHandler.obtainMessage(MsgType_Remove);
                message.obj = resObj;
                mItemHandler.sendMessage(message);
            }
        });
    }

    private void handleRemove(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");
        popBackStack();
    }

    private void itemTransport(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sEquip_ID", equip.getsEquip_ID());
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipTransportUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);

                Message message = mHandler.obtainMessage(MsgType_Transport);
                message.obj = resObj;
                mItemHandler.sendMessage(message);
            }
        });
    }

    private void handleTransport(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");
        popBackStack();
    }

    private void itemToBeTest(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sEquip_ID", equip.getsEquip_ID());
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipToBeTestUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);

                Message message = mHandler.obtainMessage(MsgType_ToBeTest);
                message.obj = resObj;
                mItemHandler.sendMessage(message);
            }
        });
    }

    private void handleToBeTest(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");
        popBackStack();
    }

    private void itemCheck(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sEquip_ID", equip.getsEquip_ID());
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipCheckUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);

                Message message = mHandler.obtainMessage(MsgType_Check);
                message.obj = resObj;
                mItemHandler.sendMessage(message);
            }
        });
    }

    private void handleCheck(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");
        popBackStack();
    }

    private void itemRepair(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sEquip_ID", equip.getsEquip_ID());
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipRepairUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);

                Message message = mHandler.obtainMessage(MsgType_Repair);
                message.obj = resObj;
                mItemHandler.sendMessage(message);
            }
        });
    }

    private void handleRepair(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");
        popBackStack();
    }

    private void itemDump(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sEquip_ID", equip.getsEquip_ID());
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipDumpUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);

                Message message = mHandler.obtainMessage(MsgType_Dump);
                message.obj = resObj;
                mItemHandler.sendMessage(message);
            }
        });
    }

    private void handleDump(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");
        popBackStack();
    }

}
