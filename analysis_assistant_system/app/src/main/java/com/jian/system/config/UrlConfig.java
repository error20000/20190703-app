package com.jian.system.config;

public class UrlConfig {

    public static String baseUrl = "http://192.168.1.11:8065";

    //查询数据字典
    public static String dictQueryUrl = baseUrl + "/api/dict/findList";

    //器材
    public static String equipQueryPageUrl = baseUrl + "/api/equip/findPage";
    public static String equipQueryDetailUrl = baseUrl + "/api/equip/findOne";
    public static String equipQueryHistoryUrl = baseUrl + "/api/equip/app/history"; //历史
    public static String equipAddUrl = baseUrl + "/api/equip/app/add"; //新增
    public static String equipInStoreUrl = baseUrl + "/api/equip/app/inStore"; //入库
    public static String equipOutStoreUrl = baseUrl + "/api/equip/app/outStore"; //出库
    public static String equipRemoveUrl = baseUrl + "/api/equip/app/remove"; //拆除
    public static String equipTransportUrl = baseUrl + "/api/equip/app/transport"; //运输
    public static String equipToBeTestUrl = baseUrl + "/api/equip/app/toBeTest"; //待检测
    public static String equipCheckUrl = baseUrl + "/api/equip/app/check"; //检测
    public static String equipRepairUrl = baseUrl + "/api/equip/app/repair"; //维修
    public static String equipDumpUrl = baseUrl + "/api/equip/app/dump"; //报废
    public static String equipUseToAidUrl = baseUrl + "/api/equip/app/useToAid"; //使用

    //仓库
    public static String storeTypeQueryUrl = baseUrl + "/api/store/findType";
    public static String storeQueryUrl = baseUrl + "/api/store/findList";

    //NFC
    public static String nfcQueryUrl = baseUrl + "/api/nfc/findAll";
    public static String nfcUnusedQueryUrl = baseUrl + "/api/nfc/unbind";
}
