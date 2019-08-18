
package com.jian.system.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.Symbol;
import com.jian.system.Application;
import com.jian.system.R;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Aid;
import com.jian.system.entity.StoreType;
import com.jian.system.entity.System;
import com.jian.system.fragment.components.AidListFragment;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapLayout extends QMUIWindowInsetLayout {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.mapView)
    MapView mMapView;

    private final static String TAG = MapLayout.class.getSimpleName();
    private QMUITipDialog tipDialog;
    private ViewPagerListener mListener;
    private System system;
    private Context context;

    private GraphicsOverlay mGraphicsOverlay;

    private List<StoreType> storeTypeData ;
    private List<Aid> aidData;


    private final int MsgType_Init = 0;
    private final int MsgType_AidList = 1;
    private final int MsgType_StoreList = 2;

    public MapLayout(Context context) {
        super(context);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.layout_map, this);
        ButterKnife.bind(this);

        initTopBar();
        initData();
    }

    private void initTopBar() {
        mTopBar.setTitle("电子地图");
    }


    private void initMap() {

        if(system == null){
            tipDialog = new QMUITipDialog.Builder(getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                    .setTipWord("获取失败。")
                    .create();
            tipDialog.show();
            return;
        }

        if (mMapView != null) {
            Basemap.Type basemapType = Basemap.Type.TOPOGRAPHIC;
            double latitude = system.getlSys_MapLat();
            double longitude = system.getlSys_MapLng();
            int levelOfDetail = 11;
            ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
            mMapView.setMap(map);
            //地图
            addLayer(map);
            //画图
            mGraphicsOverlay = new GraphicsOverlay();
            mMapView.getGraphicsOverlays().add(mGraphicsOverlay);

            //test
            createPointGraphicsByUrl(longitude, latitude, UrlConfig.baseUrl+"/upload/20190817/201908171805306689781.png");
        }
    }

    private void addLayer(ArcGISMap map){
        String url = system.getsSys_MapService();
        ServiceFeatureTable table = new ServiceFeatureTable(url);
        FeatureLayer mFeatureLayer = new FeatureLayer(table);
        mFeatureLayer.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if (mFeatureLayer.getLoadStatus() == LoadStatus.LOADED) {
                    map.getOperationalLayers().add(mFeatureLayer);
                }
            }
        });
        mFeatureLayer.loadAsync();
    }

    private void createPointGraphics(double lng, double lat, Symbol symbol) {
        Point point = new Point(lng, lat, SpatialReferences.getWgs84());
        Graphic pointGraphic = new Graphic(point, symbol);
        mGraphicsOverlay.getGraphics().add(pointGraphic);
    }

    private void createPointGraphicsByUrl(double lng, double lat, String url) {
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(url);
        symbol.setHeight(20);
        symbol.setWidth(20);
        symbol.loadAsync();
        symbol.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                createPointGraphics(lng, lng, symbol);
            }
        });
    }


    private void initData(){
        //查询数据 -- 判断网络
        tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        //查询地图配置
        querySystemData();
        //查询仓库
        queryStoreData();
        //查询航标列表
        queryAidData();
    }

    private void queryStoreData(){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.storeTypeQueryUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_StoreList;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void queryAidData(){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.aidQueryPageUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_AidList;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }


    private void querySystemData(){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.sysQueryUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_Init;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }


    private void initAidToMap(){

    }

    private void initStoreToMap(){

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
            switch (msg.what){
                case MsgType_Init:
                    handleInit(resData);
                    break;
                case MsgType_AidList:
                    handleAidList(resData);
                    break;
                case MsgType_StoreList:
                    handleStoreList(resData);
                    break;
                default:
                    break;
            }

        }
    };


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

    private void handleInit(JSONObject resObj){
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        system = resObj.getObject("data", System.class);
        //初始化地图
        initMap();
    }

    private void handleAidList(JSONObject resObj){
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        JSONArray resData = resObj.getJSONArray("data");
        if(aidData == null){
            aidData = new ArrayList<>();
        }
        for (int i = 0; i < resData.size(); i++) {
            aidData.add(resData.getObject(i, Aid.class));
        }
        //展示数据
        initAidToMap();
    }

    private void handleStoreList(JSONObject resObj){
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        JSONArray resData = resObj.getJSONArray("data");
        if(storeTypeData == null){
            storeTypeData = new ArrayList<>();
        }
        for (int i = 0; i < resData.size(); i++) {
            storeTypeData.add(resData.getObject(i, StoreType.class));
        }
        //展示数据
        initStoreToMap();
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
