package com.jian.system.config;

public class UrlConfig {

    public static String baseUrl = "http://192.168.1.11:8065";

    //器材
    public static String equipQueryPageUrl = baseUrl + "/api/equip/findPage";
    public static String equipQueryDetailUrl = baseUrl + "/api/equip/findOne";
}
