package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipBatteryMapper;
import com.jian.system.entity.EquipBattery;

import java.util.Map;

public class EquipBatteryService {

    private EquipBatteryMapper baseMapper;

    public EquipBatteryService(Context context){
        baseMapper = new EquipBatteryMapper(context);
    }


    public EquipBattery selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }
}
