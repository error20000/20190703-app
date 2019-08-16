
package com.jian.system.fragment.components;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.R;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Aid;
import com.jian.system.entity.AidEquip;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Nfc;
import com.jian.system.fragment.ViewPagerListener;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chihane.jdaddressselector.BottomDialog;

public class AidDetailView extends QMUIWindowInsetLayout{

    private final static String TAG = AidDetailView.class.getSimpleName();

    private ViewPagerListener mListener;
    private Context context;
    private QMUITipDialog tipDialog;
    private BottomDialog selectorDialog;
    private final int MsgType_Detail = 1;
    private final int MsgType_Equip = 2;

    private String sAid_ID;
    private Aid aid;
    private List<Dict> aidTypeData = new ArrayList<>();
    private List<Dict> aidStationData = new ArrayList<>();
    private List<Dict> aidIconData = new ArrayList<>();
    private List<Dict> aidLightingData = new ArrayList<>();
    private List<Dict> aidMarkData = new ArrayList<>();
    private List<Nfc> nfcData = new ArrayList<>();
    private List<AidEquip> aidEquipData = new ArrayList<>();

    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    public AidDetailView(Context context, String sAid_ID) {
        super(context);
        this.context = context;
        this.sAid_ID = sAid_ID;

        LayoutInflater.from(context).inflate(R.layout.fragment_aid_detail_view, this);
        ButterKnife.bind(this);

        initData();
    }


    private void initAidInfo() {

        if(aid == null){
            return;
        }

        QMUICommonListItemView aidId = mGroupListView.createItemView("ID");
        aidId.setDetailText(aid.getsAid_ID());


        QMUIGroupListView.newSection(getContext())
                .setTitle("")
                .addItemView(aidId, null)
                .addTo(mGroupListView);
    }


    private void initData(){
        //查询数据 -- 判断网络

        tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        //查询航标种类
        DataUtils.getDictData("AidType", aidTypeData);
        //查询航标航站
        DataUtils.getDictData("AidStation", aidStationData);
        //查询航标灯
        DataUtils.getDictData("AidLighting", aidLightingData);
        //查询航标设置
        DataUtils.getDictData("AidMark", aidMarkData);
        //查询航标Icon
        DataUtils.getDictData("AidIcon", aidIconData);
        //查询NFC
        DataUtils.getNfcAllData(nfcData);
        //查询器材详情
        Map<String, Object> params = new HashMap<>();
        params.put("sAid_ID", sAid_ID);
        queryDetail(params);
    }

    private void queryDetail(Map<String, Object> params){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.aidQueryDetailUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_Detail;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }


    //TODO --------------------------------------------------------------------------- handle

    private void showTips(String msg){
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create();
        tipDialog.show();
    }

    private void hideTips(){
        tipDialog.dismiss();
    }

    private void showToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean handleErrorCode(JSONObject resObj){
        if (resObj.getInteger("code") <= 0) {
            showToast(resObj.getString("msg"));
            return true;
        }
        return false;
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
                case MsgType_Detail:
                    handleAidInfo(resData);
                    break;
                default:
                    break;
            }
        }
    };


    private void handleAidInfo(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        aid = resObj.getObject("data", Aid.class);
        //展示数据
        initAidInfo();
    }




    protected void startFragment(QMUIFragment fragment) {
        if (mListener != null) {
            mListener.startFragment(fragment);
        }
    }

    public void setViewPagerListener(ViewPagerListener listener) {
        mListener = listener;
    }


}
