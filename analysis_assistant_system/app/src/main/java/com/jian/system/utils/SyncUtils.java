package com.jian.system.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jian.system.Application;
import com.jian.system.config.UrlConfig;
import com.jian.system.dao.NfcMapper;
import com.jian.system.entity.System;
import com.jian.system.service.SystemService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncUtils {

    private final static String TAG = SyncUtils.class.getSimpleName();

    public static void initData(){

    }

    public static void systemData(){
        //获取数据
        ThreadUtils.executes(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                String res = HttpUtils.getInstance().sendPost(UrlConfig.syncSystemUrl, params);
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
                Log.e(TAG, JSON.toJSONString(service.selectAll()));
                Log.d(TAG, "System 数据同步成功。");
            }
        });
    }

}
