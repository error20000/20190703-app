package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipTelemetryMapper;
import com.jian.system.dao.EquipViceLampMapper;
import com.jian.system.entity.EquipTelemetry;
import com.jian.system.entity.EquipViceLamp;

import java.util.List;
import java.util.Map;

public class EquipViceLampService {

    private EquipViceLampMapper baseMapper;

    public EquipViceLampService(Context context){
        baseMapper = new EquipViceLampMapper(context);
    }


    public EquipViceLamp selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }

    public EquipViceLampMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<EquipViceLamp> data){
        baseMapper.insert(data);
    }
}
