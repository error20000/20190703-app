package com.jian.system.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.geometry.Unit;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
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
import com.sonnyjack.library.qrcode.QrCodeUtils;
import com.sonnyjack.permission.IRequestPermissionCallBack;
import com.sonnyjack.permission.PermissionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
    private LocationManager locationManager;
    private Location location;

    private GraphicsOverlay mGraphicsOverlay;

    private List<JSONObject> storeTypeData;
    private List<JSONObject> aidData;
    private final String PointType_Aid = "aid";
    private final String PointType_Store = "store";


    private final int MsgType_Init = 0;
    private final int MsgType_AidList = 1;
    private final int MsgType_StoreList = 2;

    public MapLayout(Context context) {
        super(context);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.layout_map, this);
        ButterKnife.bind(this);

        /*locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.NETWORK_PROVIDER;// 指定LocationManager的定位方法
        //NETWORK_PROVIDER 网络定位、GPS_PROVIDER GPS定位
        Location location = locationManager.getLastKnownLocation(provider);// 调用getLastKnownLocation()方法获取当前的位置信息
        double lat = location.getLatitude();//获取纬度
        double lng = location.getLongitude();//获取经度*/
        //initLocation();


        initTopBar();
        initData();
    }

    private void initTopBar() {
        mTopBar.setTitle("电子地图");
    }

    @SuppressLint("MissingPermission")
    private void initLocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        criteria.setAltitudeRequired(false);//无海拔要求   criteria.setBearingRequired(false);//无方位要求
        criteria.setCostAllowed(true);//允许产生资费   criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗

        // 获取最佳服务对象
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        Log.d("location", location+"");
        //Log.d("getLatitude", location.getLatitude()+"");
        //Log.d("getLongitude", location.getLongitude()+"");

        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60*1000, 60*1000, gpsLocationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60*1000, 60*1000, networkListener);

        /*ArrayList<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        PermissionUtils.getInstances().requestPermission(context, permissionList, new IRequestPermissionCallBack() {
            @SuppressLint("MissingPermission")
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied() {
                Toast.makeText(context, "请在应用管理中打开定位权限", Toast.LENGTH_SHORT).show();
            }
        });*/


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
            int levelOfDetail = system.getlSys_MapLevel();
            ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
            mMapView.setMap(map);
            //地图
            addLayer(map);
            //画图
            mGraphicsOverlay = new GraphicsOverlay();
            mMapView.getGraphicsOverlays().add(mGraphicsOverlay);
            //test
            //createPointGraphicsByUrl(longitude, latitude, UrlConfig.baseUrl+"/upload/20190817/201908171805306689781.png", null);
            //点击事件
            mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(context, mMapView){
                @Override
                public boolean onSingleTapConfirmed (MotionEvent e){
                    Log.d("onSingleTapConfirmed", e.toString());
                    android.graphics.Point mapPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());
                    final ListenableFuture<List<IdentifyGraphicsOverlayResult>> listListenableFuture = mMapView.identifyGraphicsOverlaysAsync(mapPoint, 12, false, 5);
                    listListenableFuture.addDoneListener(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                List<IdentifyGraphicsOverlayResult> identifyLayerResults = listListenableFuture.get();
                                for (IdentifyGraphicsOverlayResult identifyLayerResult : identifyLayerResults) {
                                    for (final GeoElement geoElement : identifyLayerResult.getGraphics()) {
                                        showDetail(geoElement);
                                    }
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    return super.onSingleTapConfirmed(e);
                }
            });
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

    private void createPointGraphics(double lng, double lat, Symbol symbol, Map<String, Object> attrs) {
        Point point = new Point(lng, lat, SpatialReferences.getWgs84());
        if(attrs == null){
            attrs = new HashMap<>();
        }
        Graphic pointGraphic = new Graphic(point, attrs, symbol);
        mGraphicsOverlay.getGraphics().add(pointGraphic);
    }

    private void createPointGraphicsByUrl(double lng, double lat, String url, Map<String, Object> attrs) {
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(url);
        symbol.setWidth(system.getlSys_MapIconWidth());
        symbol.setHeight(system.getlSys_MapIconHeight());
        symbol.loadAsync();
        symbol.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                createPointGraphics(lng, lat, symbol, attrs);
            }
        });
    }

    private void createPointGraphicsByImg(double lng, double lat, Map<String, Object> attrs) {
        BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(context, R.mipmap.map);
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
        symbol.setWidth(system.getlSys_MapIconWidth());
        symbol.setHeight(system.getlSys_MapIconHeight());
        symbol.loadAsync();
        symbol.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                createPointGraphics(lng, lat, symbol, attrs);
            }
        });
    }

    private void showDetail(GeoElement element){
        String id = String.valueOf(element.getAttributes().get("id"));
        Log.d(TAG, "id -->"+id);
        String type = String.valueOf(element.getAttributes().get("type"));
        int index = Integer.parseInt(String.valueOf(element.getAttributes().get("index")));
        switch (type){
            case PointType_Aid:
                showAidDetail(aidData.get(index));
                break;
            case PointType_Store:
                showStoreDetail(storeTypeData.get(index));
                break;
        }
    }

    private void showAidDetail(JSONObject node){
        Log.d(TAG, "id ==>"+node.get("sAid_ID"));
    }

    private void showStoreDetail(JSONObject node){
        Log.d(TAG, "id ==>"+node.get("sStoreType_ID"));

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
                String res = HttpUtils.getInstance().sendPost(UrlConfig.storeQueryMapUrl, params);
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
                String res = HttpUtils.getInstance().sendPost(UrlConfig.aidQueryMapUrl, params);
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
        for (int i = 0; i < aidData.size(); i++) {
            JSONObject node = aidData.get(i);
            String url = node.getString("sAid_StatusIcon");
            if(Utils.isNullOrEmpty(url)){
                url = node.getString("sAid_TypeIcon");
            }
            if(Utils.isNullOrEmpty(url)){
                url = node.getString("sAid_TypeIcon");
            }
            //创建点
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("id", node.getString("sAid_ID"));
            attrs.put("type", PointType_Aid);
            attrs.put("index", i);
            double lng = node.getDouble("lAid_Lng");
            double lat = node.getDouble("lAid_Lat");
            if(Utils.isNullOrEmpty(url)){
                createPointGraphicsByImg(lng, lat, attrs);
            }else{
                createPointGraphicsByUrl(lng, lat, url, attrs);
            }
        }

    }

    private void initStoreToMap(){
        for (int i = 0; i < storeTypeData.size(); i++) {
            JSONObject node = storeTypeData.get(i);
            String url = node.getString("sStoreType_MapIconPic");
            //创建点
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("id", node.getString("sStoreType_ID"));
            attrs.put("type", PointType_Store);
            attrs.put("index", i);
            double lng = node.getDouble("lStoreType_Lng");
            double lat = node.getDouble("lStoreType_Lat");
            if(Utils.isNullOrEmpty(url)){
                createPointGraphicsByImg(lng, lat, attrs);
            }else{
                createPointGraphicsByUrl(lng, lat, url, attrs);
            }
        }
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
            aidData.add(resData.getJSONObject(i));
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
            storeTypeData.add(resData.getJSONObject(i));
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
