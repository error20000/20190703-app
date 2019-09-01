
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
    private List<Dict> equipTypeData = new ArrayList<>();
    private List<Dict> equipStatusData = new ArrayList<>();
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
                EditText editTextName = (EditText)equipName.getAccessoryContainerView().getChildAt(0);
                equip.setsEquip_Name(editTextName.getText().toString());
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
        equipNo.setTag(1);
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
        equipName.setTag(2);

        equipType = mGroupListView.createItemView("器材类型");
        equipType.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipType.setDetailText("--请选择类型--");
        equipType.setImageDrawable(getResources().getDrawable(R.mipmap.ic_required));
        equipType.setTag(3);

        equipNfc = mGroupListView.createItemView("NFC标签ID");
        equipNfc.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipNfc.setDetailText("--请选择NFC标签--");
        ImageView equipNfcImage = new ImageView(getActivity());
        Drawable equipNfcDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_nfc_24dp);
        equipNfcDrawable.setTint(ContextCompat.getColor(getActivity(), R.color.app_color_blue));
        equipNfcImage.setImageDrawable(equipNfcDrawable);
        equipNfc.addAccessoryCustomView(equipNfcImage);
        equipNfc.setTag(4);
        equipNfcImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanNFC();
            }
        });

        equipStore = mGroupListView.createItemView("仓库");
        equipStore.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipStore.setDetailText("--请选择仓库--");
        equipStore.setTag(5);

        equipManufacturer = mGroupListView.createItemView("生产厂家");
        equipManufacturer.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipManufacturer.setDetailText("--请选择--");
        equipManufacturer.setImageDrawable(getResources().getDrawable(R.mipmap.ic_required));
        equipManufacturer.setTag(6);

        equipMMode = mGroupListView.createItemView("厂方型号");
        equipMMode.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipMMode.addAccessoryCustomView(newEditText());
        equipMMode.setImageDrawable(getResources().getDrawable(R.mipmap.ic_required));
        equipMMode.setTag(7);

        equipMBrand = mGroupListView.createItemView("品牌");
        equipMBrand.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        equipMBrand.addAccessoryCustomView(newEditText());
        equipMBrand.setImageDrawable(getResources().getDrawable(R.mipmap.ic_required));
        equipMBrand.setTag(8);

        equipArrivalDate = mGroupListView.createItemView("到货日期");
        equipArrivalDate.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        equipArrivalDate.setDetailText("--请选择--");
        equipArrivalDate.setTag(9);
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

        QMUIGroupListView.newSection(getContext())
                .setTitle("基础信息")
                .addItemView(equipNo, null)
                .addItemView(equipName, null)
                .addItemView(equipType, mOnClickListenerGroup)
                .addItemView(equipNfc, mOnClickListenerGroup)
                .addItemView(equipStore, mOnClickListenerGroup)
                .addItemView(equipManufacturer, mOnClickListenerGroup)
                .addItemView(equipMMode, null)
                .addItemView(equipMBrand, null)
                .addItemView(equipArrivalDate, null)
                .addTo(mGroupListView);

    }

    private EditText newEditText(){
        EditText editText = new EditText(getContext());
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

    private View.OnClickListener mOnClickListenerGroup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            QMUICommonListItemView viewList = (QMUICommonListItemView) view;
            Log.d(TAG, "选项：" + viewList.getText().toString() + " 点击了");
            switch ((int)viewList.getTag()) {
                case 3: //type
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
                                    equip.setsEquip_Type(equipTypeData.get(which).getsDict_NO());
                                    dialog.dismiss();
                                    //
                                    Log.e(TAG, mGroupListView.getSection(0).toString());
                                    //mGroupListView.getSection(0).setTitle("dddddddddd");
                                    //mGroupListView.getSection(0).addItemView(equipManufacturer, null);
                                    /*mGroupListView.getSection(0)
                                            .addItemView(equipManufacturer, null)
                                            .addTo(mGroupListView);*/

                                    equipStore.setVisibility(View.VISIBLE);
                                }
                            })
                            .create(mCurrentDialogStyle).show();
                    break;
                case 4: //nfc标签
                    String[] nfcNames = new String[nfcUnusedData.size()];
                    for (int i = 0; i < nfcUnusedData.size(); i++) {
                        nfcNames[i] = nfcUnusedData.get(i).getsNfc_Name();
                    }
                    new QMUIDialog.CheckableDialogBuilder(getActivity())
                            .setTitle("请选择")
                            .addItems(nfcNames, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    viewList.getDetailTextView().setText(nfcNames[which]);
                                    equip.setsEquip_NfcID(nfcUnusedData.get(which).getsNfc_ID());
                                    dialog.dismiss();
                                }
                            })
                            .create(mCurrentDialogStyle).show();
                    break;
                case 5: //仓库
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
                            Log.i(TAG, "result : "+ result);
                            equipStore.setDetailText(result);
                            //viewList.getDetailTextView().setText(result);
                            selectorDialog.dismiss();
                        }
                    });
                    selectorDialog = new BottomDialog(getActivity());
                    selectorDialog.init(getActivity(), selector);
                    selectorDialog.show();
                    break;
            }
        }
    };

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
