package com.jian.system.service;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jian.system.config.Constant;
import com.jian.system.dao.EquipMapper;
import com.jian.system.dao.UserMapper;
import com.jian.system.entity.Equip;
import com.jian.system.entity.EquipLog;
import com.jian.system.entity.User;
import com.jian.system.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipService {

    private EquipMapper baseMapper;
    private EquipLogService logService;
    private EquipAisService aisService;
    private EquipBatteryService batteryService;
    private EquipLampService lampService;
    private EquipRadarService radarService;
    private EquipSolarEnergyService solarEnergyService;
    private EquipSpareLampService spareLampService;
    private EquipTelemetryService telemetryService;
    private EquipViceLampService viceLampService;

    public EquipService(Context context){
        baseMapper = new EquipMapper(context);
        logService = new EquipLogService(context);
        aisService = new EquipAisService(context);
        batteryService = new EquipBatteryService(context);
        lampService = new EquipLampService(context);
        radarService = new EquipRadarService(context);
        solarEnergyService = new EquipSolarEnergyService(context);
        spareLampService = new EquipSpareLampService(context);
        telemetryService = new EquipTelemetryService(context);
        viceLampService = new EquipViceLampService(context);
    }

    public List<Equip> selectPage(Map<String, Object> condition, int page, int rows){
        int start = page <= 1 ? 0 : (page - 1) * rows;
        return baseMapper.selectPage(condition, start, rows);
    }

    public long size(Map<String, Object> condition){
        return baseMapper.size(condition);
    }

    public Equip selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }

    public Map<String, Object> detail(String sEquip_ID){
        Map<String, Object> res = new HashMap<String, Object>();
        if(Utils.isNullOrEmpty(sEquip_ID)) {
            return res;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("sEquip_ID", sEquip_ID);
        Equip test = baseMapper.selectOne(condition);
        if(test == null) {
            return res;
        }
        if(Utils.isNullOrEmpty(test.getsEquip_Type())) {
            return res;
        }
        res.putAll(Utils.objToMap(test));
        //查询详情
        Object detail = null;
        Map<String, Object> condetail = new HashMap<String, Object>();
        condetail.put("sEquip_ID", test.getsEquip_ID());
        switch (test.getsEquip_Type()) {
            case Constant.EquipType_AIS:
                detail = aisService.selectOne(condetail);
                break;
            case Constant.EquipType_Battery:
                detail = batteryService.selectOne(condetail);
                break;
            case Constant.EquipType_Lamp:
                detail = lampService.selectOne(condetail);
                break;
            case Constant.EquipType_Radar:
                detail = radarService.selectOne(condetail);
                break;
            case Constant.EquipType_SolarEnergy:
                detail = solarEnergyService.selectOne(condetail);
                break;
            case Constant.EquipType_SpareLamp:
                detail = spareLampService.selectOne(condetail);
                break;
            case Constant.EquipType_Telemetry:
                detail = telemetryService.selectOne(condetail);
                break;
            case Constant.EquipType_ViceLamp:
                detail = viceLampService.selectOne(condetail);
                break;

            default:
                break;
        }
        if(detail != null) {
            res.putAll(Utils.objToMap(detail));
        }
        return res;
    }

    public List<Equip> selectList(Map<String, Object> condition){
        return baseMapper.selectList(condition);
    }

    public List<EquipLog> history(String sEquip_ID){
        if(Utils.isNullOrEmpty(sEquip_ID)) {
            return new ArrayList<>();
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("sELog_EquipID", sEquip_ID);
        //排序
        List<EquipLog> logs = logService.selectList(condition);
        for (int i = 0; i < logs.size(); i++) {
            for (int j = i; j < logs.size(); j++) {
                if(logs.get(i).getdELog_CreateDate().getTime() > logs.get(j).getdELog_CreateDate().getTime()) {
                    EquipLog temp = logs.get(i);
                    logs.set(i, logs.get(j));
                    logs.set(j, temp);
                }
            }
        }
        return logs;
    }
}
