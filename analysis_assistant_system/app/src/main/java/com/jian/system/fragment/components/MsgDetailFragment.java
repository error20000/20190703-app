
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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.jian.system.entity.Messages;
import com.jian.system.entity.Nfc;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListSectionHeaderFooterView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
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

public class MsgDetailFragment extends QMUIFragment {

    private final static String TAG = MsgDetailFragment.class.getSimpleName();
    private String title = "消息详情";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private QMUITipDialog tipDialog;
    private BottomDialog selectorDialog;
    private final int MsgType_Detail = 1;
    private final int MsgType_Handle = 2;
    private final int MsgType_UnHandle = 3;


    private String remarks = "";
    private Messages msg;
    private List<Dict> msgTypeData = new ArrayList<>();
    private List<Dict> msgStatusData = new ArrayList<>();
    private List<Dict> msgLabelData = new ArrayList<>();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_msg_detail, null);
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
                .addItem("设置为已处理", "handle")
                .addItem("标记为待处理", "unhandle")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        showEditTextDialog(tag);
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
                            case "handle":
                                itemHandle();
                                break;
                            case "unhandle":
                                itemUnHandle();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void initInfo() {

        if(msg == null){
            return;
        }

        QMUICommonListItemView msgId = mGroupListView.createItemView("ID");
        msgId.setDetailText(msg.getsMsg_ID());

        QMUICommonListItemView msgTitle = mGroupListView.createItemView("消息标题");
        msgTitle.setDetailText(msg.getsMsg_Title());

        QMUICommonListItemView msgDate = mGroupListView.createItemView("消息日期");
        String date = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", msg.getdMsg_CreateDate());
        msgDate.setDetailText(date);

        QMUICommonListItemView msgType = mGroupListView.createItemView("消息类型");
        String typeName = FormatUtils.formatDict(msg.getsMsg_Type(), msgTypeData);
        msgType.setDetailText(typeName);

        QMUICommonListItemView msgDescribe = mGroupListView.createItemView("消息描述");
        msgDescribe.setDetailText(msg.getsMsg_Describe());

        QMUICommonListItemView msgLevel = mGroupListView.createItemView("消息级别");
        msgLevel.setDetailText(msg.getlMsg_Level() == Integer.MAX_VALUE ? "默认" : msg.getlMsg_Level()+"");

        QMUICommonListItemView msgStatus = mGroupListView.createItemView("消息状态");
        String statusName = FormatUtils.formatDict(msg.getsMsg_Status(), msgStatusData);
        msgStatus.setDetailText(statusName);

        QMUICommonListItemView msgLabel = mGroupListView.createItemView("自定义标签");
        String labelName = FormatUtils.formatDict(msg.getsMsg_Label(), msgLabelData);
        msgLabel.setDetailText(labelName);

        QMUICommonListItemView msgAid = mGroupListView.createItemView("航标");
        String aidName = Utils.isNullOrEmpty(msg.getsMsg_AidName()) ? msg.getsMsg_AidID() : msg.getsMsg_AidName();
        msgAid.setDetailText(aidName);

        QMUICommonListItemView msgEquip = mGroupListView.createItemView("器材");
        String equipName = Utils.isNullOrEmpty(msg.getsMsg_EquipName()) ? msg.getsMsg_EquipID() : msg.getsMsg_EquipName();
        msgEquip.setDetailText(equipName);

        QMUICommonListItemView msgToUser = mGroupListView.createItemView("消息接收人员");
        msgToUser.setDetailText(msg.getsMsg_ToUserName());

        QMUICommonListItemView msgFromUser = mGroupListView.createItemView("消息产生人员");
        msgFromUser.setDetailText(msg.getsMsg_FromUserName());

        QMUICommonListItemView msgUpdateDate = mGroupListView.createItemView("更新日期");
        String update = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", msg.getdMsg_UpdateDate());
        msgUpdateDate.setDetailText(update);

        QMUICommonListItemView msgUser = mGroupListView.createItemView("更新人员");
        msgUser.setDetailText(msg.getsMsg_UserName());

        QMUICommonListItemView msgIP = mGroupListView.createItemView("更新人员IP");
        msgIP.setDetailText(msg.getsMsg_IP());

        QMUICommonListItemView msgRemarks = mGroupListView.createItemView("消息备注");
        msgRemarks.setDetailText(msg.getsMsg_Remarks());

        QMUIGroupListView.newSection(getContext())
                .setTitle("")
                .addItemView(msgId, null)
                .addItemView(msgTitle, null)
                .addItemView(msgDate, null)
                .addItemView(msgType, null)
                .addItemView(msgDescribe, null)
                .addItemView(msgLevel, null)
                .addItemView(msgStatus, null)
                .addItemView(msgLabel, null)
                .addItemView(msgAid, null)
                .addItemView(msgEquip, null)
                .addItemView(msgToUser, null)
                .addItemView(msgFromUser, null)
                .addItemView(msgUpdateDate, null)
                .addItemView(msgUser, null)
                .addItemView(msgIP, null)
                .addItemView(msgRemarks, null)
                .addTo(mGroupListView);

    }


    private void initData(){
        //查询数据 -- 判断网络

        tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        //查询种类
        DataUtils.getDictData(Constant.msgTypeDict, msgTypeData);
        //查询状态
        DataUtils.getDictData(Constant.msgStatusDict, msgStatusData);
        //查询标签
        DataUtils.getDictData(Constant.msgLabelDict, msgLabelData);
        //查询详情
        Bundle bundle = this.getArguments();

        String sMsg_ID = bundle.getString("sMsg_ID");
        Map<String, Object> params = new HashMap<>();
        params.put("sMsg_ID", sMsg_ID);
        queryDetail(params);
    }

    private void queryDetail(Map<String, Object> params){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.msgQueryDetailUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_Detail;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void readMsg(Map<String, Object> params){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.msgReadUrl, params);
            }
        });
    }


    //TODO --------------------------------------------------------------------------- handle

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
            switch (msg.what) {
                case MsgType_Detail:
                    handleDetail(resData);
                    break;
                case MsgType_Handle:
                    handleHandle(resData);
                    break;
                case MsgType_UnHandle:
                    handleUnHandle(resData);
                    break;
                default:
                    break;
            }
        }
    };

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


    private void handleDetail(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        msg = resObj.getObject("data", Messages.class);
        //展示数据
        initInfo();
        if("1".equals(msg.getsMsg_Status())){
            Map<String, Object> params = new HashMap<>();
            params.put("sMsg_ID", msg.getsMsg_ID());
            readMsg(params);
        }
    }

    private void itemHandle(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sMsg_ID", msg.getsMsg_ID());
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.msgHandleUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Handle);
                message.obj = res;
                mHandler.sendMessage(message);
            }
        });
    }

    private void handleHandle(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");

    }

    private void itemUnHandle(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sMsg_ID", msg.getsMsg_ID());
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.msgUnHandleUrl, params);
                Message message = mHandler.obtainMessage(MsgType_UnHandle);
                message.obj = res;
                mHandler.sendMessage(message);
            }
        });
    }

    private void handleUnHandle(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");
    }

}
