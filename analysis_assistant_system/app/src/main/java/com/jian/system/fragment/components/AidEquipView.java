
package com.jian.system.fragment.components;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
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

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.msg_title)
    QMUICommonListItemView mMsgTitle;
    @BindView(R.id.recyclerViewMsg)
    RecyclerView mMsgRecyclerView;

    private ViewPagerListener mListener;
    private Context context;
    private QMUITipDialog tipDialog;
    private BottomDialog selectorDialog;
    private final int MsgType_Detail = 1;
    private final int MsgType_Equip = 2;


    private String sAid_ID;
    private Aid aid;
    private List<AidEquip> aidEquipData = new ArrayList<>();

    public AidEquipView(Context context, String sAid_ID) {
        super(context);
        this.context = context;
        this.sAid_ID = sAid_ID;

        LayoutInflater.from(context).inflate(R.layout.layout_home, this);
        ButterKnife.bind(this);

        initData();
    }


    private  void initAidEquip(){
        /*if(historyData.size() == 0){
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
    */
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
