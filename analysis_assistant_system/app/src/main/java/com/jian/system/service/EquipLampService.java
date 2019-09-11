package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipBatteryMapper;
import com.jian.system.dao.EquipLampMapper;
import com.jian.system.entity.EquipBattery;
import com.jian.system.entity.EquipLamp;

import java.util.List;
import java.util.Map;

public class EquipLampService {

    private EquipLampMapper baseMapper;

    public EquipLampService(Context context){
        baseMapper = new EquipLampMapper(context);
    }


    public EquipLamp selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }

    public EquipLampMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<EquipLamp> data){
        baseMapper.insert(data);
    }
}
