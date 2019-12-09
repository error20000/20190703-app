
package com.jian.system.fragment.components;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jian.system.R;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Equip;
import com.jian.system.entity.Messages;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.jian.system.view.SearchDialogBuilder;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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


public class MsgAddFragment extends QMUIFragment {

    private final static String TAG = MsgAddFragment.class.getSimpleName();
    private String title = "新增消息";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    private QMUITipDialog tipDialog;
    private BottomDialog selectorDialog;
    private Messages msg = new Messages();
    private List<Dict> msgTypeData = new ArrayList<>();
    private List<Dict> msgStatusData = new ArrayList<>();
    private List<Dict> msgLabelData = new ArrayList<>();
    private List<Dict> msgReasonData = new ArrayList<>();
    private List<Dict> msgSourceData = new ArrayList<>();
    private List<Dict> equipTypeData = new ArrayList<>();
    private List<JSONObject> aidAllData = new ArrayList<>();
    private List<JSONObject> userAidData = new ArrayList<>();
    private List<StoreType> storeTypeData = new ArrayList<>();
    private List<Store> storeData = new ArrayList<>();
    private List<Equip> equipData = new ArrayList<>();

    private QMUICommonListItemView msgTypeFlag;
    private QMUICommonListItemView msgAid;
    private QMUICommonListItemView msgStore;
    private QMUICommonListItemView msgEquip;
    private QMUICommonListItemView msgLabel;
    private QMUICommonListItemView msgType;
    private QMUICommonListItemView msgReason;
    private QMUICommonListItemView msgSource;

    private final int MsgType_Add = 0;
    private final int MsgType_Aid_Equip = 1;
    private final int MsgType_Store_Equip = 2;
    public static final String Fragment_Result = "Fragment_Result";

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;
    @BindView(R.id.describe)
    EditText mEditText;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_msg_add, null);
        ButterKnife.bind(this, rootView);

        initTopBar();
        initData();

        return rootView;
    }

    private void handleResult(){
        Intent intent = new Intent();
        intent.putExtra(Fragment_Result, "add");
        setFragmentResult(RESULT_OK, intent);
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

                new QMUIDialog.MessageDialogBuilder(getActivity())
                        .setTitle("")
                        .setMessage("确定要保存吗？")
                        .addAction(0,"取消", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                //保存数据
                                msg.setsMsg_Describe(mEditText.getText().toString());
                                sendAdd();
                                //刷新数据
                                handleResult();
                            }
                        })
                        .create(mCurrentDialogStyle).show();
            }
        });
        mTopBar.setTitle(title);
    }

    @SuppressLint("NewApi")
    private void initEquipInfo() {

        msgTypeFlag = mGroupListView.createItemView("航标/仓库");
        msgTypeFlag.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        msgTypeFlag.setDetailText("--请选择--");
        msgTypeFlag.setTag("msgTypeFlag");

        msgAid = mGroupListView.createItemView("航标");
        msgAid.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        msgAid.setDetailText("--请选择--");
        msgAid.setTag("msgAid");
        msgAid.setVisibility(View.GONE);

        msgStore = mGroupListView.createItemView("仓库");
        msgStore.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        msgStore.setDetailText("--请选择--");
        msgStore.setTag("msgStore");
        msgStore.setVisibility(View.GONE);

        msgEquip = mGroupListView.createItemView("器材");
        msgEquip.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        msgEquip.setDetailText("--请选择--");
        msgEquip.setTag("msgEquip");
        msgEquip.setVisibility(View.GONE);

        /*msgLabel = mGroupListView.createItemView("消息类型");
        msgLabel.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        msgLabel.setDetailText("--请选择--");
        msgLabel.setTag("msgLabel");*/
        msgType = mGroupListView.createItemView("消息类型");
        msgType.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        msgType.setDetailText("--请选择--");
        msgType.setTag("msgType");

        msgSource = mGroupListView.createItemView("消息来源");
        msgSource.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        msgSource.setDetailText("--请选择--");
        msgSource.setTag("msgSource");

        msgReason = mGroupListView.createItemView("原因");
        msgReason.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        msgReason.setDetailText("--请选择--");
        msgReason.setTag("msgReason");

        QMUIGroupListView.newSection(getContext())
                .setTitle(" ")
                .addItemView(msgTypeFlag, mOnClickListenerGroup)
                .addItemView(msgAid, mOnClickListenerGroup)
                .addItemView(msgStore, mOnClickListenerGroup)
                .addItemView(msgEquip, mOnClickListenerGroup)
                //.addItemView(msgLabel, mOnClickListenerGroup)
                .addItemView(msgType, mOnClickListenerGroup)
                .addItemView(msgSource, mOnClickListenerGroup)
                .addItemView(msgReason, mOnClickListenerGroup)

                .addTo(mGroupListView);

    }


    private View.OnClickListener mOnClickListenerGroup;

    {
        mOnClickListenerGroup = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QMUICommonListItemView viewList = (QMUICommonListItemView) view;
                switch ((String) viewList.getTag()) {
                    case "msgTypeFlag":
                        String[] typeFlagNames = {"航标", "仓库"};
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(typeFlagNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String flag = typeFlagNames[which];
                                        viewList.getDetailTextView().setText(flag);
                                        dialog.dismiss();
                                        //隐藏
                                        msgAid.setVisibility(View.GONE);
                                        msgStore.setVisibility(View.GONE);
                                        msgEquip.setVisibility(View.GONE);
                                        //清空数据
                                        msgAid.setDetailText("--请选择--");
                                        msgStore.setDetailText("--请选择--");
                                        msgEquip.setDetailText("--请选择--");
                                        msg.setsMsg_AidID("");
                                        msg.setsMsg_EquipID("");
                                        msg.setsMsg_StoreLv1("");
                                        msg.setsMsg_StoreLv2("");
                                        msg.setsMsg_StoreLv3("");
                                        msg.setsMsg_StoreLv4("");
                                        //显示
                                        switch (flag){
                                            case "航标":
                                                //查询用户航标
                                                DataUtils.getAidUserData(userAidData);
                                                msgAid.setVisibility(View.VISIBLE);
                                                msgEquip.setVisibility(View.VISIBLE);
                                                break;
                                            case "仓库":
                                                //查询仓库
                                                DataUtils.getStoreTypeData(storeTypeData);
                                                DataUtils.getStoreData(storeData);
                                                msgStore.setVisibility(View.VISIBLE);
                                                msgEquip.setVisibility(View.VISIBLE);
                                                break;
                                        }
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "msgAid":
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
                                        viewList.getDetailTextView().setText(item.getName());
                                        msg.setsMsg_AidID(userAidData.get(item.getIndex()).getString("sAid_ID"));
                                        dialog.dismiss();
                                        //查询航标器材
                                        queryAidEquip(msg.getsMsg_AidID());
                                        //清空数据
                                        msgEquip.setDetailText("--请选择--");
                                        msg.setsMsg_EquipID("");
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "msgStore":
                        if(storeTypeData.size() == 0){
                            showToast("未查询到仓库数据");
                            return;
                        }
                        Selector selector = new Selector(getActivity(), 4);
                        selector.setDataProvider(new DataProvider() {
                            @Override
                            public void provideData(int currentDeep, String preId, DataReceiver receiver) {
                                //根据tab的深度和前一项选择的id，获取下一级菜单项
                                Log.d(TAG, "provideData: currentDeep >>> " + currentDeep + " preId >>> " + preId);
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
                                String storeLv1 = "";
                                String storeLv2 = "";
                                String storeLv3 = "";
                                String storeLv4 = "";
                                for (int i = 0; i < selectAbles.size(); i++) {
                                    ISelectAble selectAble = selectAbles.get(i);
                                    if (selectAble == null) {
                                        continue;
                                    }
                                    switch (i) {
                                        case 0:
                                            storeLv1 = selectAble.getId();
                                            msg.setsMsg_StoreLv1(storeLv1);
                                            break;
                                        case 1:
                                            storeLv2 = selectAble.getId();
                                            msg.setsMsg_StoreLv2(storeLv2);
                                            break;
                                        case 2:
                                            storeLv3 = selectAble.getId();
                                            msg.setsMsg_StoreLv3(storeLv3);
                                            break;
                                        case 3:
                                            storeLv4 = selectAble.getId();
                                            msg.setsMsg_StoreLv4(storeLv4);
                                            break;
                                    }
                                    result += " / " + selectAble.getName();
                                }
                                result = "".equals(result) ? result : result.substring(" / ".length());
                                Log.d(TAG, "result : " + result);
                                msgStore.setDetailText(result);
                                //viewList.getDetailTextView().setText(result);
                                selectorDialog.dismiss();
                                //查询仓库器材
                                queryStoreEquip(storeLv1, storeLv2, storeLv3, storeLv4);
                                //清空数据
                                msgEquip.setDetailText("--请选择--");
                                msg.setsMsg_EquipID("");
                            }
                        });
                        selectorDialog = new BottomDialog(getActivity());
                        selectorDialog.init(getActivity(), selector);
                        selectorDialog.show();
                        break;
                    case "msgEquip":
                        /*String[] equips = new String[equipData.size()];
                        for (int i = 0; i < equipData.size(); i++) {
                            equips[i] = equipData.get(i).getsEquip_NO();
                        }*/
                        List<SearchDialogBuilder.ItemEntity> equips = new ArrayList<>();
                        for (int i = 0; i < equipData.size(); i++) {
                            Equip node = equipData.get(i);
                            String typeName = FormatUtils.formatDict(node.getsEquip_Type(), equipTypeData);
                            equips.add(new SearchDialogBuilder.ItemEntity(node.getsEquip_NO(), i, typeName) );
                        }
                        new SearchDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .setHint("请输入器材编号")
                                .addItems(equips, new SearchDialogBuilder.OnSelectedListiner() {
                                    @Override
                                    public void onSelected(DialogInterface dialog, SearchDialogBuilder.ItemEntity item) {
                                        viewList.getDetailTextView().setText(item.getName());
                                        msg.setsMsg_EquipID(equipData.get(item.getIndex()).getsEquip_ID());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    /*case "msgLabel":
                        String[] lNames = new String[msgLabelData.size()];
                        for (int i = 0; i < msgLabelData.size(); i++) {
                            lNames[i] = msgLabelData.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(lNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(lNames[which]);
                                        msg.setsMsg_Label(msgLabelData.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;*/
                    case "msgType":
                        String[] tNames = new String[msgTypeData.size()];
                        for (int i = 0; i < msgTypeData.size(); i++) {
                            tNames[i] = msgTypeData.get(i).getsDict_Name();
                        }
                        Arrays.sort(tNames);
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(tNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(tNames[which]);
                                        Dict msgTypeDict = msgTypeData.get(which);
                                        msg.setsMsg_Type(msgTypeDict.getsDict_NO());
                                        msg.setsMsg_Title(msgTypeDict.getsDict_Name());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "msgSource":
                        String[] sNames = new String[msgSourceData.size()];
                        for (int i = 0; i < msgSourceData.size(); i++) {
                            sNames[i] = msgSourceData.get(i).getsDict_Name();
                        }
                        Arrays.sort(sNames);
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(sNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(sNames[which]);
                                        msg.setsMsg_Source(msgSourceData.get(which).getsDict_NO());
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "msgReason":
                        String[] rNames = new String[msgReasonData.size()];
                        for (int i = 0; i < msgReasonData.size(); i++) {
                            rNames[i] = msgReasonData.get(i).getsDict_Name();
                        }
                        new QMUIDialog.CheckableDialogBuilder(getActivity())
                                .setTitle("请选择")
                                .addItems(rNames, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewList.getDetailTextView().setText(rNames[which]);
                                        msg.setsMsg_Reason(msgReasonData.get(which).getsDict_NO());
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
                case MsgType_Aid_Equip:
                    handleAidEquip(resData);
                    break;
                case MsgType_Store_Equip:
                    handleStoreEquip(resData);
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
        //查询消息自定义类型
        //DataUtils.getDictData(Constant.msgLabelDict, msgLabelData);
        //查询消息类型
        DataUtils.getDictData(Constant.msgTypeDict, msgTypeData);
        //查询消息原因
        DataUtils.getDictData(Constant.msgReasonDict, msgReasonData);
        //查询用户航标
        //DataUtils.getAidUserData(userAidData);
        //查询仓库
        //DataUtils.getStoreTypeData(storeTypeData);
        //DataUtils.getStoreData(storeData);
        //查询器材种类
        DataUtils.getDictData(Constant.equipTypeDict, equipTypeData);
        //查询消息来源
        DataUtils.getDictData(Constant.msgSourceDict, msgSourceData);

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
                Map<String, Object> params = JSONObject.parseObject(JSONObject.toJSONString(msg), new TypeReference<Map<String, Object>>(){});
                String res = HttpUtils.getInstance().sendPost(UrlConfig.msgAddUrl, params);
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

    private void queryAidEquip(String sAid_ID){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("sAid_ID", sAid_ID);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.aidQueryEquipUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Aid_Equip);
                message.obj = res;
                mHandler.sendMessage(message);
            }
        });
    }

    private void handleAidEquip(JSONObject resObj){
        if(resObj.getInteger("code") <= 0){
            showToast(resObj.getString("msg"));
            return;
        }
        equipData.clear();
        JSONArray data = resObj.getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            equipData.add(data.getObject(i, Equip.class));
        }
    }

    private void queryStoreEquip(String sEquip_StoreLv1, String sEquip_StoreLv2, String sEquip_StoreLv3, String sEquip_StoreLv4){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("sEquip_StoreLv1", sEquip_StoreLv1);
                params.put("sEquip_StoreLv2", sEquip_StoreLv2);
                params.put("sEquip_StoreLv3", sEquip_StoreLv3);
                params.put("sEquip_StoreLv4", sEquip_StoreLv4);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.storeQueryEquipUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Store_Equip);
                message.obj = res;
                mHandler.sendMessage(message);
            }
        });
    }

    private void handleStoreEquip(JSONObject resObj){
        if(resObj.getInteger("code") <= 0){
            showToast(resObj.getString("msg"));
            return;
        }
        equipData.clear();
        JSONArray data = resObj.getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            equipData.add(data.getObject(i, Equip.class));
        }
    }
}
