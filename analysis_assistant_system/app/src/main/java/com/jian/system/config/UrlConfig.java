package com.jian.system.config;

public class UrlConfig {

    public static String baseUrl = "http://192.168.1.36:8065";
//    public static String baseUrl = "http://gztest.yunh.xyz:8085";

    //系统
    public static String sysQueryUrl = baseUrl + "/api/sys/app/findOne";

    //用户
    public static String userLoginUrl = baseUrl + "/api/user/app/login";
    public static String userLogoutUrl = baseUrl + "/api/user/app/logout";
    public static String userIsLoginUrl = baseUrl + "/api/user/app/isLogin";
    public static String userAidUrl = baseUrl + "/api/user/app/aid";
    public static String userAllUrl = baseUrl + "/api/user/app/findAll";

    //查询数据字典
    public static String dictQueryUrl = baseUrl + "/api/dict/app/findList";

    //器材
    public static String equipQueryPageUrl = baseUrl + "/api/equip/app/findPage";
    public static String equipQueryDetailUrl = baseUrl + "/api/equip/app/detail";
    public static String equipQueryNfcUrl = baseUrl + "/api/equip/app/nfc";
    public static String equipQueryScanUrl = baseUrl + "/api/equip/app/findOne";
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
    public static String equipUnusualUrl = baseUrl + "/api/equip/app/unusual"; //异常

    //仓库
    public static String storeTypeQueryUrl = baseUrl + "/api/store/app/findType";
    public static String storeQueryUrl = baseUrl + "/api/store/app/findList";
    public static String storeQueryMapUrl = baseUrl + "/api/store/app/map";
    public static String storeQueryEquipUrl = baseUrl + "/api/store/app/equip";

    //NFC
    public static String nfcQueryUrl = baseUrl + "/api/nfc/app/findAll";
    public static String nfcUnusedQueryUrl = baseUrl + "/api/nfc/app/unbind";
    public static String nfcFindAndAddUrl = baseUrl + "/api/nfc/app/findAndAdd";

    //航标
    public static String aidQueryAllUrl = baseUrl + "/api/aid/app/findAll";
    public static String aidQueryPageUrl = baseUrl + "/api/aid/app/findPage";
    public static String aidQueryDetailUrl = baseUrl + "/api/aid/app/findOne";
    public static String aidSearchUrl = baseUrl + "/api/aid/app/search"; //搜索
    public static String aidQueryEquipUrl = baseUrl + "/api/aid/app/equip";
    public static String aidQueryMapUrl = baseUrl + "/api/aid/app/map";
    public static String aidUnusualUrl = baseUrl + "/api/aid/app/unusual"; //异常
    public static String aidNormalUrl = baseUrl + "/api/aid/app/normal"; //正常


    //消息
    public static String msgQueryPageUrl = baseUrl + "/api/msg/app/findPage";
    public static String msgQueryDetailUrl = baseUrl + "/api/msg/app/findView";
    public static String msgSearchUrl = baseUrl + "/api/msg/app/search"; //搜索
    public static String msgHandleUrl = baseUrl + "/api/msg/app/handle";
    public static String msgUnHandleUrl = baseUrl + "/api/msg/app/unhandle";
    public static String msgReadUrl = baseUrl + "/api/msg/app/read";
    public static String msgAddUrl = baseUrl + "/api/msg/app/add";
    public static String msgUnReadNumUrl = baseUrl + "/api/msg/app/unReadNum";

    //同步数据
    public static String syncSystemUrl = baseUrl + "/api/sync/app/system";
    public static String syncUserUrl = baseUrl + "/api/sync/app/user";
    public static String syncUserAidUrl = baseUrl + "/api/sync/app/userAid";
    public static String syncDictUrl = baseUrl + "/api/sync/app/dict";
    public static String syncStoreUrl = baseUrl + "/api/sync/app/store";
    public static String syncStoreTypeUrl = baseUrl + "/api/sync/app/storeType";
    public static String syncNfcUrl = baseUrl + "/api/sync/app/nfc";
    public static String syncAidUrl = baseUrl + "/api/sync/app/aid";
    public static String syncAidEquipUrl = baseUrl + "/api/sync/app/aidEquip";
    public static String syncAidMapIconUrl = baseUrl + "/api/sync/app/aidMapIcon";
    public static String syncAidTypeMapIconUrl = baseUrl + "/api/sync/app/aidTypeMapIcon";
    public static String syncEquipUrl = baseUrl + "/api/sync/app/equip";
    public static String syncEquipLogUrl = baseUrl + "/api/sync/app/equipLog";
    public static String syncEquipAisUrl = baseUrl + "/api/sync/app/equipAis";
    public static String syncEquipBatteryUrl = baseUrl + "/api/sync/app/equipBattery";
    public static String syncEquipLampUrl = baseUrl + "/api/sync/app/equipLamp";
    public static String syncEquipRadarUrl = baseUrl + "/api/sync/app/equipRadar";
    public static String syncEquipSolarEnergyUrl = baseUrl + "/api/sync/app/equipSolarEnergy";
    public static String syncEquipSpareLampUrl = baseUrl + "/api/sync/app/equipSpareLamp";
    public static String syncEquipTelemetryUrl = baseUrl + "/api/sync/app/equipTelemetry";
    public static String syncEquipViceLampUrl = baseUrl + "/api/sync/app/equipViceLamp";
    public static String syncMsgUrl = baseUrl + "/api/sync/app/msg";
}
