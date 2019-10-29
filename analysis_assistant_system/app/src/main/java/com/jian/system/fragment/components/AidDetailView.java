
package com.jian.system.fragment.components;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.R;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Aid;
import com.jian.system.entity.AidEquip;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Nfc;
import com.jian.system.fragment.ViewPagerListener;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.jian.system.view.UrlImageView;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
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
    private List<Dict> aidStatusData = new ArrayList<>();
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

        QMUICommonListItemView sAid_ID = mGroupListView.createItemView("ID");
        sAid_ID.setDetailText(aid.getsAid_ID());

        QMUICommonListItemView sAid_Name = mGroupListView.createItemView("航标名称");
        sAid_Name.setDetailText(aid.getsAid_Name());

        QMUICommonListItemView sAid_NO = mGroupListView.createItemView("航标编码");
        sAid_NO.setDetailText(aid.getsAid_NO());

        QMUICommonListItemView lAid_Lng = mGroupListView.createItemView("经度");
        String strLng = aid.getlAid_LngDu()+"°"+aid.getlAid_LngFen()+"′"+aid.getlAid_LngMiao()+"″ E";
        lAid_Lng.setDetailText(strLng);

        QMUICommonListItemView lAid_Lat = mGroupListView.createItemView("纬度");
        String strLat = aid.getlAid_LatDu()+"°"+aid.getlAid_LatFen()+"′"+aid.getlAid_LatMiao()+"″ N";
        lAid_Lat.setDetailText(strLat);

        QMUICommonListItemView sAid_Station = mGroupListView.createItemView("归属航标站");
        sAid_Station.setDetailText(FormatUtils.formatDict(aid.getsAid_Station(), aidStationData) );

        QMUICommonListItemView sAid_Type = mGroupListView.createItemView("航标种类");
        sAid_Type.setDetailText(FormatUtils.formatDict(aid.getsAid_Type(), aidTypeData) );

        QMUICommonListItemView sAid_Status = mGroupListView.createItemView("航标状态");
        sAid_Status.setDetailText(FormatUtils.formatDict(aid.getsAid_Status(), aidStatusData) );

        QMUICommonListItemView sAid_Icon = mGroupListView.createItemView("航标图标");
        String imgUrl = FormatUtils.formatDictCustom(aid.getsAid_Icon(), aidIconData, "Picture");
        if(!Utils.isNullOrEmpty(imgUrl)){
            sAid_Icon.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
            UrlImageView imageView = new UrlImageView(context);
            imageView.setImageURL(UrlConfig.baseUrl+"/" + imgUrl );
            sAid_Icon.addAccessoryCustomView(imageView);
        }

        QMUICommonListItemView dAid_BuildDate = mGroupListView.createItemView("始建时间");
        String buildDate = aid.getdAid_BuildDate() == null ? "" : FormatUtils.formatDate("yyyy-MM-dd", aid.getdAid_BuildDate());
        dAid_BuildDate.setDetailText(buildDate);

        QMUICommonListItemView dAid_DelDate = mGroupListView.createItemView("撤除时间");
        String delDate = aid.getdAid_DelDate() == null ? "" : FormatUtils.formatDate("yyyy-MM-dd", aid.getdAid_DelDate());
        dAid_DelDate.setDetailText(delDate);

        QMUICommonListItemView sAid_Lighting = mGroupListView.createItemView("灯质");
        String str = FormatUtils.formatDict(aid.getsAid_Lighting(), aidLightingData);
        /*String strDesc = FormatUtils.formatDictDesc(aid.getsAid_Lighting(), aidLightingData);
        if(!Utils.isNullOrEmpty(strDesc)){
            str += "("+strDesc+")";
        }*/
        TextView equipNameEditText = new TextView(getContext());
        FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        equipNameEditText.setLayoutParams(textLayoutParams);
        equipNameEditText.setPadding(0,10,0,10);
        equipNameEditText.setMaxWidth(QMUIDisplayHelper.dp2px(getContext(), 240));
        equipNameEditText.setBackgroundDrawable(null);
        equipNameEditText.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_5));
        equipNameEditText.setTextSize(QMUIDisplayHelper.px2sp(getContext(), QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_common_list_item_detail_h_text_size) ));
        equipNameEditText.setText(str);
        //sAid_Lighting.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dp2px(getContext(), 100)));
        sAid_Lighting.setMinimumHeight(sAid_Lighting.getLayoutParams().height);
        sAid_Lighting.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, equipNameEditText.getLayoutParams().height));
        sAid_Lighting.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        sAid_Lighting.addAccessoryCustomView(equipNameEditText);
//        sAid_Lighting.setDetailText(str);

        QMUICommonListItemView sAid_Mark = mGroupListView.createItemView("航标属性");
        sAid_Mark.setDetailText(FormatUtils.formatDict(aid.getsAid_Mark(), aidMarkData) );

        QMUICommonListItemView sAid_NfcID = mGroupListView.createItemView("NFC标签");
        sAid_NfcID.setDetailText(FormatUtils.formatNFC(aid.getsAid_NfcID(), nfcData) );

        QMUICommonListItemView dAid_CreateDate = mGroupListView.createItemView("创建日期");
        String createDate = aid.getdAid_CreateDate() == null ? "" : FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", aid.getdAid_CreateDate());
        dAid_CreateDate.setDetailText(createDate);


        QMUIGroupListView.newSection(getContext())
                .setTitle("")
                .addItemView(sAid_ID, null)
                .addItemView(sAid_Name, null)
                .addItemView(sAid_NO, null)
                .addItemView(lAid_Lng, null)
                .addItemView(lAid_Lat, null)
                .addItemView(sAid_Type, null)
                .addItemView(sAid_Status, null)
                .addItemView(sAid_Icon, null)
                .addItemView(sAid_Station, null)
                .addItemView(dAid_BuildDate, null)
                .addItemView(dAid_DelDate, null)
                .addItemView(sAid_Lighting, null)
                .addItemView(sAid_Mark, null)
                .addItemView(sAid_NfcID, null)
                .addItemView(dAid_CreateDate, null)
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
        DataUtils.getDictData(Constant.aidTypeDict, aidTypeData);
        //查询航标状态
        DataUtils.getDictData(Constant.aidStatusDict, aidStatusData);
        //查询航标航站
        DataUtils.getDictData(Constant.aidStationDict, aidStationData);
        //查询航标灯
        DataUtils.getDictData(Constant.aidLightingDict, aidLightingData);
        //查询航标设置
        DataUtils.getDictData(Constant.aidMarkDict, aidMarkData);
        //查询航标Icon
        DataUtils.getDictData(Constant.aidIconDict, aidIconData);
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
