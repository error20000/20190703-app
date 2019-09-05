
package com.jian.system.fragment.components;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jian.system.Application;
import com.jian.system.MainActivity;
import com.jian.system.R;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Equip;
import com.jian.system.entity.Nfc;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.nfc.NfcActivity;
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
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.sonnyjack.library.qrcode.QrCodeUtils;
import com.sonnyjack.permission.IRequestPermissionCallBack;
import com.sonnyjack.permission.PermissionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.DataProvider;
import chihane.jdaddressselector.ISelectAble;
import chihane.jdaddressselector.SelectedListener;
import chihane.jdaddressselector.Selector;


public class EquipAddFragment extends QMUIFragment {

    private final static String TAG = EquipAddFragment.class.getSimpleName();
    private String title = "新增器材";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    private QMUITipDialog tipDialog;
    private BottomDialog selectorDialog;
    private Equip equip = new Equip();
    private Map<String, Object> equipInfo = new HashMap<>();
    private List<Dict> equipTypeData = new ArrayList<>();
    private List<Dict> equipStatusData = new ArrayList<>();
    private List<Dict> equipManufacturerData = new ArrayList<>();
    private List<StoreType> storeTypeData = new ArrayList<>();
    private List<Store> storeData = new ArrayList<>();
    private List<Nfc> nfcUnusedData = new ArrayList<>();

    private QMUICommonListItemView equipNo;
    private QMUICommonListItemView equipName;
    private QMUICommonListItemView equipType;
    private QMUICommonListItemView equipNfc;
    private QMUICommonListItemView equipStore;
    private QMUICommonListItemView equipManufacturer;
    private QMUICommonListItemView equipMMode;
    private QMUICommonListItemView equipMBrand;
    private QMUICommonListItemView equipArrivalDate;
    //Ais
    private List<Dict> equipAisMMSIXOptions = new ArrayList<>();
    private QMUICommonListItemView equipAisMMSIX;
    //Radar
    private List<Dict> equipRadarNOOptions = new ArrayList<>();
    private List<Dict> equipRadarBandOptions = new ArrayList<>();
    private QMUICommonListItemView equipRadarNO;
    private QMUICommonListItemView equipRadarBand;
    //Telemetry
    private List<Dict> equipTelemetryModeOptions = new ArrayList<>();
    private QMUICommonListItemView equipTelemetryNO;
    private QMUICommonListItemView equipTelemetrySIM;
    private QMUICommonListItemView equipTelemetryMode;
    private QMUICommonListItemView equipTelemetryVolt;
    private QMUICommonListItemView equipTelemetryWatt;
    //Battery
    private List<Dict> equipBatteryTypeOptions = new ArrayList<>();
    private QMUICommonListItemView equipBatteryNO;
    private QMUICommonListItemView equipBatteryType;
    private QMUICommonListItemView equipBatteryVolt;
    private QMUICommonListItemView equipBatteryWatt;
    private QMUICommonListItemView equipBatteryConnect;
    //SolarEnergy
    private List<Dict> equipSolarTypeOptions = new ArrayList<>();
    private QMUICommonListItemView equipSolarNO;
    private QMUICommonListItemView equipSolarType;
    private QMUICommonListItemView equipSolarVolt;
    private QMUICommonListItemView equipSolarWatt;
    private QMUICommonListItemView equipSolarConnect;
    //SpareLamp
    private QMUICommonListItemView equipSLampWatt;
    //ViceLamp
    private QMUICommonListItemView equipVLampWatt;
    //Lamp
    private List<Dict> equipLampTypeOptions = new ArrayList<>();
    private List<Dict> equipLampLensOptions = new ArrayList<>();
    private List<Dict> equipLampTelemetryOptions = new ArrayList<>();
    private QMUICommonListItemView equipLampNO;
    private QMUICommonListItemView equipLampType;
    private QMUICommonListItemView equipLampLens;
    private QMUICommonListItemView equipLampInputVolt;
    private QMUICommonListItemView equipLampWatt;
    private QMUICommonListItemView equipLampTelemetryFlag;
    private QMUICommonListItemView equipLampTelemetry;

    private final int MsgType_Add = 0;
    private final int MsgType_Nfc_Add = 1;

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
        mTopBar.addRightTextButton("保存", R.id.topbar_right_about_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextNo = equipNo.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                equip.setsEquip_NO(editTextNo.getText().toString());
                //EditText editTextName = (EditText)equipName.getAccessoryContainerView().getChildAt(0);
                //equip.setsEquip_Name(editTextName.getText().toString());
                equip.setsEquip_Name(equip.getsEquip_NO());
                Log.d(TAG, JSONObject.toJSONString(equip));
                //保存数据
                sendAdd();
            }
        });
        mTopBar.setTitle(title);
    }

    @SuppressLint("NewApi")
    private void initEquipInfo() {
        equipNo = mGroupListView.createItemView("器材编码");
        equipNo.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        View equipNoLayout = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_item_edit, null);
        EditText equipNoEditText = equipNoLayout.findViewById(R.id.item_edit_text);
        equipNoEditText.setSingleLine();
        equipNoEditText.setHint("--请输入编码--");
        ImageView equipNoImage = equipNoLayout.findViewById(R.id.item_image_button);
        Drawable equipNoDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_scan_24dp);
        equipNoDrawable.setTint(ContextCompat.getColor(getActivity(), R.color.app_color_blue));
        equipNoImage.setImageDrawable(equipNoDrawable);
        equipNo.addAccessoryCustomView(equipNoLayout);
        equipNo.setImageDrawable(getResources().getDrawable(R.mipmap.ic_required));
        equipNo.setTag("equipNo");
        equipNoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQrCode();
            }
        });

        equipName = mGroupListView.createItemView("器材名称");
        equipName.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        EditText equipNameEditText = new EditText(getContext());
        equipNameEditText.setLayoutParams(new ViewGroup.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT));
        equipNameEditText.setMaxWidth(QMUIDisplayHelper.dp2px(getContext(), 260));
        equipNameEditText.setBackgroundDrawable(null);
        equipNameEditText.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_5));
        equipNameEditText.setTextSize(QMUIDisplayHelper.px2sp(getContext(), QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_common_list_item_detail_h_text_size) ));
        equipNameEditText.setSingleLine();
        equipNameEditText.setHint("--请输入名称--");
        equipName.addAccessoryCustomView(equipNameEditText);
        equipName.setTag("equipName");

        equipType = mGroupListView.createItemView("器材类型");
        equipType.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipType.setDetailText("--请选择类型--");
        equipType.setImageDrawable(getResources().getDrawable(R.mipmap.ic_required));
        equipType.setTag("equipType");

        equipNfc = mGroupListView.createItemView("NFC标签ID");
        equipNfc.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipNfc.setDetailText("--请选择NFC标签--");
        ImageView equipNfcImage = new ImageView(getActivity());
        Drawable equipNfcDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_nfc_24dp);
        equipNfcDrawable.setTint(ContextCompat.getColor(getActivity(), R.color.app_color_blue));
        equipNfcImage.setImageDrawable(equipNfcDrawable);
        equipNfc.addAccessoryCustomView(equipNfcImage);
        equipNfc.setTag("equipNfc");
        equipNfcImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanNFC();
            }
        });

        equipStore = mGroupListView.createItemView("仓库");
        equipStore.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipStore.setDetailText("--请选择仓库--");
        equipStore.setTag("equipStore");

        equipManufacturer = mGroupListView.createItemView("生产厂家");
        equipManufacturer.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipManufacturer.setDetailText("--请选择--");
        equipManufacturer.setTag("equipManufacturer");

        equipMMode = mGroupListView.createItemView("厂方型号");
        equipMMode.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipMMode.addAccessoryCustomView(newEditText());
        equipMMode.setTag("equipMMode");

        equipMBrand = mGroupListView.createItemView("品牌");
        equipMBrand.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipMBrand.addAccessoryCustomView(newEditText());
        equipMBrand.setTag("equipMBrand");

        equipArrivalDate = mGroupListView.createItemView("到货日期");
        equipArrivalDate.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipArrivalDate.setDetailText("--请选择--");
        equipArrivalDate.setTag("equipArrivalDate");
        equipArrivalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog mDialogTime = new TimePickerDialog.Builder()
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                Date date = new Date(millseconds);
                                String str = FormatUtils.formatDate("yyyy-MM-dd", date);
                                Date temp = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", str + " 00:00:00");
                                equipArrivalDate.setDetailText(str);
                                equip.setdEquip_ArrivalDate(temp);
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
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(getResources().getColor(R.color.app_color_blue))
                        .setType(Type.YEAR_MONTH_DAY)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                        .setWheelItemTextSize(12)
                        .build();
                mDialogTime.show(getFragmentManager(), "arrivalDate");
            }
        });

        // ------------------------------------------------------------------------------ Ais
        equipAisMMSIX = mGroupListView.createItemView("MMSIX号");
        equipAisMMSIX.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipAisMMSIX.setDetailText("--请选择--");
        equipAisMMSIX.setTag("equipAisMMSIX");
        equipAisMMSIX.setVisibility(View.GONE);

        // ------------------------------------------------------------------------------ Radar
        equipRadarNO = mGroupListView.createItemView("雷达应答器编码");
        equipRadarNO.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipRadarNO.setDetailText("--请选择--");
        equipRadarNO.setTag("equipRadarNO");
        equipRadarNO.setVisibility(View.GONE);

        equipRadarBand = mGroupListView.createItemView("雷达应答器波段");
        equipRadarBand.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipRadarBand.setDetailText("--请选择--");
        equipRadarBand.setTag("equipRadarBand");
        equipRadarBand.setVisibility(View.GONE);

        // ------------------------------------------------------------------------------ Telemetry
        equipTelemetryNO = mGroupListView.createItemView("遥控遥测编码");
        equipTelemetryNO.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipTelemetryNO.addAccessoryCustomView(newEditText());
        equipTelemetryNO.setTag("equipTelemetryNO");
        equipTelemetryNO.setVisibility(View.GONE);

        equipTelemetrySIM = mGroupListView.createItemView("SIM(MMIS)卡号");
        equipTelemetrySIM.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipTelemetrySIM.addAccessoryCustomView(newEditText());
        equipTelemetrySIM.setTag("equipTelemetrySIM");
        equipTelemetrySIM.setVisibility(View.GONE);

        equipTelemetryMode = mGroupListView.createItemView("遥控遥测方式");
        equipTelemetryMode.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipTelemetryMode.setDetailText("--请选择--");
        equipTelemetryMode.setTag("equipTelemetryMode");
        equipTelemetryMode.setVisibility(View.GONE);

        equipTelemetryVolt = mGroupListView.createItemView("电压（V）");
        equipTelemetryVolt.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipTelemetryVolt.addAccessoryCustomView(newEditText());
        equipTelemetryVolt.setTag("equipTelemetryVolt");
        equipTelemetryVolt.setVisibility(View.GONE);

        equipTelemetryWatt = mGroupListView.createItemView("功率（W）");
        equipTelemetryWatt.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipTelemetryWatt.addAccessoryCustomView(newEditText());
        equipTelemetryWatt.setTag("equipTelemetryWatt");
        equipTelemetryWatt.setVisibility(View.GONE);

        // ------------------------------------------------------------------------------ Battery
        equipBatteryNO = mGroupListView.createItemView("编码");
        equipBatteryNO.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipBatteryNO.addAccessoryCustomView(newEditText());
        equipBatteryNO.setTag("equipBatteryNO");
        equipBatteryNO.setVisibility(View.GONE);

        equipBatteryType = mGroupListView.createItemView("种类");
        equipBatteryType.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipBatteryType.setDetailText("--请选择--");
        equipBatteryType.setTag("equipBatteryType");
        equipBatteryType.setVisibility(View.GONE);

        equipBatteryVolt = mGroupListView.createItemView("工作电压（V）");
        equipBatteryVolt.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipBatteryVolt.addAccessoryCustomView(newEditText());
        equipBatteryVolt.setTag("equipBatteryVolt");
        equipBatteryVolt.setVisibility(View.GONE);

        equipBatteryWatt = mGroupListView.createItemView("容量（W）");
        equipBatteryWatt.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipBatteryWatt.addAccessoryCustomView(newEditText());
        equipBatteryWatt.setTag("equipBatteryWatt");
        equipBatteryWatt.setVisibility(View.GONE);

        equipBatteryConnect = mGroupListView.createItemView("连接方式");
        equipBatteryConnect.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipBatteryConnect.addAccessoryCustomView(newEditText());
        equipBatteryConnect.setTag("equipBatteryConnect");
        equipBatteryConnect.setVisibility(View.GONE);

        // ------------------------------------------------------------------------------ Solar
        equipSolarNO = mGroupListView.createItemView("编码");
        equipSolarNO.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipSolarNO.addAccessoryCustomView(newEditText());
        equipSolarNO.setTag("equipSolarNO");
        equipSolarNO.setVisibility(View.GONE);

        equipSolarType = mGroupListView.createItemView("种类");
        equipSolarType.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipSolarType.setDetailText("--请选择--");
        equipSolarType.setTag("equipSolarType");
        equipSolarType.setVisibility(View.GONE);

        equipSolarVolt = mGroupListView.createItemView("额定电压（V）");
        equipSolarVolt.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipSolarVolt.addAccessoryCustomView(newEditText());
        equipSolarVolt.setTag("equipSolarVolt");
        equipSolarVolt.setVisibility(View.GONE);

        equipSolarWatt = mGroupListView.createItemView("功率（W）");
        equipSolarWatt.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipSolarWatt.addAccessoryCustomView(newEditText());
        equipSolarWatt.setTag("equipSolarWatt");
        equipSolarWatt.setVisibility(View.GONE);

        equipSolarConnect = mGroupListView.createItemView("连接方式");
        equipSolarConnect.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipSolarConnect.addAccessoryCustomView(newEditText());
        equipSolarConnect.setTag("equipSolarConnect");
        equipSolarConnect.setVisibility(View.GONE);

        // ------------------------------------------------------------------------------ SpareLamp
        equipSLampWatt = mGroupListView.createItemView("功率（W）");
        equipSLampWatt.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipSLampWatt.addAccessoryCustomView(newEditText());
        equipSLampWatt.setTag("equipSLampWatt");
        equipSLampWatt.setVisibility(View.GONE);

        // ------------------------------------------------------------------------------ ViceLamp
        equipVLampWatt = mGroupListView.createItemView("功率（W）");
        equipVLampWatt.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipVLampWatt.addAccessoryCustomView(newEditText());
        equipVLampWatt.setTag("equipVLampWatt");
        equipVLampWatt.setVisibility(View.GONE);

        // ------------------------------------------------------------------------------ Lamp
        equipLampNO = mGroupListView.createItemView("编码");
        equipLampNO.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipLampNO.addAccessoryCustomView(newEditText());
        equipLampNO.setTag("equipLampNO");
        equipLampNO.setVisibility(View.GONE);

        equipLampType = mGroupListView.createItemView("类型");
        equipLampType.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipLampType.setDetailText("--请选择--");
        equipLampType.setTag("equipLampType");
        equipLampType.setVisibility(View.GONE);

        equipLampLens = mGroupListView.createItemView("透镜形状");
        equipLampLens.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipLampLens.setDetailText("--请选择--");
        equipLampLens.setTag("equipLampLens");
        equipLampLens.setVisibility(View.GONE);

        equipLampInputVolt = mGroupListView.createItemView("输入电压（V）");
        equipLampInputVolt.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipLampInputVolt.addAccessoryCustomView(newEditText());
        equipLampInputVolt.setTag("equipLampInputVolt");
        equipLampInputVolt.setVisibility(View.GONE);

        equipLampWatt = mGroupListView.createItemView("功率（W）");
        equipLampWatt.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipLampWatt.addAccessoryCustomView(newEditText());
        equipLampWatt.setTag("equipLampWatt");
        equipLampWatt.setVisibility(View.GONE);

        equipLampTelemetryFlag = mGroupListView.createItemView("遥测遥控接口");
        equipLampTelemetryFlag.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        equipLampTelemetryFlag.setTag("equipLampTelemetryFlag");
        equipLampTelemetryFlag.setVisibility(View.GONE);
        equipLampTelemetryFlag.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    equipInfo.put("lLamp_TelemetryFlag", 1);
                    equipLampTelemetry.setVisibility(View.VISIBLE);
                }else{
                    equipInfo.put("lLamp_TelemetryFlag", 0);
                    equipLampTelemetry.setVisibility(View.GONE);
                }
            }
        });

        equipLampTelemetry = mGroupListView.createItemView("遥测接口类型");
        equipLampTelemetry.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipLampTelemetry.setDetailText("--请选择--");
        equipLampTelemetry.setTag("equipLampTelemetry");
        equipLampTelemetry.setVisibility(View.GONE);


        QMUIGroupListView.newSection(getContext())
                .setTitle("基础信息")
                .addItemView(equipNo, null)
                //.addItemView(equipName, null)
                .addItemView(equipType, mOnClickListenerGroup)
                .addItemView(equipNfc, mOnClickListenerGroup)
                .addItemView(equipStore, mOnClickListenerGroup)
                .addItemView(equipManufacturer, mOnClickListenerGroup)
                .addItemView(equipMMode, null)
                .addItemView(equipMBrand, null)
                .addItemView(equipArrivalDate, null)
                //ais
                .addItemView(equipAisMMSIX, mOnClickListenerGroup)
                //radar
                .addItemView(equipRadarNO, mOnClickListenerGroup)
                .addItemView(equipRadarBand, mOnClickListenerGroup)
                //Telemetry
                .addItemView(equipTelemetryNO, null)
                .addItemView(equipTelemetrySIM, null)
                .addItemView(equipTelemetryMode, mOnClickListenerGroup)
                .addItemView(equipTelemetryVolt, null)
                .addItemView(equipTelemetryWatt, null)
                //Battery
                .addItemView(equipBatteryNO, null)
                .addItemView(equipBatteryType, mOnClickListenerGroup)
                .addItemView(equipBatteryVolt, null)
                .addItemView(equipBatteryWatt, null)
                .addItemView(equipBatteryConnect, null)
                //SolarEnergy
                .addItemView(equipSolarNO, null)
                .addItemView(equipSolarType, mOnClickListenerGroup)
                .addItemView(equipSolarVolt, null)
                .addItemView(equipSolarWatt, null)
                .addItemView(equipSolarConnect, null)
                //SpareLamp
                .addItemView(equipSLampWatt, null)
                //SolarEnergy
                .addItemView(equipVLampWatt, null)
                //Lamp
                .addItemView(equipLampNO, null)
                .addItemView(equipLampType, mOnClickListenerGroup)
                .addItemView(equipLampLens, mOnClickListenerGroup)
                .addItemView(equipLampInputVolt, null)
                .addItemView(equipLampWatt, null)
                .addItemView(equipLampTelemetryFlag, null)
                .addItemView(equipLampTelemetry, mOnClickListenerGroup)

                .addTo(mGroupListView);

    }

    private EditText newEditText(){
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_item_edit, null);
//        EditText editText = view.findViewById(R.id.item_edit_text);
        EditText editText = new EditText(getContext());
        editText.setId(R.id.item_edit_text);
        editText.setLayoutParams(new ViewGroup.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT));
        editText.setMaxWidth(QMUIDisplayHelper.dp2px(getContext(), 260));
        editText.setBackgroundDrawable(null);
        editText.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_5));
        editText.setTextSize(QMUIDisplayHelper.px2sp(getContext(), QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_common_list_item_detail_h_text_size) ));
        editText.setSingleLine();
        editText.setHint("--请输入--");
        return editText;
    }

    /**
     * 扫描二维码（先请求权限，用第三方库）
     */
    private void scanQrCode() {
        ArrayList<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.CAMERA);
        PermissionUtils.getInstances().requestPermission(getActivity(), permissionList, new IRequestPermissionCallBack() {
            @Override
            public void onGranted() {
                //扫描二维码/条形码
                //QrCodeUtils.startScan(getActivity(), Application.Scan_Add_Request_Code);
                Intent intent = QrCodeUtils.createScanQrCodeIntent(getActivity());
                startActivityForResult(intent, Application.Scan_Add_Request_Code);
            }

            @Override
            public void onDenied() {
                Toast.makeText(getActivity(), "请在应用管理中打开拍照权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case Application.Scan_Add_Request_Code:
                    String str = QrCodeUtils.getScanResult(data);
                    Log.d("onActivityResult", str);
                    //填入NO
                    EditText editTextNo = equipNo.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                    editTextNo.setText(str);
                    break;
                case Application.Nfc_Add_Request_Code:
                    String result = data.getStringExtra(NfcActivity.NFC_RESULT);
                    Log.d("onActivityResult", result);
                    //查询NFC
                    nfcFindAndAdd(result);
                    break;
            }
        }
    }

    /**
     * NFC扫描
     */
    private void scanNFC() {
        Intent intent = new Intent(getActivity(), NfcActivity.class);
        intent.putExtra(NfcActivity.NFC_TYPE, NfcActivity.NFC_TYPE_ADD);
        startActivityForResult(intent, Application.Nfc_Add_Request_Code);
    }

    private View.OnClickListener mOnClickListenerGroup;

    {
        mOnClickListenerGroup = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QMUICommonListItemView viewList = (QMUICommonListItemView) view;
                switch ((String) viewList.getTag()) {
                    case "equipType": //type
                        String[] typeNames = new String[equipTypeData.size()];
                        for (int i = 0; i < equipTypeData.size(); i++) {
                            typeNames[i] = equipTypeData.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(typeNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(typeNames[which]);
                                        Dict type = equipTypeData.get(which);
                                        equip.setsEquip_Type(type.getsDict_NO());
                                        dialog.dismiss();
                                        //隐藏
                                        //Ais
                                        equipAisMMSIX.setVisibility(View.GONE);
                                        //Radar
                                        equipRadarNO.setVisibility(View.GONE);
                                        equipRadarBand.setVisibility(View.GONE);
                                        //Telemetry
                                        equipTelemetryNO.setVisibility(View.GONE);
                                        equipTelemetrySIM.setVisibility(View.GONE);
                                        equipTelemetryMode.setVisibility(View.GONE);
                                        equipTelemetryVolt.setVisibility(View.GONE);
                                        equipTelemetryWatt.setVisibility(View.GONE);
                                        //Battery
                                        equipBatteryNO.setVisibility(View.GONE);
                                        equipBatteryType.setVisibility(View.GONE);
                                        equipBatteryVolt.setVisibility(View.GONE);
                                        equipBatteryWatt.setVisibility(View.GONE);
                                        equipBatteryConnect.setVisibility(View.GONE);
                                        //SolarEnergy
                                        equipSolarNO.setVisibility(View.GONE);
                                        equipSolarType.setVisibility(View.GONE);
                                        equipSolarVolt.setVisibility(View.GONE);
                                        equipSolarWatt.setVisibility(View.GONE);
                                        equipSolarConnect.setVisibility(View.GONE);
                                        //SpareLamp
                                        equipSLampWatt.setVisibility(View.GONE);
                                        //ViceLamp
                                        equipVLampWatt.setVisibility(View.GONE);
                                        //Lamp
                                        equipLampNO.setVisibility(View.GONE);
                                        equipLampType.setVisibility(View.GONE);
                                        equipLampLens.setVisibility(View.GONE);
                                        equipLampInputVolt.setVisibility(View.GONE);
                                        equipLampWatt.setVisibility(View.GONE);
                                        equipLampTelemetryFlag.setVisibility(View.GONE);
                                        //equipLampTelemetry.setVisibility(View.GONE);

                                        //显示
                                        switch (type.getsDict_NO()){
                                            case Constant.EquipType_AIS:
                                                DataUtils.getDictData(Constant.equipAisMMSIXDict, equipAisMMSIXOptions);
                                                equipAisMMSIX.setVisibility(View.VISIBLE);
                                                break;
                                            case Constant.EquipType_Radar:
                                                DataUtils.getDictData(Constant.equipRadarNODict, equipRadarNOOptions);
                                                DataUtils.getDictData(Constant.equipRadarBandDict, equipRadarBandOptions);
                                                equipRadarNO.setVisibility(View.VISIBLE);
                                                equipRadarBand.setVisibility(View.VISIBLE);
                                                break;
                                            case Constant.EquipType_Telemetry:
                                                DataUtils.getDictData(Constant.equipTelemetryModeDict, equipTelemetryModeOptions);
                                                equipTelemetryNO.setVisibility(View.VISIBLE);
                                                equipTelemetrySIM.setVisibility(View.VISIBLE);
                                                equipTelemetryMode.setVisibility(View.VISIBLE);
                                                equipTelemetryVolt.setVisibility(View.VISIBLE);
                                                equipTelemetryWatt.setVisibility(View.VISIBLE);
                                                break;
                                            case Constant.EquipType_Battery:
                                                DataUtils.getDictData(Constant.equipBatteryTypeDict, equipBatteryTypeOptions);
                                                equipBatteryNO.setVisibility(View.VISIBLE);
                                                equipBatteryType.setVisibility(View.VISIBLE);
                                                equipBatteryVolt.setVisibility(View.VISIBLE);
                                                equipBatteryWatt.setVisibility(View.VISIBLE);
                                                equipBatteryConnect.setVisibility(View.VISIBLE);
                                                break;
                                            case Constant.EquipType_SolarEnergy:
                                                DataUtils.getDictData(Constant.equipSolarTypeDict, equipSolarTypeOptions);
                                                equipSolarNO.setVisibility(View.VISIBLE);
                                                equipSolarType.setVisibility(View.VISIBLE);
                                                equipSolarVolt.setVisibility(View.VISIBLE);
                                                equipSolarWatt.setVisibility(View.VISIBLE);
                                                equipSolarConnect.setVisibility(View.VISIBLE);
                                                break;
                                            case Constant.EquipType_SpareLamp:
                                                equipSLampWatt.setVisibility(View.VISIBLE);
                                                break;
                                            case Constant.EquipType_ViceLamp:
                                                equipVLampWatt.setVisibility(View.VISIBLE);
                                                break;
                                            case Constant.EquipType_Lamp:
                                                DataUtils.getDictData(Constant.equipLampTypeDict, equipLampTypeOptions);
                                                DataUtils.getDictData(Constant.equipLampLensDict, equipLampLensOptions);
                                                DataUtils.getDictData(Constant.equipLampTelemetryDict, equipLampTelemetryOptions);
                                                equipLampNO.setVisibility(View.VISIBLE);
                                                equipLampType.setVisibility(View.VISIBLE);
                                                equipLampLens.setVisibility(View.VISIBLE);
                                                equipLampInputVolt.setVisibility(View.VISIBLE);
                                                equipLampWatt.setVisibility(View.VISIBLE);
                                                equipLampTelemetryFlag.setVisibility(View.VISIBLE);
                                                //equipLampTelemetry.setVisibility(View.VISIBLE);
                                                break;
                                        }
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "equipNfc": //nfc标签
                        String[] nfcNames = new String[nfcUnusedData.size()];
                        for (int i = 0; i < nfcUnusedData.size(); i++) {
                            nfcNames[i] = nfcUnusedData.get(i).getsNfc_Name();
                        }
                        new SearchDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .setHint("请输入标签名称")
                                .addItems(nfcNames, new SearchDialogBuilder.OnSelectedListiner() {
                                    @Override
                                    public void onSelected(DialogInterface dialog, SearchDialogBuilder.ItemEntity item) {
                                        String name = item.getName();
                                        int index = item.getIndex();
                                        viewList.getDetailTextView().setText(name);
                                        equip.setsEquip_NfcID(nfcUnusedData.get(index).getsNfc_ID());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "equipStore": //仓库
                        Selector selector = new Selector(getActivity(), 4);
                        selector.setDataProvider(new DataProvider() {
                            @Override
                            public void provideData(int currentDeep, String preId, DataReceiver receiver) {
                                //根据tab的深度和前一项选择的id，获取下一级菜单项
                                Log.i(TAG, "provideData: currentDeep >>> " + currentDeep + " preId >>> " + preId);
                                List<ISelectAble> data = new ArrayList<>();
                                if (currentDeep == 0) {
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
                                } else {
                                    for (int j = 0; j < storeData.size(); j++) {
                                        Store store = storeData.get(j);
                                        if (!preId.equals(store.getsStore_Parent())) {
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
                                    if (selectAble == null) {
                                        continue;
                                    }
                                    switch (i) {
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
                                Log.i(TAG, "result : " + result);
                                equipStore.setDetailText(result);
                                //viewList.getDetailTextView().setText(result);
                                selectorDialog.dismiss();
                            }
                        });
                        selectorDialog = new BottomDialog(getActivity());
                        selectorDialog.init(getActivity(), selector);
                        selectorDialog.show();
                        break;
                    case "equipManufacturer": //厂家
                        String[] mNames = new String[equipManufacturerData.size()];
                        for (int i = 0; i < equipManufacturerData.size(); i++) {
                            mNames[i] = equipManufacturerData.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(mNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(mNames[which]);
                                        equip.setsEquip_Manufacturer(equipManufacturerData.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "equipAisMMSIX": //mmsix
                        String[] amNames = new String[equipAisMMSIXOptions.size()];
                        for (int i = 0; i < equipAisMMSIXOptions.size(); i++) {
                            amNames[i] = equipAisMMSIXOptions.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(amNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(amNames[which]);
                                        equipInfo.put("sAis_MMSIX", equipAisMMSIXOptions.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "equipRadarNO": //Radar NO
                        String[] rnNames = new String[equipRadarNOOptions.size()];
                        for (int i = 0; i < equipRadarNOOptions.size(); i++) {
                            rnNames[i] = equipRadarNOOptions.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(rnNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(rnNames[which]);
                                        equipInfo.put("sRadar_NO", equipRadarNOOptions.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "equipRadarBand":
                        String[] rbNames = new String[equipRadarBandOptions.size()];
                        for (int i = 0; i < equipRadarBandOptions.size(); i++) {
                            rbNames[i] = equipRadarBandOptions.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(rbNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(rbNames[which]);
                                        equipInfo.put("sRadar_Band", equipRadarBandOptions.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "equipTelemetryMode":
                        String[] tmNames = new String[equipTelemetryModeOptions.size()];
                        for (int i = 0; i < equipTelemetryModeOptions.size(); i++) {
                            tmNames[i] = equipTelemetryModeOptions.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(tmNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(tmNames[which]);
                                        equipInfo.put("sTelemetry_Mode", equipTelemetryModeOptions.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "equipBatteryType":
                        String[] btNames = new String[equipBatteryTypeOptions.size()];
                        for (int i = 0; i < equipBatteryTypeOptions.size(); i++) {
                            btNames[i] = equipBatteryTypeOptions.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(btNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(btNames[which]);
                                        equipInfo.put("sBattery_Type", equipBatteryTypeOptions.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "equipSolarType":
                        String[] stNames = new String[equipSolarTypeOptions.size()];
                        for (int i = 0; i < equipSolarTypeOptions.size(); i++) {
                            stNames[i] = equipSolarTypeOptions.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(stNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(stNames[which]);
                                        equipInfo.put("sSolar_Type", equipSolarTypeOptions.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "equipLampType":
                        String[] ltNames = new String[equipLampTypeOptions.size()];
                        for (int i = 0; i < equipLampTypeOptions.size(); i++) {
                            ltNames[i] = equipLampTypeOptions.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(ltNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(ltNames[which]);
                                        equipInfo.put("sLamp_Type", equipLampTypeOptions.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "equipLampLens":
                        String[] llNames = new String[equipLampLensOptions.size()];
                        for (int i = 0; i < equipLampLensOptions.size(); i++) {
                            llNames[i] = equipLampLensOptions.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(llNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(llNames[which]);
                                        equipInfo.put("sLamp_Lens", equipLampLensOptions.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "equipLampTelemetry":
                        String[] ltlNames = new String[equipLampTelemetryOptions.size()];
                        for (int i = 0; i < equipLampTelemetryOptions.size(); i++) {
                            ltlNames[i] = equipLampTelemetryOptions.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(ltlNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(ltlNames[which]);
                                        equipInfo.put("sLamp_Telemetry", equipLampTelemetryOptions.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                }
            }
        };
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //处理结果
            hideTips();
            String str =  (String) msg.obj;
            if(Utils.isNullOrEmpty(str)){
                showToast("网络异常，请检查网络。");
                return;
            }
            JSONObject resData = JSONObject.parseObject(str);
            //处理数据
            switch (msg.what){
                case MsgType_Add:
                    handleAdd(resData);
                    break;
                case MsgType_Nfc_Add:
                    handleNFC(resData);
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
        //tipDialog.show();
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
        DataUtils.getNfcUnusedData(nfcUnusedData);

        initEquipInfo();

    }

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

    private void sendAdd(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = JSONObject.parseObject(JSONObject.toJSONString(equip), new TypeReference<Map<String, Object>>(){});
                params.putAll(equipInfo);
                //base
                EditText sEquip_MModel = equipMMode.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("sEquip_MModel", sEquip_MModel.getText());
                EditText sEquip_MBrand = equipMBrand.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("sEquip_MBrand", sEquip_MBrand.getText());
                //Ais
                //Radar
                //Telemetry
                EditText sTelemetry_NO = equipTelemetryNO.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("sTelemetry_NO", sTelemetry_NO.getText());
                EditText sTelemetry_SIM = equipTelemetrySIM.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("sTelemetry_SIM", sTelemetry_SIM.getText());
                EditText lTelemetry_Volt = equipTelemetryVolt.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("lTelemetry_Volt", lTelemetry_Volt.getText());
                EditText lTelemetry_Watt = equipTelemetryWatt.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("lTelemetry_Watt", lTelemetry_Watt.getText());
                //Battery
                EditText sBattery_NO = equipBatteryNO.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("sBattery_NO", sBattery_NO.getText());
                EditText lBattery_Volt = equipBatteryVolt.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("lBattery_Volt", lBattery_Volt.getText());
                EditText lBattery_Watt = equipBatteryWatt.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("lBattery_Watt", lBattery_Watt.getText());
                EditText sBattery_Connect = equipBatteryConnect.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("sBattery_Connect", sBattery_Connect.getText());
                //SolarEnergy
                EditText sSolar_NO = equipSolarNO.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("sSolar_NO", sSolar_NO.getText());
                EditText lSolar_Volt = equipSolarVolt.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("lSolar_Volt", lSolar_Volt.getText());
                EditText lSolar_Watt = equipSolarWatt.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("lSolar_Watt", lSolar_Watt.getText());
                EditText sSolar_Connect = equipSolarConnect.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("sSolar_Connect", sSolar_Connect.getText());
                //SpareLamp
                EditText lSLamp_Watt = equipSLampWatt.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("lSLamp_Watt", lSLamp_Watt.getText());
                //ViceLamp
                EditText lVLamp_Watt = equipVLampWatt.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("lVLamp_Watt", lVLamp_Watt.getText());
                //Lamp
                EditText sLamp_NO = equipLampNO.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("sLamp_NO", sLamp_NO.getText());
                EditText lLamp_InputVolt = equipLampInputVolt.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("lLamp_InputVolt", lLamp_InputVolt.getText());
                EditText lLamp_Watt = equipLampWatt.getAccessoryContainerView().findViewById(R.id.item_edit_text);
                params.put("lLamp_Watt", lLamp_Watt.getText());

                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipAddUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Add);
                message.obj = res;
                mHandler.sendMessage(message);
            }
        });
    }
    private void handleAdd(JSONObject resObj){
        if(resObj.getInteger("code") <= 0){
            showToast(resObj.getString("msg"));
            return;
        }
        showToast("保存成功");
        popBackStack();
    }

    private void nfcFindAndAdd(String sNfc_NO){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("sNfc_NO", sNfc_NO);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.nfcFindAndAddUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_Nfc_Add;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void handleNFC(JSONObject resObj){
        JSONObject resData = resObj.getJSONObject("data");
        if(resData == null){
            Toast.makeText(getActivity(), "NFC标签已被使用，请重新选择。", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, resObj.getString("data"));
        //填入NO
        equipNfc.setDetailText(resData.getString("sNfc_Name"));
        equip.setsEquip_NfcID(resData.getString("sNfc_ID"));
    }
}
