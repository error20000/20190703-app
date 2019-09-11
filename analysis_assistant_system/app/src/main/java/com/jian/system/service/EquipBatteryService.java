package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipAisMapper;
import com.jian.system.dao.EquipBatteryMapper;
import com.jian.system.entity.EquipAis;
import com.jian.system.entity.EquipBattery;

import java.util.List;
import java.util.Map;

public class EquipBatteryService {

    private EquipBatteryMapper baseMapper;

    public EquipBatteryService(Context context){
        baseMapper = new EquipBatteryMapper(context);
    }


    public EquipBattery selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }

    public EquipBatteryMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<EquipBattery> data){
        baseMapper.insert(data);
    }
}
