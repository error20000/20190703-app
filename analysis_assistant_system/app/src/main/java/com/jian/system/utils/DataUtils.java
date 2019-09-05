package com.jian.system.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Nfc;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtils {

    private final static String TAG = DataUtils.class.getSimpleName();


    /**
     * 根据字典分类，查询数据字典
     * @param sDict_DictTypeNO
     * @param resData
     */
    public static void getDictData(String sDict_DictTypeNO, List<Dict> resData){
        if(resData == null){
            throw new RuntimeException("Dict is list can not be null!");
        }
        //缓存获取数据
        List<Dict> cacheData = DataCacheUtils.getList(sDict_DictTypeNO, Dict.class);
        if(cacheData != null){
            Log.d(TAG, sDict_DictTypeNO + " Dict 从缓存获取数据。");
            resData.addAll(cacheData);
            return;
        }
        //获取数据
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("sDict_DictTypeNO", sDict_DictTypeNO);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.dictQueryUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.dictQueryUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") > 0){
                    JSONArray data = resObj.getJSONArray("data");
                    for (int i = 0; i < data.size(); i++) {
                        resData.add(data.getObject(i, Dict.class));
                    }
                    DataCacheUtils.set(sDict_DictTypeNO, resData);
                }
            }
        });
    }

    /**
     * 获取一级仓库
     * @param storeTypeData
     */
    public static void getStoreTypeData(List<StoreType> storeTypeData){
        if(storeTypeData == null){
            throw new RuntimeException("StoreType is list can not be null!");
        }
        String ckey = "cache_store_type_data";
        //缓存获取数据
        List<StoreType> cacheData = DataCacheUtils.getList(ckey, StoreType.class);
        if(cacheData != null){
            Log.d(TAG, " StoreType 从缓存获取数据。");
            storeTypeData.addAll(cacheData);
            return;
        }
        //获取数据
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.storeTypeQueryUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.storeTypeQueryUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") > 0){
                    JSONArray data = resObj.getJSONArray("data");
                    for (int i = 0; i < data.size(); i++) {
                        storeTypeData.add(data.getObject(i, StoreType.class));
                    }
                    DataCacheUtils.set(ckey, storeTypeData);
                }
            }
        });
    }

    /**
     * 查询其他仓库
     * @param storeData
     */
    public static void getStoreData(List<Store> storeData){
        if(storeData == null){
            throw new RuntimeException("Store is list can not be null!");
        }
        String ckey = "cache_store_data";
        //缓存获取数据
        List<Store> cacheData = DataCacheUtils.getList(ckey, Store.class);
        if(cacheData != null){
            Log.d(TAG, " Store 从缓存获取数据。");
            storeData.addAll(cacheData);
            return;
        }
        //获取数据
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.storeQueryUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.storeQueryUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") > 0){
                    JSONArray data = resObj.getJSONArray("data");
                    for (int i = 0; i < data.size(); i++) {
                        storeData.add(data.getObject(i, Store.class));
                    }
                    DataCacheUtils.set(ckey, storeData);
                }
            }
        });
    }

    /**
     * 查询所有nfc标签
     * @param nfcAllData
     */
    public static void getNfcAllData(List<Nfc> nfcAllData){
        if(nfcAllData == null){
            throw new RuntimeException("Nfc is list can not be null!");
        }
        String ckey = "cache_nfc_all_data";
        //缓存获取数据
        List<Nfc> cacheData = DataCacheUtils.getList(ckey, Nfc.class);
        if(cacheData != null){
            Log.d(TAG, " NfcAll 从缓存获取数据。");
            nfcAllData.addAll(cacheData);
            return;
        }
        //获取数据
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.nfcQueryUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.nfcQueryUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") > 0){
                    JSONArray data = resObj.getJSONArray("data");
                    for (int i = 0; i < data.size(); i++) {
                        nfcAllData.add(data.getObject(i, Nfc.class));
                    }
                    DataCacheUtils.set(ckey, nfcAllData);
                }
            }
        });
    }

    /**
     * 查询未使用的nfc标签
     * @param nfcUnusedData
     */
    public static void getNfcUnusedData(List<Nfc> nfcUnusedData){
        if(nfcUnusedData == null){
            throw new RuntimeException("Nfc is list can not be null!");
        }
        //获取数据
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.nfcUnusedQueryUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.nfcUnusedQueryUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") > 0){
                    JSONArray data = resObj.getJSONArray("data");
                    for (int i = 0; i < data.size(); i++) {
                        nfcUnusedData.add(data.getObject(i, Nfc.class));
                    }
                }
            }
        });
    }

    /**
     * 查询所有航标
     * @param aidAllData
     */
    public static void getAidAllData(List<JSONObject> aidAllData){
        if(aidAllData == null){
            throw new RuntimeException("Aid is list can not be null!");
        }
        String ckey = "cache_aid_all_data";
        //缓存获取数据
        List<JSONObject> cacheData = DataCacheUtils.getList(ckey, JSONObject.class);
        if(cacheData != null){
            Log.d(TAG, " AidAll 从缓存获取数据。");
            aidAllData.addAll(cacheData);
            return;
        }
        //获取数据
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.aidQueryAllUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.aidQueryAllUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") > 0){
                    JSONArray data = resObj.getJSONArray("data");
                    for (int i = 0; i < data.size(); i++) {
                        aidAllData.add(data.getJSONObject(i));
                    }
                    DataCacheUtils.set(ckey, aidAllData);
                }
            }
        });
    }

    /**
     * 查询用户航标
     * @param aidUserData
     */
    public static void getAidUserData(List<JSONObject> aidUserData){
        if(aidUserData == null){
            throw new RuntimeException("Aid is list can not be null!");
        }
        String ckey = "cache_aid_user_data";
        //缓存获取数据
        List<JSONObject> cacheData = DataCacheUtils.getList(ckey, JSONObject.class);
        if(cacheData != null){
            Log.d(TAG, " AidUser 从缓存获取数据。");
            aidUserData.addAll(cacheData);
            return;
        }
        //获取数据
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.userAidUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.userAidUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") > 0){
                    JSONArray data = resObj.getJSONArray("data");
                    for (int i = 0; i < data.size(); i++) {
                        aidUserData.add(data.getJSONObject(i));
                    }
                    DataCacheUtils.set(ckey, aidUserData);
                }
            }
        });
    }

}
