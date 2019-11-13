
package com.jian.system.fragment.components;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedEvent;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedListener;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.Symbol;
import com.jian.system.MainActivity;
import com.jian.system.R;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Messages;
import com.jian.system.entity.System;
import com.jian.system.fragment.MapLayout;
import com.jian.system.fragment.ViewPagerListener;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.sonnyjack.permission.PermissionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import chihane.jdaddressselector.BottomDialog;

public class MapFragment extends QMUIFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.mapView)
    MapView mMapView;

    private final static String TAG = MapFragment.class.getSimpleName();
    private String title = "电子地图";
    private QMUITipDialog tipDialog;
    private System system;
    private Location location;
    private int contentId = R.id.map_container;

    private GraphicsOverlay mGraphicsOverlay;
    private LocationDisplay locationDisplay;

    private List<JSONObject> storeTypeData;
    private List<JSONObject> aidData;
    private final String PointType_Aid = "aid";
    private final String PointType_Store = "store";
    private int tempZoom = 0;


    private final int MsgType_Init = 0;
    private final int MsgType_AidList = 1;
    private final int MsgType_StoreList = 2;
    private final int request_code = 1;


    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.pause();
        }
        super.onPause();
    }

   @Override
    public void onResume() {
        if (mMapView != null) {
            mMapView.resume();
        }
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        if (mMapView != null) {
            mMapView.dispose();
        }
        super.onDestroyView();
    }


    @Override
    public View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_map, null);
        ButterKnife.bind(this, rootView);
        rootView.setId(contentId);

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

        mTopBar.addRightImageButton(R.drawable.ic_refresh, R.id.topbar_right_about_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                storeTypeData = null;
                aidData = null;
                mGraphicsOverlay.getGraphics().clear(); //清除原来的点
                initData();
            }
        });

        mTopBar.setTitle(title);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == request_code){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(!locationDisplay.isStarted()){
                    Log.e("ddddddddddddd", "onRequestPermissionsResult");
                    locationDisplay.startAsync();
                }
            }
        }
    }

    private void initMap() {
        if(system == null){
            tipDialog = new QMUITipDialog.Builder(getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                    .setTipWord("系统配置获取失败。")
                    .create();
            tipDialog.show();
            return;
        }

        if (mMapView != null) {
            Basemap.Type basemapType = Basemap.Type.TOPOGRAPHIC;
            double latitude = location == null ? system.getlSys_MapLat() : location.getLatitude();
            double longitude = location == null ? system.getlSys_MapLng() : location.getLongitude();
            int levelOfDetail = system.getlSys_MapLevel();
            tempZoom = system.getlSys_MapLevel();
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
            //比例尺监听
            mMapView.addMapScaleChangedListener(new MapScaleChangedListener() {
                @Override
                public void mapScaleChanged(MapScaleChangedEvent mapScaleChangedEvent) {
                    double scale = mapScaleChangedEvent.getSource().getMapScale();
                    int zoom = scaleToZoom(scale);
                    changeZoom(zoom);
                }
            });
            //定位服务
            arcgisLocation();
        }
    }

    private void changeZoom(int zoom){
        Log.e("dddddddddddddddd", tempZoom +"----"+zoom+"");
        //同一级别
        if(tempZoom == zoom){
            return;
        }
        //同一组别
        if(tempZoom <= system.getlSys_MapLevelPoint()){
            if(zoom <= system.getlSys_MapLevelPoint()){
                tempZoom = zoom;
                return;
            }
        }else if(tempZoom <= system.getlSys_MapLevelDef()){
            if(zoom <= system.getlSys_MapLevelDef() && zoom > system.getlSys_MapLevelPoint()){
                tempZoom = zoom;
                return;
            }
        }else{
            if(zoom > system.getlSys_MapLevelDef()){
                tempZoom = zoom;
                return;
            }
        }
        tempZoom = zoom;
        //删除
        //mGraphicsOverlay.getGraphics().clear();
        //新增
        //initAidToMap();
        //initStoreToMap();
        for (int i = 0; i < mGraphicsOverlay.getGraphics().size(); i++) {
            Graphic graphic = mGraphicsOverlay.getGraphics().get(i);
            updateGraphic(graphic);
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

    private void arcgisLocation() {
        //初始化定位
        locationDisplay = mMapView.getLocationDisplay();
        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        //权限检查
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "没有位置服务权限");
            //请求权限
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, request_code);
            return;
        }
        //开始定位
        locationDisplay.startAsync();
        //获取的点是基于当前地图坐标系的点
        Point point = locationDisplay.getMapLocation();
        Log.e("xyh", "point: " + point.toString());

        //获取基于GPS的位置信息
        LocationDataSource.Location location = locationDisplay.getLocation();
        //基于WGS84的经纬度坐标。
        Point point1 = location.getPosition();
        if (point1 != null) {
            Log.e("xyh", "point1: " + point1.toString());
        }
        locationDisplay.addLocationChangedListener(new LocationDisplay.LocationChangedListener() {
            @Override
            public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
                Log.e("onLocationChanged", "point: " + locationChangedEvent.getLocation().getPosition().toString());
            }
        });
    }

    private void locateResult(Location location){
        Log.d(TAG, "location getLongitude" + location.getLongitude());
        Log.d(TAG, "location getLatitude" + location.getLatitude());
        this.location = location;
    }

    private void createPointGraphics(double lng, double lat, Symbol symbol, Map<String, Object> attrs) {
        Point point = new Point(lng, lat, SpatialReferences.getWgs84());
        if(attrs == null){
            attrs = new HashMap<>();
        }
        Graphic pointGraphic = new Graphic(point, attrs, symbol);
        mGraphicsOverlay.getGraphics().add(pointGraphic);
    }

    private void createPointGraphicsByUrl(double lng, double lat, PictureMarkerSymbol symbol, Map<String, Object> attrs) {
        symbol.loadAsync();
        symbol.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                createPointGraphics(lng, lat, symbol, attrs);
            }
        });
    }

    private void createPointGraphicsByImg(double lng, double lat, PictureMarkerSymbol symbol, Map<String, Object> attrs) {
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
        //String lngStr = node.getString("lAid_Lng");
        //lngTV.setText("经度：" + FormatUtils.formatDegree(lngStr));
        String lngStr = node.getString("lAid_LngDu") + "°"
                + node.getString("lAid_LngFen") + "'"
                + node.getString("lAid_LngMiao") + "\"";
        lngTV.setText("经度：" + lngStr);
        TextView latTV = view.findViewById(R.id.callout_lat);
        //String latStr = node.getString("lAid_Lat");
        //latTV.setText("纬度："+ FormatUtils.formatDegree(latStr));
        String latStr = node.getString("lAid_LatDu") + "°"
                + node.getString("lAid_LatFen") + "'"
                + node.getString("lAid_LatMiao") + "\"";
        latTV.setText("纬度："+ latStr);
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
                bundle.putString("from", "map");
                AidDetailFragment fragment = new AidDetailFragment();
                fragment.setArguments(bundle);
                //startFragment(fragment);
                //startFragmentAndDestroyCurrent(fragment);
                getFragmentManager().beginTransaction()
                        .add(contentId, fragment, "MapFragment_Aid")
                        .addToBackStack("MapFragment_Aid")
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
        //String lngStr = node.getString("lStoreType_Lng");
        //String latStr = node.getString("lStoreType_Lat");
        //location.setText(FormatUtils.formatDegree(lngStr)+" ~ "+FormatUtils.formatDegree(latStr));
        String lngStr = node.getString("lStoreType_LngDu") + "°"
                + node.getString("lStoreType_LngFen") + "'"
                + node.getString("lStoreType_LngMiao") + "\"";
        String latStr = node.getString("lStoreType_LatDu") + "°"
                + node.getString("lStoreType_LatFen") + "'"
                + node.getString("lStoreType_LatMiao") + "\"";
        location.setText(lngStr+" ~ "+latStr);
        view.findViewById(R.id.callout_show_detail).setVisibility(View.GONE);

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
            attrs.put("status", node.getString("sAid_Status"));
            attrs.put("pic", url);
            double lng = node.getDouble("lAid_Lng");
            double lat = node.getDouble("lAid_Lat");
            if(tempZoom <= system.getlSys_MapLevelPoint()){
                String color = "normal".equals(attrs.get("status")) ? "#008000" : "red";
                int size = system.getlSys_MapIconWidthPoint();
                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol();
                symbol.setColor(Color.parseColor(color));
                symbol.setSize(size);
                symbol.setStyle(SimpleMarkerSymbol.Style.CIRCLE);
                createPointGraphics(lng, lat, symbol, attrs);
            }else if(tempZoom <= system.getlSys_MapLevelDef()){
                int iconRes = "normal".equals(attrs.get("status")) ? R.mipmap.map1 : R.mipmap.map2;
                BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), iconRes);
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
                symbol.setWidth(system.getlSys_MapIconWidthDef());
                symbol.setHeight(100);
                createPointGraphicsByImg(lng, lat, symbol, attrs);
            }else if(Utils.isNullOrEmpty(url)){
                int iconRes = "normal".equals(attrs.get("status")) ? R.mipmap.map : R.mipmap.map2;
                BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), iconRes);
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
                symbol.setWidth(system.getlSys_MapIconWidthDef());
                symbol.setHeight(system.getlSys_MapIconHeightDef());
                createPointGraphicsByImg(lng, lat, symbol, attrs);
            }else{
                url = UrlConfig.baseUrl +"/"+ url;
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(url);
                symbol.setWidth(system.getlSys_MapIconWidth());
                symbol.setHeight(system.getlSys_MapIconHeight());
                createPointGraphicsByUrl(lng, lat, symbol, attrs);
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
            attrs.put("status", node.getString("status"));
            attrs.put("pic", url);
            double lng = node.getDouble("lStoreType_Lng");
            double lat = node.getDouble("lStoreType_Lat");

            if(tempZoom <= system.getlSys_MapLevelPoint()){
                String color = "normal".equals(attrs.get("status")) ? "#008000" : "red";
                int size = system.getlSys_StoreIconWidthPoint();
                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol();
                symbol.setColor(Color.parseColor(color));
                symbol.setSize(size);
                symbol.setStyle(SimpleMarkerSymbol.Style.CIRCLE);
                createPointGraphics(lng, lat, symbol, attrs);
            }else if(tempZoom <= system.getlSys_MapLevelDef()){
                int iconRes = "normal".equals(attrs.get("status")) ? R.mipmap.map1 : R.mipmap.map2;
                BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), iconRes);
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
                symbol.setWidth(system.getlSys_StoreIconWidthDef());
                symbol.setHeight(system.getlSys_StoreIconHeightDef());
                createPointGraphicsByImg(lng, lat, symbol, attrs);
            }else if(Utils.isNullOrEmpty(url)){
                int iconRes = "normal".equals(attrs.get("status")) ? R.mipmap.map : R.mipmap.map2;
                BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), iconRes);
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
                symbol.setWidth(system.getlSys_StoreIconWidthDef());
                symbol.setHeight(system.getlSys_StoreIconHeightDef());
                createPointGraphicsByImg(lng, lat, symbol, attrs);
            }else {
                url = UrlConfig.baseUrl +"/"+ url;
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(url);
                symbol.setWidth(system.getlSys_StoreIconWidth());
                symbol.setHeight(system.getlSys_StoreIconHeight());
                createPointGraphicsByUrl(lng, lat, symbol, attrs);
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


    private Graphic updateGraphic(Graphic graphic){
        Map<String, Object> attrs = graphic.getAttributes();
        String url = (String) attrs.get("pic");
        if( PointType_Aid.equals(attrs.get("type")) ){
            if(tempZoom <= system.getlSys_MapLevelPoint()){
                String color = "normal".equals(attrs.get("status")) ? "#008000" : "red";
                int size = system.getlSys_MapIconWidthPoint();
                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol();
                symbol.setColor(Color.parseColor(color));
                symbol.setSize(size);
                symbol.setStyle(SimpleMarkerSymbol.Style.CIRCLE);
                graphic.setSymbol(symbol);
            }else if(tempZoom <= system.getlSys_MapLevelDef()){
                int iconRes = "normal".equals(attrs.get("status")) ? R.mipmap.map1 : R.mipmap.map2;
                BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), iconRes);
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
                symbol.setWidth(system.getlSys_MapIconWidthDef());
                symbol.setHeight(system.getlSys_MapIconHeightDef());
                graphic.setSymbol(symbol);
            }else if(Utils.isNullOrEmpty(url)){
                int iconRes = "normal".equals(attrs.get("status")) ? R.mipmap.map : R.mipmap.map2;
                BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), iconRes);
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
                symbol.setWidth(system.getlSys_MapIconWidthDef());
                symbol.setHeight(system.getlSys_MapIconHeightDef());
                graphic.setSymbol(symbol);
            }else{
                url = UrlConfig.baseUrl +"/"+ url;
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(url);
                symbol.setWidth(system.getlSys_MapIconWidth());
                symbol.setHeight(system.getlSys_MapIconHeight());
                graphic.setSymbol(symbol);
            }
        }else if( PointType_Store.equals(attrs.get("type")) ){
            if(tempZoom <= system.getlSys_MapLevelPoint()){
                String color = "normal".equals(attrs.get("status")) ? "#008000" : "red";
                int size = system.getlSys_StoreIconWidthPoint();
                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol();
                symbol.setColor(Color.parseColor(color));
                symbol.setSize(size);
                symbol.setStyle(SimpleMarkerSymbol.Style.CIRCLE);
                graphic.setSymbol(symbol);
            }else if(tempZoom <= system.getlSys_MapLevelDef()){
                int iconRes = "normal".equals(attrs.get("status")) ? R.mipmap.map1 : R.mipmap.map2;
                BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), iconRes);
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
                symbol.setWidth(system.getlSys_StoreIconWidthDef());
                symbol.setHeight(system.getlSys_StoreIconHeightDef());
                graphic.setSymbol(symbol);
            }else if(Utils.isNullOrEmpty(url)){
                int iconRes = "normal".equals(attrs.get("status")) ? R.mipmap.map : R.mipmap.map2;
                BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), iconRes);
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
                symbol.setWidth(system.getlSys_StoreIconWidthDef());
                symbol.setHeight(system.getlSys_StoreIconHeightDef());
                graphic.setSymbol(symbol);
            }else {
                url = UrlConfig.baseUrl +"/"+ url;
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(url);
                symbol.setWidth(system.getlSys_StoreIconWidth());
                symbol.setHeight(system.getlSys_StoreIconHeight());
                graphic.setSymbol(symbol);
            }
        }
        return graphic;
    }


    private int scaleToZoom(double scale){
        /*
         17 2256.994353
         18 1128.497176
         19 564.248588
         20 282.124294
         21 141.062147
         22 70.5310735
         16 4513.988705
         15 9027.977411
         14 18055.954822
         13 36111.909643
         12 72223.819286
         11 144447.638572
         10 288895.277144
         9 577790.554289
         8 1155581.108577
         7 2311162.217155
         6 4622324.434309
         5 9244648.868618
         4 18489297.737236
         3 36978595.474472
         2 73957190.948944
         1 147914381.897889
         0 295828763.795777
         */
        if(scale >= 295828763.795777){
            return 0;
        }else if( scale >= 147914381.897889){
            return 1;
        }else if( scale >= 73957190.948944){
            return 2;
        }else if( scale >= 36978595.474472){
            return 3;
        }else if( scale >= 18489297.737236){
            return 4;
        }else if( scale >= 9244648.868618){
            return 5;
        }else if( scale >= 4622324.434309){
            return 6;
        }else if( scale >= 2311162.217155){
            return 7;
        }else if( scale >= 1155581.108577){
            return 8;
        }else if( scale >= 577790.554289){
            return 9;
        }else if( scale >= 288895.277144){
            return 10;
        }else if( scale >= 144447.638572){
            return 11;
        }else if( scale >= 72223.819286){
            return 12;
        }else if( scale >= 36111.909643){
            return 13;
        }else if( scale >= 18055.954822){
            return 14;
        }else if( scale >= 9027.977411){
            return 15;
        }else if( scale >= 4513.988705){
            return 16;
        }else if( scale >= 2256.994353){
            return 17;
        }else if( scale >= 1128.497176){
            return 18;
        }else if( scale >= 564.248588){
            return 19;
        }else if( scale >= 282.124294){
            return 20;
        }else if( scale >= 141.062147){
            return 21;
        }else if( scale >= 70.5310735){
            return 22;
        }else{
            return 23;
        }
    }
}
