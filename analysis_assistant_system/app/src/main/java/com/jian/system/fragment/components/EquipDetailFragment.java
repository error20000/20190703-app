
package com.jian.system.fragment.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
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
import com.jian.system.fragment.MsgLayout;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.jian.system.view.SearchDialogBuilder;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
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
import java.util.Date;
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
    private final int MsgType_Unusual = 109;


    private String from;
    private String remarks = "";
    private Equip equip;
    private JSONObject detail;
    private String sAid_ID = "";
    private String sEquip_ID = "";
    private String sEquip_Type = "";
    private List<Dict> equipTypeData = new ArrayList<>();
    private List<Dict> equipStatusData = new ArrayList<>();
    private List<Dict> equipManufacturerData = new ArrayList<>();
    private List<StoreType> storeTypeData = new ArrayList<>();
    private List<Store> storeData = new ArrayList<>();
    private List<Nfc> nfcData = new ArrayList<>();
    private List<EquipLog> historyData = new ArrayList<>();
    private List<JSONObject> aidAllData = new ArrayList<>();
    private List<JSONObject> userAidData = new ArrayList<>();
    private Date timeFilterStartDate;

    //Ais
    private List<Dict> equipAisMMSIXOptions = new ArrayList<>();
    //Radar
    private List<Dict> equipRadarNOOptions = new ArrayList<>();
    private List<Dict> equipRadarBandOptions = new ArrayList<>();
    //Telemetry
    private List<Dict> equipTelemetryModeOptions = new ArrayList<>();
    //Battery
    private List<Dict> equipBatteryTypeOptions = new ArrayList<>();
    //SolarEnergy
    private List<Dict> equipSolarTypeOptions = new ArrayList<>();
    //SpareLamp
    //ViceLamp
    //Lamp
    private List<Dict> equipLampTypeOptions = new ArrayList<>();
    private List<Dict> equipLampLensOptions = new ArrayList<>();
    private List<Dict> equipLampTelemetryOptions = new ArrayList<>();

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
        sEquip_ID = bundle.getString("id");
        sEquip_Type = bundle.getString("type");

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
                .addItem("异常", "unusual")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        //增加日期选择
                        TimeFilterDialogBuilder2 builder2 = new TimeFilterDialogBuilder2(getContext());
                        builder2.setTitle("请选择")
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
                                                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE,  new QMUIDialogAction.ActionListener() {
                                                    @Override
                                                    public void onClick(QMUIDialog dialog2, int index) {
                                                        dialog2.dismiss();
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .create(mCurrentDialogStyle).show();
                                    }
                                })
                                .addAction("下一步", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                        //主要流程
                                        if("inStore".equals(tag)){
                                            showStore(tag);
                                        }else if("useToAid".equals(tag)){
                                            showAid(tag);
                                        }else{
                                            showEditTextDialog(tag);
                                        }
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                    }
                })
                .build()
                .show();
    }

    /*private void showEditTextDialog(String tag) {
        TimeFilterDialogBuilder builder = new TimeFilterDialogBuilder(getContext());
        builder.setTitle("")
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
    }*/

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
                                .addAction(0,"确定", QMUIDialogAction.ACTION_PROP_NEGATIVE,  new QMUIDialogAction.ActionListener() {
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
                            case "unusual": //异常
                                itemUnusual();
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

        QMUICommonListItemView equipNfc = mGroupListView.createItemView("NFC标签");
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

        QMUICommonListItemView equipManufacturer = mGroupListView.createItemView("生产厂家");
        String equipManufacturerName = FormatUtils.formatDict(equip.getsEquip_Manufacturer(), equipManufacturerData);
        equipManufacturer.setDetailText(equipManufacturerName);

        QMUICommonListItemView equipMMode = mGroupListView.createItemView("厂方型号");
        equipMMode.setDetailText(equip.getsEquip_MModel());

        QMUICommonListItemView equipMBrand = mGroupListView.createItemView("品牌");
        equipMBrand.setDetailText(equip.getsEquip_MBrand());

        QMUICommonListItemView equipArrivalDate = mGroupListView.createItemView("到货日期");
        String arrivalDate = FormatUtils.formatDate("yyyy-MM-dd", equip.getdEquip_ArrivalDate());
        equipArrivalDate.setDetailText(arrivalDate);


        // ------------------------------------------------------------------------------ Ais
        QMUICommonListItemView equipAisMMSIX = mGroupListView.createItemView("MMSIX号");
        equipAisMMSIX.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_AIS)){
            String equipAisMMSIXName = FormatUtils.formatDict(detail.getString("sAis_MMSIX"), equipAisMMSIXOptions);
            equipAisMMSIX.setDetailText(equipAisMMSIXName);
            equipAisMMSIX.setVisibility(View.VISIBLE);
        }

        // ------------------------------------------------------------------------------ Radar
        QMUICommonListItemView equipRadarNO = mGroupListView.createItemView("雷达应答器编码");
        equipRadarNO.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Radar)){
            String equipRadarNOName = FormatUtils.formatDict(detail.getString("sRadar_NO"), equipRadarNOOptions);
            equipRadarNO.setDetailText(equipRadarNOName);
            equipRadarNO.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipRadarBand = mGroupListView.createItemView("雷达应答器波段");
        equipRadarBand.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Radar)){
            String equipRadarBandName = FormatUtils.formatDict(detail.getString("sRadar_Band"), equipRadarBandOptions);
            equipRadarBand.setDetailText(equipRadarBandName);
            equipRadarBand.setVisibility(View.VISIBLE);
        }

        // ------------------------------------------------------------------------------ Telemetry
        QMUICommonListItemView equipTelemetryNO = mGroupListView.createItemView("遥控遥测编码");
        equipTelemetryNO.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Telemetry)){
            equipTelemetryNO.setDetailText(detail.getString("sTelemetry_NO"));
            equipTelemetryNO.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipTelemetrySIM = mGroupListView.createItemView("SIM(MMIS)卡号");
        equipTelemetrySIM.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Telemetry)){
            equipTelemetrySIM.setDetailText(detail.getString("sTelemetry_SIM"));
            equipTelemetrySIM.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipTelemetryMode = mGroupListView.createItemView("遥控遥测方式");
        equipTelemetryMode.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Telemetry)){
            String equipTelemetryModeName = FormatUtils.formatDict(detail.getString("sTelemetry_Mode"), equipTelemetryModeOptions);
            equipTelemetryMode.setDetailText(equipTelemetryModeName);
            equipTelemetryMode.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipTelemetryVolt = mGroupListView.createItemView("电压（V）");
        equipTelemetryVolt.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Telemetry)){
            equipTelemetryVolt.setDetailText(detail.getString("lTelemetry_Volt"));
            equipTelemetryVolt.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipTelemetryWatt = mGroupListView.createItemView("功率（W）");
        equipTelemetryWatt.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Telemetry)){
            equipTelemetryWatt.setDetailText(detail.getString("lTelemetry_Watt"));
            equipTelemetryWatt.setVisibility(View.VISIBLE);
        }

        // ------------------------------------------------------------------------------ Battery
        QMUICommonListItemView equipBatteryNO = mGroupListView.createItemView("编码");
        equipBatteryNO.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Battery)){
            equipBatteryNO.setDetailText(detail.getString("sBattery_NO"));
            equipBatteryNO.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipBatteryType = mGroupListView.createItemView("种类");
        equipBatteryType.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Battery)){
            String equipBatteryTypeName = FormatUtils.formatDict(detail.getString("sBattery_Type"), equipBatteryTypeOptions);
            equipBatteryType.setDetailText(equipBatteryTypeName);
            equipBatteryType.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipBatteryVolt = mGroupListView.createItemView("工作电压（V）");
        equipBatteryVolt.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Battery)){
            equipBatteryVolt.setDetailText(detail.getString("lBattery_Volt"));
            equipBatteryVolt.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipBatteryWatt = mGroupListView.createItemView("容量（W）");
        equipBatteryWatt.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Battery)){
            equipBatteryWatt.setDetailText(detail.getString("lBattery_Watt"));
            equipBatteryWatt.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipBatteryConnect = mGroupListView.createItemView("连接方式");
        equipBatteryConnect.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Battery)){
            equipBatteryConnect.setDetailText(detail.getString("sBattery_Connect"));
            equipBatteryConnect.setVisibility(View.VISIBLE);
        }

        // ------------------------------------------------------------------------------ Solar
        QMUICommonListItemView equipSolarNO = mGroupListView.createItemView("编码");
        equipSolarNO.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_SolarEnergy)){
            equipSolarNO.setDetailText(detail.getString("sSolar_NO"));
            equipSolarNO.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipSolarType = mGroupListView.createItemView("种类");
        equipSolarType.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Battery)){
            String equipSolarTypeName = FormatUtils.formatDict(detail.getString("sSolar_Type"), equipSolarTypeOptions);
            equipSolarType.setDetailText(equipSolarTypeName);
            equipSolarType.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipSolarVolt = mGroupListView.createItemView("额定电压（V）");
        equipSolarVolt.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_SolarEnergy)){
            equipSolarVolt.setDetailText(detail.getString("lSolar_Volt"));
            equipSolarVolt.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipSolarWatt = mGroupListView.createItemView("功率（W）");
        equipSolarWatt.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_SolarEnergy)){
            equipSolarWatt.setDetailText(detail.getString("lSolar_Watt"));
            equipSolarWatt.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipSolarConnect = mGroupListView.createItemView("连接方式");
        equipSolarConnect.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_SolarEnergy)){
            equipSolarConnect.setDetailText(detail.getString("sSolar_Connect"));
            equipSolarConnect.setVisibility(View.VISIBLE);
        }

        // ------------------------------------------------------------------------------ SpareLamp
        QMUICommonListItemView equipSLampWatt = mGroupListView.createItemView("功率（W）");
        equipSLampWatt.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_SpareLamp)){
            equipSLampWatt.setDetailText(detail.getString("lSLamp_Watt"));
            equipSLampWatt.setVisibility(View.VISIBLE);
        }

        // ------------------------------------------------------------------------------ ViceLamp
        QMUICommonListItemView equipVLampWatt = mGroupListView.createItemView("功率（W）");
        equipVLampWatt.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_ViceLamp)){
            equipVLampWatt.setDetailText(detail.getString("lVLamp_Watt"));
            equipVLampWatt.setVisibility(View.VISIBLE);
        }

        // ------------------------------------------------------------------------------ Lamp
        QMUICommonListItemView equipLampNO = mGroupListView.createItemView("编码");
        equipLampNO.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Lamp)){
            equipLampNO.setDetailText(detail.getString("sLamp_NO"));
            equipLampNO.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipLampType = mGroupListView.createItemView("类型");
        equipLampType.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Lamp)){
            String equipLampTypeName = FormatUtils.formatDict(detail.getString("sLamp_Type"), equipLampTypeOptions);
            equipLampType.setDetailText(equipLampTypeName);
            equipLampType.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipLampLens = mGroupListView.createItemView("透镜形状");
        equipLampLens.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Lamp)){
            String equipLampLensName = FormatUtils.formatDict(detail.getString("sLamp_Lens"), equipLampLensOptions);
            equipLampLens.setDetailText(equipLampLensName);
            equipLampLens.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipLampInputVolt = mGroupListView.createItemView("输入电压（V）");
        equipLampInputVolt.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Lamp)){
            equipLampInputVolt.setDetailText(detail.getString("lLamp_InputVolt"));
            equipLampInputVolt.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipLampWatt = mGroupListView.createItemView("功率（W）");
        equipLampWatt.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Lamp)){
            equipLampWatt.setDetailText(detail.getString("lLamp_Watt"));
            equipLampWatt.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipLampTelemetryFlag = mGroupListView.createItemView("遥测遥控接口");
        equipLampTelemetryFlag.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Lamp)){
            equipLampTelemetryFlag.setDetailText("1".equals(detail.getString("lLamp_TelemetryFlag")) ? "是" : "否");
            equipLampTelemetryFlag.setVisibility(View.VISIBLE);
        }

        QMUICommonListItemView equipLampTelemetry = mGroupListView.createItemView("遥测接口类型");
        equipLampTelemetry.setVisibility(View.GONE);
        if(sEquip_Type.equals(Constant.EquipType_Lamp) && "1".equals(detail.getString("lLamp_TelemetryFlag"))){
            String equipLampTelemetryName = FormatUtils.formatDict(detail.getString("sLamp_Telemetry"), equipLampTelemetryOptions);
            equipLampTelemetry.setDetailText(equipLampTelemetryName);
            equipLampTelemetry.setVisibility(View.VISIBLE);
        }

        QMUIGroupListView.newSection(getContext())
                .setTitle("基础信息")
                .addItemView(equipId, null)
                .addItemView(equipNo, null)
                //.addItemView(equipName, null)
                .addItemView(equipType, null)
                .addItemView(equipStatus, null)
                .addItemView(equipDate, null)
                .addItemView(equipNfc, null)
                .addItemView(equipAid, null)
                .addItemView(equipStore, null)
                .addItemView(equipManufacturer, null)
                .addItemView(equipMMode, null)
                .addItemView(equipMBrand, null)
                .addItemView(equipArrivalDate, null)
                //ais
                .addItemView(equipAisMMSIX, null)
                //radar
                .addItemView(equipRadarNO, null)
                .addItemView(equipRadarBand, null)
                //Telemetry
                .addItemView(equipTelemetryNO, null)
                .addItemView(equipTelemetrySIM, null)
                .addItemView(equipTelemetryMode, null)
                .addItemView(equipTelemetryVolt, null)
                .addItemView(equipTelemetryWatt, null)
                //Battery
                .addItemView(equipBatteryNO, null)
                .addItemView(equipBatteryType, null)
                .addItemView(equipBatteryVolt, null)
                .addItemView(equipBatteryWatt, null)
                .addItemView(equipBatteryConnect, null)
                //SolarEnergy
                .addItemView(equipSolarNO, null)
                .addItemView(equipSolarType, null)
                .addItemView(equipSolarVolt, null)
                .addItemView(equipSolarWatt, null)
                .addItemView(equipSolarConnect, null)
                //SpareLamp
                .addItemView(equipSLampWatt, null)
                //SolarEnergy
                .addItemView(equipVLampWatt, null)
                //Lamp
                .addItemView(equipLampNO, null)
                .addItemView(equipLampType, null)
                .addItemView(equipLampLens, null)
                .addItemView(equipLampInputVolt, null)
                .addItemView(equipLampWatt, null)
                .addItemView(equipLampTelemetryFlag, null)
                .addItemView(equipLampTelemetry, null)

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
            hideTips();
            String str =  (String) msg.obj;
            //异常处理
            if(Utils.isNullOrEmpty(str)){
                showToast("网络异常，请检查网络。");
                return;
            }
            JSONObject resData = JSONObject.parseObject(str);
            if (handleErrorCode(resData)) {
                Log.d(TAG, resData.toJSONString());
                return;
            }
            //处理数据
            switch (msg.what){
                case MsgType_Detail:
                    equip = resData.getObject("data", Equip.class);
                    detail = resData.getJSONObject("data");
                    initEquipInfo();
                    break;
                case MsgType_History:
                    JSONArray resArray = resData.getJSONArray("data");
                    for (int i = 0; i < resArray.size(); i++) {
                        historyData.add(resArray.getObject(i, EquipLog.class));
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
            String str =  (String) msg.obj;
            //异常处理
            if(Utils.isNullOrEmpty(str)){
                showToast("网络异常，请检查网络。");
                return;
            }
            JSONObject resData = JSONObject.parseObject(str);
            if (handleErrorCode(resData)) {
                Log.d(TAG, resData.toJSONString());
                return;
            }
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
        //查询器材厂家
        DataUtils.getDictData(Constant.equipManufacturerDict, equipManufacturerData);
        //查询仓库
        DataUtils.getStoreTypeData(storeTypeData);
        DataUtils.getStoreData(storeData);
        //查询NFC
        DataUtils.getNfcAllData(nfcData);
        //查询所有航标
        DataUtils.getAidAllData(aidAllData);
        //查询种类属性
        sEquip_Type = sEquip_Type == null ? "" : sEquip_Type;
        switch (sEquip_Type) {
            case Constant.EquipType_AIS:
                DataUtils.getDictData(Constant.equipAisMMSIXDict, equipAisMMSIXOptions);
                break;
            case Constant.EquipType_Radar:
                DataUtils.getDictData(Constant.equipRadarNODict, equipRadarNOOptions);
                DataUtils.getDictData(Constant.equipRadarBandDict, equipRadarBandOptions);
                break;
            case Constant.EquipType_Telemetry:
                DataUtils.getDictData(Constant.equipTelemetryModeDict, equipTelemetryModeOptions);
                break;
            case Constant.EquipType_Battery:
                DataUtils.getDictData(Constant.equipBatteryTypeDict, equipBatteryTypeOptions);
                break;
            case Constant.EquipType_SolarEnergy:
                DataUtils.getDictData(Constant.equipSolarTypeDict, equipSolarTypeOptions);
                break;
            case Constant.EquipType_SpareLamp:
                break;
            case Constant.EquipType_ViceLamp:
                break;
            case Constant.EquipType_Lamp:
                DataUtils.getDictData(Constant.equipLampTypeDict, equipLampTypeOptions);
                DataUtils.getDictData(Constant.equipLampLensDict, equipLampLensOptions);
                DataUtils.getDictData(Constant.equipLampTelemetryDict, equipLampTelemetryOptions);
                break;
        }
        //查询器材详情
        Map<String, Object> params = new HashMap<>();
        params.put("sEquip_ID", sEquip_ID);
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
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_Detail;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void queryHistory(Map<String, Object> params){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipQueryHistoryUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_History;
                msg.obj = res;
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
                params.put("date", timeFilterStartDate.getTime());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipInStoreUrl, params);
                Message message = mHandler.obtainMessage(MsgType_InStore);
                message.obj = res;
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
                params.put("date", timeFilterStartDate.getTime());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipOutStoreUrl, params);
                Message message = mHandler.obtainMessage(MsgType_OutStore);
                message.obj = res;
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
                params.put("date", timeFilterStartDate.getTime());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipUseToAidUrl, params);
                Message message = mHandler.obtainMessage(MsgType_UseToAid);
                message.obj = res;
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
                params.put("date", timeFilterStartDate.getTime());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipRemoveUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Remove);
                message.obj = res;
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
                params.put("date", timeFilterStartDate.getTime());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipTransportUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Transport);
                message.obj = res;
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
                params.put("date", timeFilterStartDate.getTime());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipToBeTestUrl, params);
                Message message = mHandler.obtainMessage(MsgType_ToBeTest);
                message.obj = res;
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
                params.put("date", timeFilterStartDate.getTime());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipCheckUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Check);
                message.obj = res;
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
                params.put("date", timeFilterStartDate.getTime());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipRepairUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Repair);
                message.obj = res;
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
                params.put("date", timeFilterStartDate.getTime());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipDumpUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Dump);
                message.obj = res;
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

    private void itemUnusual(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sEquip_ID", equip.getsEquip_ID());
                params.put("remarks", remarks);
                params.put("date", timeFilterStartDate.getTime());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipUnusualUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Unusual);
                message.obj = res;
                mItemHandler.sendMessage(message);
            }
        });
    }

    private void handleUnusual(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");
        popBackStack();
    }


    class TimeFilterDialogBuilder2 extends QMUIDialog.AutoResizeDialogBuilder {
        private Context mContext;
        private TextView mTextView1;
        private EditText mEditText;

        public TimeFilterDialogBuilder2(Context context) {
            super(context);
            mContext = context;
            timeFilterStartDate = new Date();
        }

        public TextView getTextView1() {
            return mTextView1;
        }

        public TextView getEditText() {
            return mEditText;
        }

        @Override
        public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
            LinearLayout layoutRow = new LinearLayout(mContext);
            layoutRow.setOrientation(LinearLayout.VERTICAL);
            layoutRow.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            LinearLayout layout1 = new LinearLayout(mContext);
            layout1.setOrientation(LinearLayout.HORIZONTAL);
            layout1.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = QMUIDisplayHelper.dp2px(mContext, 20);
            layout1.setPadding(padding, padding, padding, padding);
            //日期标签
            TextView mTextViewLine = new AppCompatTextView(mContext);
            mTextViewLine.setText("日期：");
            LinearLayout.LayoutParams lineLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lineLP.leftMargin = QMUIDisplayHelper.dp2px(getContext(), 10);
            lineLP.rightMargin = QMUIDisplayHelper.dp2px(getContext(), 10);
            mTextViewLine.setLayoutParams(lineLP);
            layout1.addView(mTextViewLine);
            //日期
            mTextView1 = new AppCompatTextView(mContext);
            QMUIViewHelper.setBackgroundKeepingPadding(mTextView1, QMUIResHelper.getAttrDrawable(mContext, R.attr.qmui_list_item_bg_with_border_bottom));
            if(timeFilterStartDate == null){
                mTextView1.setHint("请选择日期");
            }else{
                mTextView1.setText(FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", timeFilterStartDate));
            }
            layout1.addView(mTextView1);
            mTextView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "请选择日期");
                    TimePickerDialog mDialogTime = new TimePickerDialog.Builder()
                            .setCallBack(new OnDateSetListener() {
                                @Override
                                public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                    timeFilterStartDate = new Date(millseconds);
                                    String str = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", timeFilterStartDate);
                                    timeFilterStartDate = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", str);
                                    mTextView1.setText(str);
                                }
                            })
                            .setCancelStringId("取消")
                            .setSureStringId("确定")
                            .setTitleStringId("选择日期")
                            .setYearText("年")
                            .setMonthText("月")
                            .setDayText("日")
                            .setHourText("时")
                            .setMinuteText("分")
                            .setCyclic(false)
                            //.setMinMillseconds(System.currentTimeMillis())
                            //.setMaxMillseconds(System.currentTimeMillis() + tenYears)
                            .setCurrentMillseconds(timeFilterStartDate == null ? System.currentTimeMillis() : timeFilterStartDate.getTime())
                            .setThemeColor(getResources().getColor(R.color.app_color_blue))
                            .setType(Type.ALL)
                            .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                            .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                            .setWheelItemTextSize(12)
                            .build();
                    mDialogTime.show(getFragmentManager(), "startDate");
                }
            });
            layoutRow.addView(layout1);
            return layoutRow;
        }
    }

   /*class TimeFilterDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
        private Context mContext;
        private TextView mTextView1;
        private EditText mEditText;

        public TimeFilterDialogBuilder(Context context) {
            super(context);
            mContext = context;
            timeFilterStartDate = new Date();
        }

        public TextView getTextView1() {
            return mTextView1;
        }

        public TextView getEditText() {
            return mEditText;
        }

        @Override
        public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
            LinearLayout layoutRow = new LinearLayout(mContext);
            layoutRow.setOrientation(LinearLayout.VERTICAL);
            layoutRow.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            LinearLayout layout1 = new LinearLayout(mContext);
            layout1.setOrientation(LinearLayout.HORIZONTAL);
            layout1.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = QMUIDisplayHelper.dp2px(mContext, 20);
            layout1.setPadding(padding, padding, padding, padding);
            //日期标签
            TextView mTextViewLine = new AppCompatTextView(mContext);
            mTextViewLine.setText("日期：");
            LinearLayout.LayoutParams lineLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lineLP.leftMargin = QMUIDisplayHelper.dp2px(getContext(), 10);
            lineLP.rightMargin = QMUIDisplayHelper.dp2px(getContext(), 10);
            mTextViewLine.setLayoutParams(lineLP);
            layout1.addView(mTextViewLine);
            //日期
            mTextView1 = new AppCompatTextView(mContext);
            QMUIViewHelper.setBackgroundKeepingPadding(mTextView1, QMUIResHelper.getAttrDrawable(mContext, R.attr.qmui_list_item_bg_with_border_bottom));
            if(timeFilterStartDate == null){
                mTextView1.setHint("请选择日期");
            }else{
                mTextView1.setText(FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", timeFilterStartDate));
            }
            layout1.addView(mTextView1);
            mTextView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "请选择日期");
                    TimePickerDialog mDialogTime = new TimePickerDialog.Builder()
                            .setCallBack(new OnDateSetListener() {
                                @Override
                                public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                    timeFilterStartDate = new Date(millseconds);
                                    String str = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", timeFilterStartDate);
                                    timeFilterStartDate = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", str);
                                    mTextView1.setText(str);
                                }
                            })
                            .setCancelStringId("取消")
                            .setSureStringId("确定")
                            .setTitleStringId("选择日期")
                            .setYearText("年")
                            .setMonthText("月")
                            .setDayText("日")
                            .setHourText("时")
                            .setMinuteText("分")
                            .setCyclic(false)
                            //.setMinMillseconds(System.currentTimeMillis())
                            //.setMaxMillseconds(System.currentTimeMillis() + tenYears)
                            .setCurrentMillseconds(timeFilterStartDate == null ? System.currentTimeMillis() : timeFilterStartDate.getTime())
                            .setThemeColor(getResources().getColor(R.color.app_color_blue))
                            .setType(Type.ALL)
                            .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                            .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                            .setWheelItemTextSize(12)
                            .build();
                    mDialogTime.show(getFragmentManager(), "startDate");
                }
            });
            layoutRow.addView(layout1);

            LinearLayout layout2 = new LinearLayout(mContext);
            layout2.setOrientation(LinearLayout.HORIZONTAL);
            layout2.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding2 = QMUIDisplayHelper.dp2px(mContext, 20);
            layout2.setPadding(padding2, padding2, padding2, padding2);
            //备注标签
            TextView mTextViewLine2 = new AppCompatTextView(mContext);
            mTextViewLine2.setText("备注：");
            LinearLayout.LayoutParams lineLP2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lineLP2.leftMargin = QMUIDisplayHelper.dp2px(getContext(), 10);
            lineLP2.rightMargin = QMUIDisplayHelper.dp2px(getContext(), 10);
            mTextViewLine.setLayoutParams(lineLP2);
            layout2.addView(mTextViewLine2);
            //备注
            LinearLayout.LayoutParams lineLP3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mEditText = new AppCompatEditText(mContext);
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
            mEditText.setTextSize(QMUIDisplayHelper.px2sp(getContext(), QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_common_list_item_detail_h_text_size) ) );
            mEditText.setLayoutParams(lineLP3);
            mEditText.setHint("请填写备注，非必填！");
            layout2.addView(mEditText);
            layoutRow.addView(layout2);
            return layoutRow;
        }
    }*/
}
