package com.jian.system.config;

public class UrlConfig {

    public static boolean debug = true;  //false 关闭可选测试地址

    public static String baseUrl = "http://192.168.59.168:8065";
//    public static String baseUrl = "http://gztest.yunh.xyz:8085";

    //系统
    public static String sysQueryUrl = getBaseUrl() + "/api/sys/app/findOne";

    //用户
    public static String userLoginUrl = getBaseUrl() + "/api/user/app/login";
    public static String userLogoutUrl = getBaseUrl() + "/api/user/app/logout";
    public static String userIsLoginUrl = getBaseUrl() + "/api/user/app/isLogin";
    public static String userAidUrl = getBaseUrl() + "/api/user/app/aid";
    public static String userAllUrl = getBaseUrl() + "/api/user/app/findAll";
    public static String userChangePwdUrl = getBaseUrl() + "/api/user/app/changePWD";


    //查询数据字典
    public static String dictQueryUrl = getBaseUrl() + "/api/dict/app/findList";

    //器材
    public static String equipQueryPageUrl = getBaseUrl() + "/api/equip/app/findPage";
    public static String equipQueryDetailUrl = getBaseUrl() + "/api/equip/app/detail";
    public static String equipQueryNfcUrl = getBaseUrl() + "/api/equip/app/nfc";
    public static String equipQueryScanUrl = getBaseUrl() + "/api/equip/app/findOne";
    public static String equipQueryHistoryUrl = getBaseUrl() + "/api/equip/app/history"; //历史
    public static String equipSearchUrl = getBaseUrl() + "/api/equip/app/search"; //搜索
    public static String equipAddUrl = getBaseUrl() + "/api/equip/app/add"; //新增
    public static String equipInStoreUrl = getBaseUrl() + "/api/equip/app/inStore"; //入库
    public static String equipOutStoreUrl = getBaseUrl() + "/api/equip/app/outStore"; //出库
    public static String equipRemoveUrl = getBaseUrl() + "/api/equip/app/remove"; //拆除
    public static String equipTransportUrl = getBaseUrl() + "/api/equip/app/transport"; //运输
    public static String equipToBeTestUrl = getBaseUrl() + "/api/equip/app/toBeTest"; //待检测
    public static String equipCheckUrl = getBaseUrl() + "/api/equip/app/check"; //检测
    public static String equipRepairUrl = getBaseUrl() + "/api/equip/app/repair"; //维修
    public static String equipDumpUrl = getBaseUrl() + "/api/equip/app/dump"; //报废
    public static String equipUseToAidUrl = getBaseUrl() + "/api/equip/app/useToAid"; //使用
    public static String equipUnusualUrl = getBaseUrl() + "/api/equip/app/unusual"; //异常

    //仓库
    public static String storeTypeQueryUrl = getBaseUrl() + "/api/store/app/findType";
    public static String storeQueryUrl = getBaseUrl() + "/api/store/app/findList";
    public static String storeQueryMapUrl = getBaseUrl() + "/api/store/app/map";
    public static String storeQueryEquipUrl = getBaseUrl() + "/api/store/app/equip";

    //NFC
    public static String nfcQueryUrl = getBaseUrl() + "/api/nfc/app/findAll";
    public static String nfcUnusedQueryUrl = getBaseUrl() + "/api/nfc/app/unbind";
    public static String nfcFindAndAddUrl = getBaseUrl() + "/api/nfc/app/findAndAdd";

    //航标
    public static String aidQueryAllUrl = getBaseUrl() + "/api/aid/app/findAll";
    public static String aidQueryPageUrl = getBaseUrl() + "/api/aid/app/findPage";
    public static String aidQueryDetailUrl = getBaseUrl() + "/api/aid/app/findOne";
    public static String aidSearchUrl = getBaseUrl() + "/api/aid/app/search"; //搜索
    public static String aidQueryEquipUrl = getBaseUrl() + "/api/aid/app/equip";
    public static String aidQueryMapUrl = getBaseUrl() + "/api/aid/app/map";
    public static String aidUnusualUrl = getBaseUrl() + "/api/aid/app/unusual"; //异常
    public static String aidNormalUrl = getBaseUrl() + "/api/aid/app/normal"; //正常


    //消息
    public static String msgQueryPageUrl = getBaseUrl() + "/api/msg/app/findPage";
    public static String msgQueryDetailUrl = getBaseUrl() + "/api/msg/app/findView";
    public static String msgSearchUrl = getBaseUrl() + "/api/msg/app/search"; //搜索
    public static String msgHandleUrl = getBaseUrl() + "/api/msg/app/handle";
    public static String msgUnHandleUrl = getBaseUrl() + "/api/msg/app/unhandle";
    public static String msgReadUrl = getBaseUrl() + "/api/msg/app/read";
    public static String msgAddUrl = getBaseUrl() + "/api/msg/app/add";
    public static String msgUnReadNumUrl = getBaseUrl() + "/api/msg/app/unReadNum";

    //同步数据
    public static String syncSystemUrl = getBaseUrl() + "/api/sync/app/system";
    public static String syncUserUrl = getBaseUrl() + "/api/sync/app/user";
    public static String syncUserAidUrl = getBaseUrl() + "/api/sync/app/userAid";
    public static String syncDictUrl = getBaseUrl() + "/api/sync/app/dict";
    public static String syncStoreUrl = getBaseUrl() + "/api/sync/app/store";
    public static String syncStoreTypeUrl = getBaseUrl() + "/api/sync/app/storeType";
    public static String syncNfcUrl = getBaseUrl() + "/api/sync/app/nfc";
    public static String syncAidUrl = getBaseUrl() + "/api/sync/app/aid";
    public static String syncAidEquipUrl = getBaseUrl() + "/api/sync/app/aidEquip";
    public static String syncAidMapIconUrl = getBaseUrl() + "/api/sync/app/aidMapIcon";
    public static String syncAidTypeMapIconUrl = getBaseUrl() + "/api/sync/app/aidTypeMapIcon";
    public static String syncEquipUrl = getBaseUrl() + "/api/sync/app/equip";
    public static String syncEquipLogUrl = getBaseUrl() + "/api/sync/app/equipLog";
    public static String syncEquipAisUrl = getBaseUrl() + "/api/sync/app/equipAis";
    public static String syncEquipBatteryUrl = getBaseUrl() + "/api/sync/app/equipBattery";
    public static String syncEquipLampUrl = getBaseUrl() + "/api/sync/app/equipLamp";
    public static String syncEquipRadarUrl = getBaseUrl() + "/api/sync/app/equipRadar";
    public static String syncEquipSolarEnergyUrl = getBaseUrl() + "/api/sync/app/equipSolarEnergy";
    public static String syncEquipSpareLampUrl = getBaseUrl() + "/api/sync/app/equipSpareLamp";
    public static String syncEquipTelemetryUrl = getBaseUrl() + "/api/sync/app/equipTelemetry";
    public static String syncEquipViceLampUrl = getBaseUrl() + "/api/sync/app/equipViceLamp";
    public static String syncMsgUrl = getBaseUrl() + "/api/sync/app/msg";
    
    private static  String getBaseUrl(){
        return baseUrl;
    }
}
