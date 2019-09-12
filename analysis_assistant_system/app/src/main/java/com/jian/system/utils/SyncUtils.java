package com.jian.system.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jian.system.Application;
import com.jian.system.config.UrlConfig;
import com.jian.system.dao.NfcMapper;
import com.jian.system.entity.Aid;
import com.jian.system.entity.AidEquip;
import com.jian.system.entity.AidMapIcon;
import com.jian.system.entity.AidTypeMapIcon;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Equip;
import com.jian.system.entity.EquipAis;
import com.jian.system.entity.EquipBattery;
import com.jian.system.entity.EquipLamp;
import com.jian.system.entity.EquipLog;
import com.jian.system.entity.EquipRadar;
import com.jian.system.entity.EquipSolarEnergy;
import com.jian.system.entity.EquipSpareLamp;
import com.jian.system.entity.EquipTelemetry;
import com.jian.system.entity.EquipViceLamp;
import com.jian.system.entity.Nfc;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;
import com.jian.system.entity.Sync;
import com.jian.system.entity.System;
import com.jian.system.entity.User;
import com.jian.system.entity.UserAid;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.service.AidEquipService;
import com.jian.system.service.AidMapIconService;
import com.jian.system.service.AidService;
import com.jian.system.service.AidTypeMapIconService;
import com.jian.system.service.DictService;
import com.jian.system.service.EquipAisService;
import com.jian.system.service.EquipBatteryService;
import com.jian.system.service.EquipLampService;
import com.jian.system.service.EquipLogService;
import com.jian.system.service.EquipRadarService;
import com.jian.system.service.EquipService;
import com.jian.system.service.EquipSolarEnergyService;
import com.jian.system.service.EquipSpareLampService;
import com.jian.system.service.EquipTelemetryService;
import com.jian.system.service.EquipViceLampService;
import com.jian.system.service.NfcService;
import com.jian.system.service.StoreService;
import com.jian.system.service.StoreTypeService;
import com.jian.system.service.SyncService;
import com.jian.system.service.SystemService;
import com.jian.system.service.UserAidService;
import com.jian.system.service.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncUtils {

    private final static String TAG = SyncUtils.class.getSimpleName();

    /**
     * 同步基础信息
     */
    public static void baseData(){
        systemData();
        dictData();
        initEquipData();
        initStoreData();
        initAidData();
    }

    /**
     * 同步用户基础信息
     */
    public static void loginData(String sUser_ID){
        userData(sUser_ID);
        userAidData(sUser_ID);
    }

    /**
     * 同步器材信息
     */
    public static void initEquipData(){
        equipData();
        equipLogData();
        equipAisData();
        equipBatteryData();
        equipLampData();
        equipRadarData();
        equipSolarEnergyData();
        equipSpareLampData();
        equipTelemetryData();
        equipViceLampData();
    }

    /**
     * 同步仓库信息
     */
    public static void initStoreData(){
        storeTypeData();
        storeData();
    }

    /**
     * 同步航标信息
     */
    public static void initAidData(){
        aidData();
        aidEquipData();
        aidMapIconData();
        aidTypeMapIconData();
    }

    /**
     * 同步系统信息
     */
    public static void systemData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncSystemUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<System> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, System.class));
                }
                SystemService service = new SystemService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "System 数据同步成功。");
            }
        });
    }

    public static void userData(String sUser_ID){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("sUser_ID", sUser_ID);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncUserUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<User> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, User.class));
                }
                UserService service = new UserService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "User 数据同步成功。");
            }
        });
    }

    public static void userAidData(String sUser_ID){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("sUser_ID", sUser_ID);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncUserAidUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<UserAid> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, UserAid.class));
                }
                UserAidService service = new UserAidService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "UserAid 数据同步成功。");
            }
        });
    }

    /**
     * 同步数据字典信息
     */
    public static void dictData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncDictUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<Dict> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, Dict.class));
                }
                DictService service = new DictService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "Dict 数据同步成功。");
            }
        });
    }

    //TODO ------------------------------------------------------------------------------------- equip

    public static void equipData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncEquipUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<Equip> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, Equip.class));
                }
                EquipService service = new EquipService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "Equip 数据同步成功。");
            }
        });
    }

    public static void equipLogData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                //日志做增量同步
                //查询上次同步时间
                Date syncDate = null;
                SyncService syncService = new SyncService(Application.getContext());
                Sync sync = syncService.selectOne("tBase_EquipLog");
                if(sync != null){
                    syncDate = sync.getdSync_UpdateDate();
                }
                Log.d(TAG, "Sync：" + JSONObject.toJSONString(sync));
                //查询数据
                Map<String, Object> params = new HashMap<>();
                params.put("syncDate", syncDate == null ? "" : syncDate.getTime());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncEquipLogUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<EquipLog> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, EquipLog.class));
                }
                EquipLogService service = new EquipLogService(Application.getContext());
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "EquipLog 数据同步成功。");
                //记录
                if(sync == null){
                    sync = new Sync();
                    sync.setsSync_TableName("tBase_EquipLog");
                    sync.setdSync_UpdateDate(new Date());
                    syncService.insert(sync);
                }else{
                    sync.setsSync_TableName("tBase_EquipLog");
                    sync.setdSync_UpdateDate(new Date());
                    syncService.update(sync);
                }
            }
        });
    }

    public static void equipAisData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncEquipAisUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<EquipAis> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, EquipAis.class));
                }
                EquipAisService service = new EquipAisService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "EquipAis 数据同步成功。");
            }
        });
    }

    public static void equipBatteryData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncEquipBatteryUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<EquipBattery> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, EquipBattery.class));
                }
                EquipBatteryService service = new EquipBatteryService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "EquipBattery 数据同步成功。");
            }
        });
    }

    public static void equipLampData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncEquipLampUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<EquipLamp> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, EquipLamp.class));
                }
                EquipLampService service = new EquipLampService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "EquipLamp 数据同步成功。");
            }
        });
    }

    public static void equipRadarData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncEquipRadarUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<EquipRadar> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, EquipRadar.class));
                }
                EquipRadarService service = new EquipRadarService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "EquipRadar 数据同步成功。");
            }
        });
    }

    public static void equipSolarEnergyData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncEquipSolarEnergyUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<EquipSolarEnergy> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, EquipSolarEnergy.class));
                }
                EquipSolarEnergyService service = new EquipSolarEnergyService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "EquipSolarEnergy 数据同步成功。");
            }
        });
    }

    public static void equipSpareLampData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncEquipSpareLampUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<EquipSpareLamp> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, EquipSpareLamp.class));
                }
                EquipSpareLampService service = new EquipSpareLampService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "EquipSpareLamp 数据同步成功。");
            }
        });
    }

    public static void equipTelemetryData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncEquipTelemetryUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<EquipTelemetry> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, EquipTelemetry.class));
                }
                EquipTelemetryService service = new EquipTelemetryService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "EquipTelemetry 数据同步成功。");
            }
        });
    }

    public static void equipViceLampData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncEquipViceLampUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<EquipViceLamp> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, EquipViceLamp.class));
                }
                EquipViceLampService service = new EquipViceLampService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "EquipViceLamp 数据同步成功。");
            }
        });
    }

    //TODO -------------------------------------------------------------------------------------store

    public static void storeTypeData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncStoreTypeUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<StoreType> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, StoreType.class));
                }
                StoreTypeService service = new StoreTypeService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "StoreType 数据同步成功。");
            }
        });
    }

    public static void storeData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncStoreUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<Store> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, Store.class));
                }
                StoreService service = new StoreService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "Store 数据同步成功。");
            }
        });
    }

    //TODO -------------------------------------------------------------------------------------nfc

    public static void nfcData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncNfcUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<Nfc> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, Nfc.class));
                }
                NfcService service = new NfcService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "Nfc 数据同步成功。");
            }
        });
    }

    //TODO -------------------------------------------------------------------------------------aid

    public static void aidData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncAidUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<Aid> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, Aid.class));
                }
                AidService service = new AidService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "Aid 数据同步成功。");
            }
        });
    }

    public static void aidEquipData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncAidEquipUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<AidEquip> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, AidEquip.class));
                }
                AidEquipService service = new AidEquipService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "AidEquip 数据同步成功。");
            }
        });
    }

    public static void aidMapIconData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncAidMapIconUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<AidMapIcon> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, AidMapIcon.class));
                }
                AidMapIconService service = new AidMapIconService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "AidMapIcon 数据同步成功。");
            }
        });
    }

    public static void aidTypeMapIconData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncAidTypeMapIconUrl, params);
                if(Utils.isNullOrEmpty(res)){
                    Log.d(TAG, "获取数据失败：res为空");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                //处理数据
                List<AidTypeMapIcon> onlineData = new ArrayList<>();
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    onlineData.add(data.getObject(i, AidTypeMapIcon.class));
                }
                AidTypeMapIconService service = new AidTypeMapIconService(Application.getContext());
                //删除
                service.deleteAll();
                //新增
                service.insert(onlineData);
                Log.d(TAG, "AidTypeMapIcon 数据同步成功。");
            }
        });
    }

    //TODO ------------------------------------------------------------------------------------- Msg

}
