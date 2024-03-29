package com.jian.system.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.Application;
import com.jian.system.gesture.util.GestureUtils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    private static HttpUtils instance = null;
    OkHttpClient client = null;

    public static HttpUtils getInstance(){
        return getInstance(60, 60, 60);
    }
    public static HttpUtils getInstance(int ctimeout, int wtimeout, int rtimeout){
        if (instance == null) {
            instance = new HttpUtils(ctimeout, wtimeout, rtimeout);
        }
        return instance;
    }

    public HttpUtils(int ctimeout, int wtimeout, int rtimeout){
        client = new OkHttpClient.Builder()
                .connectTimeout(ctimeout, TimeUnit.SECONDS)
                .writeTimeout(wtimeout, TimeUnit.SECONDS)
                .readTimeout(rtimeout, TimeUnit.SECONDS)
                .build();
    }

    public String sendPost(String url, Map<String, Object> params){
        FormBody.Builder builder = new FormBody.Builder();
        if(params != null){
            for (String key: params.keySet()) {
                builder.add(key, String.valueOf(params.get(key)));
            }
        }
        Log.d("HttpUtils params", JSONObject.toJSONString(params) );
        return sendPost(url, builder.build());
    }

    public String sendPost(String url, RequestBody body){
        Request request = new Request.Builder()
                .url(url)
                .header("token", Application.getTokenStr())
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            Log.d("HttpUtils sendPost", url + "   " + response.code() );
            if(response.code() == 200){
                String res = response.body().string();
                Log.d("HttpUtils sendPost", res);
                return res;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String sendGet(String url){
        Request request = new Request.Builder()
                .url(url)
                .header("token", Application.getTokenStr())
                .build();
        try (Response response = client.newCall(request).execute()) {
            Log.d("HttpUtils sendGet", url + "   " + response.code());
            if(response.code() == 200){
                String res = response.body().string();
                Log.d("HttpUtils sendGet", res);
                return res;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


}
