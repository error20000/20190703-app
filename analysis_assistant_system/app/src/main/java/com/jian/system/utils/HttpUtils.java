package com.jian.system.utils;

import java.util.Map;
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
        return getInstance(10, 10, 10);
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
        for (String key: params.keySet()) {
            builder.add(key, String.valueOf(params.get(key)));
        }
        return sendPost(url, builder.build());
    }

    public String sendPost(String url, RequestBody body){
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String sendGet(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
