
package com.jian.system.fragment.components;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jian.system.Application;
import com.jian.system.R;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Equip;
import com.jian.system.entity.Messages;
import com.jian.system.entity.Nfc;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;
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
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
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


public class MsgAddFragment extends QMUIFragment {

    private final static String TAG = MsgAddFragment.class.getSimpleName();
    private String title = "新增任务";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    private QMUITipDialog tipDialog;
    private Messages msg = new Messages();
    private List<Dict> msgTypeData = new ArrayList<>();
    private List<Dict> msgStatusData = new ArrayList<>();
    private List<Dict> msgLabelData = new ArrayList<>();
    private List<Dict> msgReasonData = new ArrayList<>();
    private List<JSONObject> aidAllData = new ArrayList<>();
    private List<JSONObject> userAidData = new ArrayList<>();
    private List<StoreType> storeTypeData = new ArrayList<>();
    private List<Store> storeData = new ArrayList<>();

    private QMUICommonListItemView msgAid;
    private QMUICommonListItemView msgLabel;
    private QMUICommonListItemView msgReason;

    private final int MsgType_Add = 0;

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_msg_add, null);
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
                                sendAdd();
                            }
                        })
                        .create(mCurrentDialogStyle).show();
            }
        });
        mTopBar.setTitle(title);
    }

    @SuppressLint("NewApi")
    private void initEquipInfo() {

        msgAid = mGroupListView.createItemView("航标");
        msgAid.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        msgAid.setDetailText("--请选择--");
        msgAid.setTag("msgAid");

        msgLabel = mGroupListView.createItemView("事件类型");
        msgLabel.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        msgLabel.setDetailText("--请选择--");
        msgLabel.setTag("msgLabel");

        msgReason = mGroupListView.createItemView("原因");
        msgReason.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        msgReason.setDetailText("--请选择--");
        msgReason.setTag("msgReason");



        QMUIGroupListView.newSection(getContext())
                .setTitle(" ")
                .addItemView(msgAid, mOnClickListenerGroup)
                .addItemView(msgLabel, mOnClickListenerGroup)
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
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "msgLabel":
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
        DataUtils.getDictData(Constant.msgLabelDict, msgLabelData);
        //查询消息原因
        DataUtils.getDictData(Constant.msgReasonDict, msgReasonData);
        //查询用户航标
        DataUtils.getAidUserData(userAidData);
        //查询仓库
        DataUtils.getStoreTypeData(storeTypeData);
        DataUtils.getStoreData(storeData);

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

}
