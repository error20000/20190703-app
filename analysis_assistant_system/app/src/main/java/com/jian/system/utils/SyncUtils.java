package com.jian.system.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jian.system.config.UrlConfig;
import com.jian.system.dao.NfcMapper;

import java.util.HashMap;
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
                String res = HttpUtils.getInstance().sendPost(UrlConfig.userAidUrl, params);
                JSONObject resObj = JSONObject.parseObject(res);
                if(resObj.getInteger("code") <= 0){
                    Log.d(TAG, "获取数据失败："+resObj.getString("code")+" "+resObj.getString("msg"));
                    return;
                }
                JSONArray data = resObj.getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    aidUserData.add(data.getJSONObject(i));
                }
                SystemS
            }
        });
    }

}
