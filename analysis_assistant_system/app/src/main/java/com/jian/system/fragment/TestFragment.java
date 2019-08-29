
package com.jian.system.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.Symbol;
import com.jian.system.R;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.System;
import com.jian.system.fragment.components.AidDetailFragment;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestFragment extends Fragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.mapView)
    MapView mMapView;

    private final static String TAG = MapLayout.class.getSimpleName();
    private String title = "电子地图";
    private QMUITipDialog tipDialog;
    private ViewPagerListener mListener;
    private View cacheView;
    private System system;
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


    @Override
    public void onPause() {
        Log.e("ddddddddddd", "onPause");
        if (mMapView != null) {
            Log.e("ddddddddddd", "mMapView onPause");
            mMapView.pause();
        }
        super.onPause();
    }

   @Override
    public void onResume() {
        Log.e("ddddddddddd", "onResume");
        if (mMapView != null) {
            Log.e("ddddddddddd", "mMapView onResume");
            mMapView.resume();
        }
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        Log.e("ddddddddddd", "onDestroyView");
        if (mMapView != null) {
            Log.e("ddddddddddd", "mMapView onDestroyView");
            mMapView.dispose();
        }
        super.onDestroyView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("ddddddddddd", "onCreateView2222222222222222222");

        if(cacheView == null){
            Log.e("ddddddddddd", "cacheViewcacheViewcacheViewcacheViewcacheView");
            View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_map, null);
            ButterKnife.bind(this, rootView);
            cacheView = rootView;
        }else{
            Log.e("cacheView" , cacheView.findViewById(R.id.mapView).toString());
            initData();
        }

        return cacheView;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        initLocation();
        initTopBar();
        initData();
    }

    private void initTopBar() {

        mTopBar.addRightImageButton(R.drawable.ic_refresh, R.id.topbar_right_about_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                storeTypeData = null;
                aidData = null;
                initData();
            }
        });

        mTopBar.setTitle(title);
    }

    private void initLocation(){
        //获取位置服务
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        //ACCURACY_HIGH/ACCURACY_LOW精度选择
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //高度
        criteria.setAltitudeRequired(true);
        //方位信息
        criteria.setBearingRequired(true);
        //是否允许付费
        criteria.setCostAllowed(true);
        //对电量的要求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //速度
        criteria.setSpeedRequired(true);
        //获取最佳服务
        String provider = locationManager.getBestProvider(criteria, true);
        Log.e(TAG, "provider:" + provider);
        //权限检查
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "没有位置服务权限");
            return;
        }
        //获取到了位置
        location = locationManager.getLastKnownLocation(provider);
        locateResult(location);
        //开启地理位置监听定位类型、毫秒、米、监听时间
        locationManager.requestLocationUpdates(provider, 3000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //位置变化，获取最新的位置
                locateResult(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
    }

    private void locateResult(Location location){
        Log.d(TAG, "location getLongitude" + location.getLongitude());
        Log.d(TAG, "location getLatitude" + location.getLatitude());
        this.location = location;
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
            double latitude = location == null ? system.getlSys_MapLat() : location.getLatitude();
            double longitude = location == null ? system.getlSys_MapLng() : location.getLongitude();
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
            mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(getActivity(), mMapView){
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
                                        showDetail(e, geoElement);
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
        BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.mipmap.map);
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

    private void showDetail(MotionEvent event, GeoElement element){
        String id = String.valueOf(element.getAttributes().get("id"));
        Log.d(TAG, "id -->"+id);
        String type = String.valueOf(element.getAttributes().get("type"));
        int index = Integer.parseInt(String.valueOf(element.getAttributes().get("index")));
        switch (type){
            case PointType_Aid:
                showAidDetail(event, aidData.get(index));
                break;
            case PointType_Store:
                showStoreDetail(event, storeTypeData.get(index));
                break;
        }
    }

    private void showAidDetail(MotionEvent event, JSONObject node){
        Log.d(TAG, "id ==>"+node.get("sAid_ID"));
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_map_callout_aid, null);
        //设置属性
        TextView title = view.findViewById(R.id.callout_title);
        title.setText(node.getString("sAid_Name"));
        TextView station = view.findViewById(R.id.callout_station);
        station.setText("航标站：" + node.getString("sAid_StationName"));
        TextView no = view.findViewById(R.id.callout_no);
        no.setText("编码：" + node.getString("sAid_NO"));
        TextView lngTV = view.findViewById(R.id.callout_lng);
        String lngStr = node.getString("lAid_Lng");
        lngTV.setText("经度：" + FormatUtils.formatDegree(lngStr));
        TextView latTV = view.findViewById(R.id.callout_lat);
        String latStr = node.getString("lAid_Lat");
        latTV.setText("纬度："+ FormatUtils.formatDegree(latStr));
        ImageView icon = view.findViewById(R.id.callout_icon);
        String url = node.getString("sAid_IconUrl");
        if(!Utils.isNullOrEmpty(url)){
            url = UrlConfig.baseUrl + "/"+ node.getString("sAid_IconUrl");
            icon.setImageBitmap(Utils.getHttpBitmap(url));
        }

        double lng = node.getDouble("lAid_Lng");
        double lat = node.getDouble("lAid_Lat");
        Point point = new Point(lng, lat, SpatialReferences.getWgs84());
        Callout callout = mMapView.getCallout();
        //callout.setStyle(R.xml.calloutstyle);
        callout.show(view, point);
        //事件
        view.findViewById(R.id.callout_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calloutClose(callout);
            }
        });
        view.findViewById(R.id.callout_show_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoDetail(callout, node.getString("sAid_ID"), PointType_Aid);
            }
        });
    }

    private void gotoDetail(Callout callout, String id, String type){
        switch (type){
            case PointType_Aid:
                Bundle bundle = new Bundle();
                bundle.putString("sAid_ID", id);
                AidDetailFragment fragment = new AidDetailFragment();
                fragment.setArguments(bundle);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main, fragment, "test")
                        .addToBackStack("test")
                        .commit();

                break;
            case PointType_Store:

                break;
        }
    }

    private void calloutClose(Callout callout){
        if(callout != null){
            callout.dismiss();
        }
    }

    private void showStoreDetail(MotionEvent event, JSONObject node){
        Log.d(TAG, "id ==>"+node.get("sStoreType_ID"));
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_map_callout_store, null);
        //设置属性
        TextView title = view.findViewById(R.id.callout_title);
        title.setText(node.getString("sStoreType_Name"));
        TextView station = view.findViewById(R.id.callout_station);
        station.setText(node.getString("sStoreType_StationName"));
        TextView location = view.findViewById(R.id.callout_location);
        String lngStr = node.getString("lStoreType_Lng");
        String latStr = node.getString("lStoreType_Lat");
        location.setText(FormatUtils.formatDegree(lngStr)+" ~ "+FormatUtils.formatDegree(latStr));

        double lng = node.getDouble("lStoreType_Lng");
        double lat = node.getDouble("lStoreType_Lat");
        Point point = new Point(lng, lat, SpatialReferences.getWgs84());
        Callout callout = mMapView.getCallout();
        //callout.setStyle(R.xml.calloutstyle);
        callout.show(view, point);
        //事件
        view.findViewById(R.id.callout_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calloutClose(callout);
            }
        });
        view.findViewById(R.id.callout_show_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoDetail(callout, node.getString("sStoreType_ID"), PointType_Store);
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
                url = UrlConfig.baseUrl +"/"+ url;
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

}
