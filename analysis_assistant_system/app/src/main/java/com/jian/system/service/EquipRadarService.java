package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipLampMapper;
import com.jian.system.dao.EquipRadarMapper;
import com.jian.system.entity.EquipLamp;
import com.jian.system.entity.EquipRadar;

import java.util.List;
import java.util.Map;

public class EquipRadarService {

    private EquipRadarMapper baseMapper;

    public EquipRadarService(Context context){
        baseMapper = new EquipRadarMapper(context);
    }


    public EquipRadar selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }

    public EquipRadarMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<EquipRadar> data){
        baseMapper.insert(data);
    }
}
