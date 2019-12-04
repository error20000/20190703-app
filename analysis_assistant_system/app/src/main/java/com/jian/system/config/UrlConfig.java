package com.jian.system.config;

import com.jian.system.utils.Utils;

import java.lang.reflect.Field;

public class UrlConfig {

    public static boolean debug = true;  //false 关闭可选测试地址

    public static String baseUrl = "http://192.168.1.33:8065";
//    public static String baseUrl = "http://gztest.yunh.xyz:8085";

    //系统
    public static String sysQueryUrl = "/api/sys/app/findOne";

    //用户
    public static String userLoginUrl = "/api/user/app/login";
    public static String userLogoutUrl = "/api/user/app/logout";
    public static String userIsLoginUrl = "/api/user/app/isLogin";
    public static String userAidUrl = "/api/user/app/aid";
    public static String userAllUrl = "/api/user/app/findAll";
    public static String userChangePwdUrl = "/api/user/app/changePWD";


    //查询数据字典
    public static String dictQueryUrl = "/api/dict/app/findList";

    //器材
    public static String equipQueryPageUrl = "/api/equip/app/findPage";
    public static String equipQueryDetailUrl = "/api/equip/app/detail";
    public static String equipQueryNfcUrl = "/api/equip/app/nfc";
    public static String equipQueryScanUrl = "/api/equip/app/findOne";
    public static String equipQueryHistoryUrl = "/api/equip/app/history"; //历史
    public static String equipSearchUrl = "/api/equip/app/search"; //搜索
    public static String equipAddUrl = "/api/equip/app/add"; //新增
    public static String equipInStoreUrl = "/api/equip/app/inStore"; //入库
    public static String equipOutStoreUrl = "/api/equip/app/outStore"; //出库
    public static String equipRemoveUrl = "/api/equip/app/remove"; //拆除
    public static String equipTransportUrl = "/api/equip/app/transport"; //运输
    public static String equipToBeTestUrl = "/api/equip/app/toBeTest"; //待检测
    public static String equipCheckUrl = "/api/equip/app/check"; //检测
    public static String equipRepairUrl = "/api/equip/app/repair"; //维修
    public static String equipDumpUrl = "/api/equip/app/dump"; //报废
    public static String equipUseToAidUrl = "/api/equip/app/useToAid"; //使用
    public static String equipUnusualUrl = "/api/equip/app/unusual"; //异常

    //仓库
    public static String storeTypeQueryUrl = "/api/store/app/findType";
    public static String storeQueryUrl = "/api/store/app/findList";
    public static String storeQueryMapUrl = "/api/store/app/map";
    public static String storeQueryEquipUrl = "/api/store/app/equip";

    //NFC
    public static String nfcQueryUrl = "/api/nfc/app/findAll";
    public static String nfcUnusedQueryUrl = "/api/nfc/app/unbind";
    public static String nfcFindAndAddUrl = "/api/nfc/app/findAndAdd";

    //航标
    public static String aidQueryAllUrl = "/api/aid/app/findAll";
    public static String aidQueryPageUrl = "/api/aid/app/findPage";
    public static String aidQueryDetailUrl = "/api/aid/app/findOne";
    public static String aidSearchUrl = "/api/aid/app/search"; //搜索
    public static String aidQueryEquipUrl = "/api/aid/app/equip";
    public static String aidQueryMapUrl = "/api/aid/app/map";
    public static String aidUnusualUrl = "/api/aid/app/unusual"; //异常
    public static String aidNormalUrl = "/api/aid/app/normal"; //正常


    //消息
    public static String msgQueryPageUrl = "/api/msg/app/findPage";
    public static String msgQueryDetailUrl = "/api/msg/app/findView";
    public static String msgSearchUrl = "/api/msg/app/search"; //搜索
    public static String msgHandleUrl = "/api/msg/app/handle";
    public static String msgUnHandleUrl = "/api/msg/app/unhandle";
    public static String msgReadUrl = "/api/msg/app/read";
    public static String msgAddUrl = "/api/msg/app/add";
    public static String msgUnReadNumUrl = "/api/msg/app/unReadNum";

    //同步数据
    public static String syncSystemUrl = "/api/sync/app/system";
    public static String syncUserUrl = "/api/sync/app/user";
    public static String syncUserAidUrl = "/api/sync/app/userAid";
    public static String syncDictUrl = "/api/sync/app/dict";
    public static String syncStoreUrl = "/api/sync/app/store";
    public static String syncStoreTypeUrl = "/api/sync/app/storeType";
    public static String syncNfcUrl = "/api/sync/app/nfc";
    public static String syncAidUrl = "/api/sync/app/aid";
    public static String syncAidEquipUrl = "/api/sync/app/aidEquip";
    public static String syncAidMapIconUrl = "/api/sync/app/aidMapIcon";
    public static String syncAidTypeMapIconUrl = "/api/sync/app/aidTypeMapIcon";
    public static String syncEquipUrl = "/api/sync/app/equip";
    public static String syncEquipLogUrl = "/api/sync/app/equipLog";
    public static String syncEquipAisUrl = "/api/sync/app/equipAis";
    public static String syncEquipBatteryUrl = "/api/sync/app/equipBattery";
    public static String syncEquipLampUrl = "/api/sync/app/equipLamp";
    public static String syncEquipRadarUrl = "/api/sync/app/equipRadar";
    public static String syncEquipSolarEnergyUrl = "/api/sync/app/equipSolarEnergy";
    public static String syncEquipSpareLampUrl = "/api/sync/app/equipSpareLamp";
    public static String syncEquipTelemetryUrl = "/api/sync/app/equipTelemetry";
    public static String syncEquipViceLampUrl = "/api/sync/app/equipViceLamp";
    public static String syncMsgUrl = "/api/sync/app/msg";

    static {
        updateBaseUrl("");
    }

    public static void updateBaseUrl(String newBaseUrl) {
        String before = baseUrl;
        //更新base url
        if(!Utils.isNullOrEmpty(newBaseUrl)){
            baseUrl = newBaseUrl;
        }
        //更新其他url
        Field[]  f = UrlConfig.class.getDeclaredFields();
        for (int i = 0; i < f.length; i++) {
            if("debug".equals(f[i].getName()) || "baseUrl".equals(f[i].getName())) {
                continue;
            }
            f[i].setAccessible(true);
            if(f[i].getType().toString().endsWith("java.lang.String") ) {
                try {
                    f[i].set(UrlConfig.class, baseUrl + f[i].get(UrlConfig.class).toString().replace(before, ""));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
