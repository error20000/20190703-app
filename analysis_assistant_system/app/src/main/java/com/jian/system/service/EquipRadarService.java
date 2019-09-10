package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipRadarMapper;
import com.jian.system.entity.EquipRadar;

import java.util.Map;

public class EquipRadarService {

    private EquipRadarMapper baseMapper;

    public EquipRadarService(Context context){
        baseMapper = new EquipRadarMapper(context);
    }


    public EquipRadar selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }
}
