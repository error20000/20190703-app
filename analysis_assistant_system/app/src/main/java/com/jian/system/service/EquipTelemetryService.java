package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipSpareLampMapper;
import com.jian.system.dao.EquipTelemetryMapper;
import com.jian.system.entity.EquipSpareLamp;
import com.jian.system.entity.EquipTelemetry;

import java.util.List;
import java.util.Map;

public class EquipTelemetryService {

    private EquipTelemetryMapper baseMapper;

    public EquipTelemetryService(Context context){
        baseMapper = new EquipTelemetryMapper(context);
    }


    public EquipTelemetry selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }

    public EquipTelemetryMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<EquipTelemetry> data){
        baseMapper.insert(data);
    }
}
