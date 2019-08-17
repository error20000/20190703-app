package com.jian.system.config;

public class UrlConfig {

    public static String baseUrl = "http://192.168.1.16:8065";

    //用户
    public static String userLoginUrl = baseUrl + "/api/user/app/login";
    public static String userLogoutUrl = baseUrl + "/api/user/app/logout";
    public static String userIsLoginUrl = baseUrl + "/api/user/app/isLogin";
    public static String userAidUrl = baseUrl + "/api/user/app/aid";

    //查询数据字典
    public static String dictQueryUrl = baseUrl + "/api/dict/app/findList";

    //器材
    public static String equipQueryPageUrl = baseUrl + "/api/equip/app/findPage";
    public static String equipQueryDetailUrl = baseUrl + "/api/equip/app/findOne";
    public static String equipQueryHistoryUrl = baseUrl + "/api/equip/app/history"; //历史
    public static String equipSearchUrl = baseUrl + "/api/equip/app/search"; //搜索
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
    public static String storeTypeQueryUrl = baseUrl + "/api/store/app/findType";
    public static String storeQueryUrl = baseUrl + "/api/store/app/findList";

    //NFC
    public static String nfcQueryUrl = baseUrl + "/api/nfc/app/findAll";
    public static String nfcUnusedQueryUrl = baseUrl + "/api/nfc/app/unbind";

    //航标
    public static String aidQueryAllUrl = baseUrl + "/api/aid/app/findAll";
    public static String aidQueryPageUrl = baseUrl + "/api/aid/app/findPage";
    public static String aidQueryDetailUrl = baseUrl + "/api/aid/app/findOne";
    public static String aidSearchUrl = baseUrl + "/api/aid/app/search"; //搜索
    public static String aidQueryEquipUrl = baseUrl + "/api/aid/app/equip";

}
