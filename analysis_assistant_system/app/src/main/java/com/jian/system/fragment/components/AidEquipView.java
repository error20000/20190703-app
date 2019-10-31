
package com.jian.system.fragment.components;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jian.system.R;
import com.jian.system.adapter.AidAdapter;
import com.jian.system.adapter.AidEquipAdapter;
import com.jian.system.adapter.BaseRecyclerAdapter;
import com.jian.system.adapter.BaseRecyclerOnScrollListener;
import com.jian.system.config.UrlConfig;
import com.jian.system.decorator.DividerItemDecoration;
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
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chihane.jdaddressselector.BottomDialog;

public class AidEquipView extends QMUIWindowInsetLayout{

    private final static String TAG = AidDetailView.class.getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ViewPagerListener mListener;
    private Context context;
    private QMUITipDialog tipDialog;
    private final int MsgType_Detail = 1;
    private final int MsgType_Equip = 2;


    private String sAid_ID;
    private AidEquipAdapter mItemAdapter;
    private List<AidEquip> aidEquipData = new ArrayList<>();

    public AidEquipView(Context context, String sAid_ID) {
        super(context);
        this.context = context;
        this.sAid_ID = sAid_ID;

        LayoutInflater.from(context).inflate(R.layout.fragment_aid_equip_view, this);
        ButterKnife.bind(this);

        initData();
    }


    private  void initAidEquip(){

        mItemAdapter = new AidEquipAdapter(context, aidEquipData);
        mItemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Log.d(TAG, "onItemClick: " + aidEquipData.get(pos).getsAidEquip_EquipID());
                //进入详情页面
                intoDetail(aidEquipData.get(pos).getsAidEquip_EquipID(), aidEquipData.get(pos).getsAidEquip_EquipType());
            }
        });
        mRecyclerView.setAdapter(mItemAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    }

    private void intoDetail(String sEquip_NO, String sEquip_Type){
        Bundle bundle = new Bundle();
        bundle.putString("id", sEquip_NO);
        bundle.putString("type", sEquip_Type);
        EquipDetailFragment fragment = new EquipDetailFragment();
        fragment.setArguments(bundle);
        startFragment(fragment);
    }

    private void initData(){
        //查询数据 -- 判断网络

        tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        //查询器材详情
        Map<String, Object> params = new HashMap<>();
        params.put("sAid_ID", sAid_ID);
        queryEquip(params);
    }

    private void queryEquip(Map<String, Object> params){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.aidQueryEquipUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_Equip;
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
                case MsgType_Equip:
                    handleAidEquip(resData);
                    break;
                default:
                    break;
            }
        }
    };



    private void handleAidEquip(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        JSONArray resData = resObj.getJSONArray("data");
        for (int i = 0; i < resData.size(); i++) {
            aidEquipData.add(resData.getObject(i, AidEquip.class));
        }
        //展示数据
        initAidEquip();
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
