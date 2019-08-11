package com.jian.system.config;

public class UrlConfig {

    public static String baseUrl = "http://192.168.1.11:8065";

    //查询数据字典
    public static String dictQueryUrl = baseUrl + "/api/dict/findList";

    //器材
    public static String equipQueryPageUrl = baseUrl + "/api/equip/findPage";
    public static String equipQueryDetailUrl = baseUrl + "/api/equip/findOne";
    public static String equipQueryHistoryUrl = baseUrl + "/api/equip/history";

    //仓库
    public static String storeTypeQueryUrl = baseUrl + "/api/store/findType";
    public static String storeQueryUrl = baseUrl + "/api/store/findList";

    //NFC
    public static String nfcQueryUrl = baseUrl + "/api/nfc/findAll";
    public static String nfcUnusedQueryUrl = baseUrl + "/api/nfc/unbind";
}
